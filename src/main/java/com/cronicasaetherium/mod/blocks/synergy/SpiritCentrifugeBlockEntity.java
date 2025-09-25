package com.cronicasaetherium.mod.blocks.synergy;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.registry.ModBlockEntities;
import com.cronicasaetherium.mod.common.capability.ModEnergyStorage;
import com.cronicasaetherium.mod.common.capability.ModItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * BlockEntity da Centrífuga Espiritual - Processamento automatizado de magia
 * 
 * Esta máquina representa a evolução da sinergia tech/magic, permitindo que
 * jogadores tecnológicos processem elementos mágicos de forma eficiente.
 * 
 * Sistema de Processamento:
 * - Slot 0: Entrada - Bolsas de Espírito cheias (mistas)
 * - Slot 1: Saída Espírito Puro (branco)
 * - Slot 2: Saída Espírito Maligno (vermelho)  
 * - Slot 3: Saída Espírito Arcano (azul)
 * 
 * Operação:
 * - Consome 500 FE por operação (custo moderado)
 * - Tempo de processamento: 5 segundos (100 ticks)
 * - Separa espíritos mistos em tipos específicos
 * - Interface compatível com automação de outros mods
 * 
 * Esta máquina resolve o problema de separação manual de espíritos,
 * automatizando uma tarefa tradicionalmente mágica usando tecnologia.
 */
public class SpiritCentrifugeBlockEntity extends BlockEntity {
    
    // Constantes de configuração da máquina
    private static final int MAX_ENERGY = 25000; // Capacidade de energia (alta para operação contínua)
    private static final int ENERGY_PER_OPERATION = 500; // FE consumido por separação
    private static final int PROCESSING_TIME = 100; // 5 segundos por operação
    
    // Estado interno da máquina
    private int processProgress = 0;
    private boolean isProcessing = false;
    
    // Sistema de energia (consome FE)
    private final ModEnergyStorage energyStorage = new ModEnergyStorage(
        MAX_ENERGY, 1000, 0, // Apenas recebe energia (não gera)
        this::setChanged
    );
    
    // Inventário: [0] entrada, [1-3] saídas por tipo de espírito
    private final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            syncToClient();
        }
        
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) {
                // Slot de entrada: apenas Bolsas de Espírito cheias
                return isSpiritBag(stack) && !stack.isEmpty();
            }
            return false; // Slots de saída não aceitam inserção manual
        }
    };
    
    /**
     * Construtor da BlockEntity
     */
    public SpiritCentrifugeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public SpiritCentrifugeBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.SPIRIT_CENTRIFUGE.get(), pos, state);
    }
    
    /**
     * Método principal de processamento - executado a cada tick
     */
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }
        
        boolean wasProcessing = isProcessing;
        
        // Verifica se pode processar
        if (canProcess()) {
            if (!isProcessing) {
                startProcessing();
            }
            
            continueProcessing();
        } else {
            stopProcessing();
        }
        
        // Sincroniza com cliente se mudou estado
        if (wasProcessing != isProcessing) {
            setChanged();
            syncToClient();
        }
    }
    
    /**
     * Verifica se a máquina pode processar
     */
    private boolean canProcess() {
        // Verifica energia suficiente
        if (energyStorage.getEnergyStored() < ENERGY_PER_OPERATION) {
            return false;
        }
        
        // Verifica se há bolsa de espírito na entrada
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty() || !isSpiritBag(input)) {
            return false;
        }
        
        // Verifica se há espaço nos slots de saída
        SeparationResult result = getSeparationResult(input);
        if (result == null) {
            return false;
        }
        
        // Verifica cada slot de saída
        for (int i = 1; i <= 3; i++) {
            ItemStack output = result.getOutput(i - 1);
            if (!output.isEmpty()) {
                ItemStack currentSlot = inventory.getStackInSlot(i);
                if (!currentSlot.isEmpty()) {
                    if (!ItemStack.isSameItemSameComponents(currentSlot, output) ||
                        currentSlot.getCount() + output.getCount() > currentSlot.getMaxStackSize()) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Inicia um novo processo de separação
     */
    private void startProcessing() {
        isProcessing = true;
        processProgress = 0;
        
        CronicasAetherium.LOGGER.debug("Centrífuga Espiritual iniciou separação na posição {}", getBlockPos());
    }
    
    /**
     * Continua o processo atual
     */
    private void continueProcessing() {
        processProgress++;
        
        // Consome energia gradualmente
        if (processProgress % 20 == 0) { // A cada segundo
            energyStorage.removeEnergyDirect(ENERGY_PER_OPERATION / 5);
        }
        
        // Completa o processo
        if (processProgress >= PROCESSING_TIME) {
            completeProcessing();
        }
    }
    
    /**
     * Para o processamento
     */
    private void stopProcessing() {
        if (isProcessing) {
            isProcessing = false;
            processProgress = 0;
        }
    }
    
    /**
     * Completa um ciclo de processamento
     */
    private void completeProcessing() {
        ItemStack input = inventory.getStackInSlot(0);
        SeparationResult result = getSeparationResult(input);
        
        if (result != null) {
            // Consome item de entrada
            input.shrink(1);
            inventory.setStackInSlot(0, input);
            
            // Distribui resultados nos slots de saída
            for (int i = 0; i < 3; i++) {
                ItemStack output = result.getOutput(i);
                if (!output.isEmpty()) {
                    int slotIndex = i + 1; // Slots 1, 2, 3
                    ItemStack currentSlot = inventory.getStackInSlot(slotIndex);
                    
                    if (currentSlot.isEmpty()) {
                        inventory.setStackInSlot(slotIndex, output.copy());
                    } else {
                        currentSlot.grow(output.getCount());
                    }
                }
            }
            
            CronicasAetherium.LOGGER.info("Centrífuga separou espíritos na posição {}", getBlockPos());
        }
        
        // Reseta progresso
        processProgress = 0;
        
        // Continua se ainda há trabalho
        if (!canProcess()) {
            stopProcessing();
        }
    }
    
    /**
     * Verifica se um item é uma bolsa de espírito
     */
    private boolean isSpiritBag(ItemStack stack) {
        // TODO: Implementar quando as bolsas de espírito forem definidas
        // Por enquanto, usa items placeholder
        return stack.getItem() == Items.BUNDLE; // Placeholder
    }
    
    /**
     * Obtém o resultado da separação para uma bolsa
     */
    private SeparationResult getSeparationResult(ItemStack input) {
        if (!isSpiritBag(input)) {
            return null;
        }
        
        // Simulação de separação baseada no conteúdo da bolsa
        // TODO: Implementar lógica real baseada no conteúdo das bolsas
        
        // Por enquanto, resultados simulados
        ItemStack pureSpirit = new ItemStack(Items.QUARTZ, 2);   // Espírito Puro (branco)
        ItemStack malignSpirit = new ItemStack(Items.REDSTONE, 1); // Espírito Maligno (vermelho)
        ItemStack arcaneSpirit = new ItemStack(Items.LAPIS_LAZULI, 1); // Espírito Arcano (azul)
        
        return new SeparationResult(pureSpirit, malignSpirit, arcaneSpirit);
    }
    
    /**
     * Dropa o conteúdo da máquina quando removida
     */
    public void dropContents() {
        if (level != null && !level.isClientSide()) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    net.minecraft.world.Containers.dropItemStack(level, getBlockPos().getX(), 
                        getBlockPos().getY(), getBlockPos().getZ(), stack);
                }
            }
        }
    }
    
    // ================================
    // GETTERS PARA GUI E SYNC
    // ================================
    
    public int getEnergyStored() { return energyStorage.getEnergyStored(); }
    public int getMaxEnergy() { return energyStorage.getMaxEnergyStored(); }
    public int getProcessProgress() { 
        if (!isProcessing) return 0;
        return (processProgress * 100) / PROCESSING_TIME; 
    }
    public boolean isProcessing() { return isProcessing; }
    public ItemStackHandler getInventory() { return inventory; }
    public ModEnergyStorage getEnergyStorage() { return energyStorage; }
    
    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            // TODO: Implementar sincronização de dados
        }
    }
    
    // ================================
    // PERSISTÊNCIA NBT
    // ================================
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putInt("progress", processProgress);
        tag.putBoolean("processing", isProcessing);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("energy", energyStorage.serializeNBT(registries));
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        processProgress = tag.getInt("progress");
        isProcessing = tag.getBoolean("processing");
        
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("energy")) {
            energyStorage.deserializeNBT(registries, tag.getCompound("energy"));
        }
    }
    
    /**
     * Classe para representar resultados de separação
     */
    private static class SeparationResult {
        private final ItemStack pureSpirit;
        private final ItemStack malignSpirit;
        private final ItemStack arcaneSpirit;
        
        public SeparationResult(ItemStack pureSpirit, ItemStack malignSpirit, ItemStack arcaneSpirit) {
            this.pureSpirit = pureSpirit;
            this.malignSpirit = malignSpirit;
            this.arcaneSpirit = arcaneSpirit;
        }
        
        public ItemStack getOutput(int index) {
            return switch (index) {
                case 0 -> pureSpirit;
                case 1 -> malignSpirit;
                case 2 -> arcaneSpirit;
                default -> ItemStack.EMPTY;
            };
        }
    }
}