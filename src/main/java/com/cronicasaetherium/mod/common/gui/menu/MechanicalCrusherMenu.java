package com.cronicasaetherium.mod.common.gui.menu;

import com.cronicasaetherium.mod.blocks.tech.MechanicalCrusherBlockEntity;
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
 * Menu/Container para a interface do Triturador Mecânico
 * 
 * Esta classe gerencia a lógica da interface gráfica do Mechanical Crusher,
 * incluindo sincronização de dados entre servidor e cliente, slots de
 * inventário e validação de interações do jogador.
 * 
 * Dados sincronizados:
 * - Energia armazenada e capacidade máxima
 * - Progresso de processamento (0-100%)
 * - Estado de processamento ativo/inativo
 * 
 * Layout de slots:
 * - Slot 0: Entrada (minérios para triturar)
 * - Slot 1: Saída principal (resultado dobrado)
 * - Slot 2: Subproduto (materiais extras com chance)
 * - Slots 3-29: Inventário do jogador (hotbar)
 * - Slots 30-38: Inventário do jogador (9 slots principais)
 */
public class MechanicalCrusherMenu extends AbstractContainerMenu {
    
    private final MechanicalCrusherBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;
    
    // Índices dos dados sincronizados
    private static final int DATA_ENERGY = 0;
    private static final int DATA_MAX_ENERGY = 1;
    private static final int DATA_PROGRESS = 2;
    private static final int DATA_IS_PROCESSING = 3;
    private static final int DATA_COUNT = 4;
    
    /**
     * Construtor para o servidor
     */
    public MechanicalCrusherMenu(int containerId, Inventory playerInventory, MechanicalCrusherBlockEntity blockEntity) {
        super(ModMenuTypes.MECHANICAL_CRUSHER.get(), containerId);
        this.blockEntity = blockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = new SimpleContainerData(DATA_COUNT);
        
        // Adiciona slots da máquina
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 56, 35)); // Entrada
        addSlot(new OutputSlot(blockEntity.getInventory(), 1, 116, 35)); // Saída principal
        addSlot(new OutputSlot(blockEntity.getInventory(), 2, 116, 53)); // Subproduto
        
        // Adiciona slots do jogador
        addPlayerInventory(playerInventory);
        
        // Adiciona container data para sincronização
        addDataSlots(this.data);
    }
    
    /**
     * Construtor para o cliente (via rede)
     */
    public MechanicalCrusherMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, buf));
    }
    
    /**
     * Obtém a BlockEntity do buffer de rede
     */
    private static MechanicalCrusherBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (be instanceof MechanicalCrusherBlockEntity crusher) {
            return crusher;
        }
        throw new IllegalStateException("BlockEntity não é um MechanicalCrusherBlockEntity!");
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
                // Mover do jogador para a máquina (apenas slot de entrada)
                if (!moveItemStackTo(originalStack, 0, 1, false)) {
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
    }
    
    // Getters para a GUI acessar os dados sincronizados
    public int getEnergyStored() { return data.get(DATA_ENERGY); }
    public int getMaxEnergy() { return data.get(DATA_MAX_ENERGY); }
    public int getProcessProgress() { return data.get(DATA_PROGRESS); }
    public boolean isProcessing() { return data.get(DATA_IS_PROCESSING) == 1; }
    
    /**
     * Calcula a altura da barra de energia para renderização
     */
    public int getEnergyBarHeight() {
        int maxHeight = 52; // Altura máxima da barra em pixels
        if (getMaxEnergy() == 0) return 0;
        return (getEnergyStored() * maxHeight) / getMaxEnergy();
    }
    
    /**
     * Calcula o progresso da seta de processamento para renderização
     */
    public int getProgressArrowWidth() {
        int maxWidth = 24; // Largura máxima da seta em pixels
        return (getProcessProgress() * maxWidth) / 100;
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