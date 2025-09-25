package com.cronicasaetherium.mod.common.gui.menu;

import com.cronicasaetherium.mod.blocks.tech.SteamEngineBlockEntity;
import com.cronicasaetherium.mod.common.gui.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * Menu/Container para a interface do Motor a Vapor
 * 
 * Esta classe gerencia a lógica da interface gráfica do Steam Engine,
 * incluindo sincronização de dados entre servidor e cliente, slots de
 * inventário e validação de interações do jogador.
 * 
 * Dados sincronizados:
 * - Energia armazenada e capacidade máxima
 * - Tempo de queima atual e máximo
 * - Progresso de aquecimento
 * - Nível de água
 * - Estado ativo/inativo
 * 
 * Layout de slots:
 * - Slot 0: Combustível (carvão, madeira, etc.)
 * - Slot 1: Auxiliar (bucket de água)
 * - Slots 2-28: Inventário do jogador (hotbar)
 * - Slots 29-37: Inventário do jogador (9 slots principais)
 */
public class SteamEngineMenu extends AbstractContainerMenu {
    
    private final SteamEngineBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;
    
    // Índices dos dados sincronizados
    private static final int DATA_ENERGY = 0;
    private static final int DATA_MAX_ENERGY = 1;
    private static final int DATA_FUEL_TIME = 2;
    private static final int DATA_MAX_FUEL_TIME = 3;
    private static final int DATA_WARMUP_PROGRESS = 4;
    private static final int DATA_WATER_LEVEL = 5;
    private static final int DATA_IS_ACTIVE = 6;
    private static final int DATA_COUNT = 7;
    
    /**
     * Construtor para o servidor
     */
    public SteamEngineMenu(int containerId, Inventory playerInventory, SteamEngineBlockEntity blockEntity) {
        super(ModMenuTypes.STEAM_ENGINE.get(), containerId);
        this.blockEntity = blockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = new SimpleContainerData(DATA_COUNT);
        
        // Adiciona slots da máquina
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 56, 35)); // Combustível
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 1, 56, 53)); // Água
        
        // Adiciona slots do jogador
        addPlayerInventory(playerInventory);
        
        // Adiciona container data para sincronização
        addDataSlots(this.data);
    }
    
    /**
     * Construtor para o cliente (via rede)
     */
    public SteamEngineMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, buf));
    }
    
    /**
     * Obtém a BlockEntity do buffer de rede
     */
    private static SteamEngineBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (be instanceof SteamEngineBlockEntity steamEngine) {
            return steamEngine;
        }
        throw new IllegalStateException("BlockEntity não é um SteamEngineBlockEntity!");
    }
    
    /**
     * Adiciona os slots do inventário do jogador
     */
    private void addPlayerInventory(Inventory playerInventory) {
        // Hotbar (slots 0-8)
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        
        // Inventário principal (slots 9-35)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18));
            }
        }
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            
            if (index < 2) {
                // Mover da máquina para o jogador
                if (!moveItemStackTo(originalStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Mover do jogador para a máquina
                if (!moveItemStackTo(originalStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return newStack;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, blockEntity.getBlockState().getBlock());
    }
    
    /**
     * Atualiza os dados sincronizados a cada tick
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        
        // Atualiza dados da BlockEntity
        data.set(DATA_ENERGY, blockEntity.getEnergyStored());
        data.set(DATA_MAX_ENERGY, blockEntity.getMaxEnergy());
        data.set(DATA_FUEL_TIME, blockEntity.getFuelBurnTime());
        data.set(DATA_MAX_FUEL_TIME, blockEntity.getMaxBurnTime());
        data.set(DATA_WARMUP_PROGRESS, blockEntity.getWarmupProgress());
        data.set(DATA_WATER_LEVEL, blockEntity.getWaterLevel());
        data.set(DATA_IS_ACTIVE, blockEntity.isActive() ? 1 : 0);
    }
    
    // Getters para a GUI acessar os dados sincronizados
    public int getEnergyStored() { return data.get(DATA_ENERGY); }
    public int getMaxEnergy() { return data.get(DATA_MAX_ENERGY); }
    public int getFuelBurnTime() { return data.get(DATA_FUEL_TIME); }
    public int getMaxFuelTime() { return data.get(DATA_MAX_FUEL_TIME); }
    public int getWarmupProgress() { return data.get(DATA_WARMUP_PROGRESS); }
    public int getWaterLevel() { return data.get(DATA_WATER_LEVEL); }
    public boolean isActive() { return data.get(DATA_IS_ACTIVE) == 1; }
    
    /**
     * Calcula a altura da barra de energia para renderização
     */
    public int getEnergyBarHeight() {
        int maxHeight = 52; // Altura máxima da barra em pixels
        if (getMaxEnergy() == 0) return 0;
        return (getEnergyStored() * maxHeight) / getMaxEnergy();
    }
    
    /**
     * Calcula a altura da barra de combustível para renderização
     */
    public int getFuelBarHeight() {
        int maxHeight = 14; // Altura máxima da barra em pixels
        if (getMaxFuelTime() == 0) return 0;
        return (getFuelBurnTime() * maxHeight) / getMaxFuelTime();
    }
    
    /**
     * Calcula a altura da barra de aquecimento para renderização
     */
    public int getWarmupBarHeight() {
        int maxHeight = 24; // Altura máxima da barra em pixels
        return (getWarmupProgress() * maxHeight) / 100;
    }
    
    /**
     * Calcula a altura da barra de água para renderização
     */
    public int getWaterBarHeight() {
        int maxHeight = 32; // Altura máxima da barra em pixels
        int maxWater = 1000; // Capacidade máxima de água
        return (getWaterLevel() * maxHeight) / maxWater;
    }
}