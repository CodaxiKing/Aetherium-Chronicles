package com.cronicasaetherium.mod.common.gui.menu;

import com.cronicasaetherium.mod.blocks.synergy.ManaInfuserBlockEntity;
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
 * Menu/Container para a interface da Infusora de Mana
 * 
 * Esta classe gerencia a lógica da interface gráfica da Mana Infuser,
 * a máquina de sinergia que converte energia tecnológica em mana mágico.
 * 
 * Dados sincronizados:
 * - Energia armazenada e capacidade máxima
 * - Progresso de conversão (0-100%)
 * - Estado de processamento ativo/inativo
 * - Nível de mana gerado (futuro sistema de mana)
 * 
 * Layout de slots:
 * - Slot 0: Catalisador (itens que aceleram conversão)
 * - Slot 1: Saída (cristais de mana ou produtos mágicos)
 * - Slot 2: Auxiliar (componentes mágicos extras)
 * - Slots 3-29: Inventário do jogador (hotbar)
 * - Slots 30-38: Inventário do jogador (9 slots principais)
 */
public class ManaInfuserMenu extends AbstractContainerMenu {
    
    private final ManaInfuserBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;
    
    // Índices dos dados sincronizados
    private static final int DATA_ENERGY = 0;
    private static final int DATA_MAX_ENERGY = 1;
    private static final int DATA_PROGRESS = 2;
    private static final int DATA_IS_PROCESSING = 3;
    private static final int DATA_MANA_LEVEL = 4; // Para futuro sistema de mana
    private static final int DATA_COUNT = 5;
    
    /**
     * Construtor para o servidor
     */
    public ManaInfuserMenu(int containerId, Inventory playerInventory, ManaInfuserBlockEntity blockEntity) {
        super(ModMenuTypes.MANA_INFUSER.get(), containerId);
        this.blockEntity = blockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = new SimpleContainerData(DATA_COUNT);
        
        // Adiciona slots da máquina (simulados - inventário real ainda não implementado)
        // TODO: Implementar quando o sistema de inventário for adicionado à ManaInfuserBlockEntity
        // addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 56, 35)); // Catalisador
        // addSlot(new OutputSlot(blockEntity.getInventory(), 1, 116, 35)); // Saída
        // addSlot(new SlotItemHandler(blockEntity.getInventory(), 2, 56, 53)); // Auxiliar
        
        // Por enquanto, adiciona slots vazios para manter a estrutura da GUI
        addSlot(new Slot(new net.minecraft.world.inventory.SimpleContainer(3), 0, 56, 35)); // Catalisador
        addSlot(new Slot(new net.minecraft.world.inventory.SimpleContainer(3), 1, 116, 35)); // Saída
        addSlot(new Slot(new net.minecraft.world.inventory.SimpleContainer(3), 2, 56, 53)); // Auxiliar
        
        // Adiciona slots do jogador
        addPlayerInventory(playerInventory);
        
        // Adiciona container data para sincronização
        addDataSlots(this.data);
    }
    
    /**
     * Construtor para o cliente (via rede)
     */
    public ManaInfuserMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, buf));
    }
    
    /**
     * Obtém a BlockEntity do buffer de rede
     */
    private static ManaInfuserBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (be instanceof ManaInfuserBlockEntity infuser) {
            return infuser;
        }
        throw new IllegalStateException("BlockEntity não é um ManaInfuserBlockEntity!");
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
            
            if (index < 3) {
                // Mover da máquina para o jogador
                if (!moveItemStackTo(originalStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Mover do jogador para a máquina
                if (!moveItemStackTo(originalStack, 0, 3, false)) {
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
        data.set(DATA_PROGRESS, blockEntity.getProcessProgress());
        data.set(DATA_IS_PROCESSING, blockEntity.isProcessing() ? 1 : 0);
        data.set(DATA_MANA_LEVEL, 0); // TODO: Implementar sistema de mana
    }
    
    // Getters para a GUI acessar os dados sincronizados
    public int getEnergyStored() { return data.get(DATA_ENERGY); }
    public int getMaxEnergy() { return data.get(DATA_MAX_ENERGY); }
    public int getProcessProgress() { return data.get(DATA_PROGRESS); }
    public boolean isProcessing() { return data.get(DATA_IS_PROCESSING) == 1; }
    public int getManaLevel() { return data.get(DATA_MANA_LEVEL); }
    
    /**
     * Calcula a altura da barra de energia para renderização
     */
    public int getEnergyBarHeight() {
        int maxHeight = 52; // Altura máxima da barra em pixels
        if (getMaxEnergy() == 0) return 0;
        return (getEnergyStored() * maxHeight) / getMaxEnergy();
    }
    
    /**
     * Calcula o progresso da conversão de mana para renderização
     */
    public int getManaProgressWidth() {
        int maxWidth = 24; // Largura máxima da barra em pixels
        return (getProcessProgress() * maxWidth) / 100;
    }
    
    /**
     * Calcula a altura da barra de mana para renderização
     */
    public int getManaBarHeight() {
        int maxHeight = 40; // Altura máxima da barra em pixels
        int maxMana = 10000; // Capacidade máxima de mana (futuro)
        return (getManaLevel() * maxHeight) / maxMana;
    }
    
    /**
     * Slot especial para saídas que não permite inserção manual
     */
    private static class OutputSlot extends SlotItemHandler {
        public OutputSlot(net.neoforged.neoforge.items.IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
        
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false; // Não permite inserção manual
        }
    }
}