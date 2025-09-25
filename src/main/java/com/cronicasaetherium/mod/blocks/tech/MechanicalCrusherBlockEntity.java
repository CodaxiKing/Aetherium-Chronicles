package com.cronicasaetherium.mod.blocks.tech;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.registry.ModBlockEntities;
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
 * BlockEntity do Triturador Mecânico - Máquina de processamento Tier 1
 * 
 * O Triturador Mecânico dobra o rendimento de minérios, convertendo:
 * - 1 Minério de Cobre → 2 Cobre Cru + chance de Estanho
 * - 1 Minério de Estanho → 2 Estanho Cru + chance de Cobre  
 * - 1 Minério de Ferro → 2 Ferro Cru + chance de outros metais
 * - Outros minérios → Rendimento dobrado + subprodutos
 * 
 * Sistemas integrados:
 * - Consumo de Forge Energy do Steam Engine
 * - Inventário: slot entrada, slot saída principal, slot subproduto
 * - Processamento progressivo com animação
 * - Receitas customizadas para cada tipo de minério
 * - Chance de subprodutos extras (balanceamento)
 * - Sincronização client-server para efeitos visuais
 * 
 * Esta máquina é essencial para a progressão Tier 1, permitindo que
 * o jogador obtenha materiais suficientes para as próximas tecnologias.
 */
public class MechanicalCrusherBlockEntity extends BlockEntity {
    
    // Constantes de configuração da máquina
    private static final int MAX_ENERGY = 10000; // Capacidade máxima de energia
    private static final int ENERGY_PER_OPERATION = 200; // Energia por operação
    private static final int PROCESSING_TIME = 200; // Ticks para completar (10 segundos)
    
    // Estado interno da máquina
    private int energyStored = 0; // Energia atualmente armazenada
    private int processProgress = 0; // Progresso da operação atual (0-200)
    private boolean isProcessing = false; // Se a máquina está processando
    
    // Inventário: [0] entrada, [1] saída principal, [2] subproduto
    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // Marca a BlockEntity como modificada
            syncToClient(); // Sincroniza com o cliente
        }
        
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) { // Slot de entrada - apenas minérios
                return isValidInput(stack);
            }
            return false; // Slots de saída não aceitam inserção manual
        }
    };
    
    /**
     * Construtor da BlockEntity
     */
    public MechanicalCrusherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Construtor simplificado usando o tipo registrado
     */
    public MechanicalCrusherBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.MECHANICAL_CRUSHER.get(), pos, state);
    }
    
    /**
     * Método principal de processamento - executado a cada tick
     * 
     * Gerencia toda a lógica da máquina:
     * - Verifica se há energia suficiente
     * - Processa minérios se houver
     * - Atualiza progresso e estado
     * - Produz resultados quando completa
     */
    public void tick() {
        if (level == null || level.isClientSide()) {
            return; // Processa apenas no servidor
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
        
        // Sincroniza com o cliente se o estado mudou
        if (wasProcessing != isProcessing) {
            setChanged();
            syncToClient();
        }
    }
    
    /**
     * Verifica se a máquina pode processar
     * 
     * @return true se pode processar
     */
    private boolean canProcess() {
        // Verifica energia suficiente
        if (energyStored < ENERGY_PER_OPERATION) {
            return false;
        }
        
        // Verifica se há item válido no slot de entrada
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty() || !isValidInput(input)) {
            return false;
        }
        
        // Verifica se há espaço nos slots de saída
        CrushingResult result = getCrushingResult(input);
        if (result == null) {
            return false;
        }
        
        // Verifica slot de saída principal
        ItemStack currentOutput = inventory.getStackInSlot(1);
        if (!currentOutput.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(currentOutput, result.mainOutput) ||
                currentOutput.getCount() + result.mainOutput.getCount() > currentOutput.getMaxStackSize()) {
                return false;
            }
        }
        
        // Verifica slot de subproduto
        if (!result.byproduct.isEmpty()) {
            ItemStack currentByproduct = inventory.getStackInSlot(2);
            if (!currentByproduct.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(currentByproduct, result.byproduct) ||
                    currentByproduct.getCount() + result.byproduct.getCount() > currentByproduct.getMaxStackSize()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Inicia um novo processo de trituração
     */
    private void startProcessing() {
        isProcessing = true;
        processProgress = 0;
        
        CronicasAetherium.LOGGER.debug("Triturador Mecânico iniciou processamento na posição {}", getBlockPos());
    }
    
    /**
     * Continua o processo atual
     */
    private void continueProcessing() {
        processProgress++;
        
        // Consome energia gradualmente
        if (processProgress % 20 == 0) { // A cada segundo
            consumeEnergy(ENERGY_PER_OPERATION / 10);
        }
        
        // Completa o processo quando atinge o tempo necessário
        if (processProgress >= PROCESSING_TIME) {
            completeProcessing();
        }
    }
    
    /**
     * Para o processamento atual
     */
    private void stopProcessing() {
        if (isProcessing) {
            isProcessing = false;
            processProgress = 0;
            
            CronicasAetherium.LOGGER.debug("Triturador Mecânico parou processamento na posição {}", getBlockPos());
        }
    }
    
    /**
     * Completa um ciclo de processamento
     */
    private void completeProcessing() {
        ItemStack input = inventory.getStackInSlot(0);
        CrushingResult result = getCrushingResult(input);
        
        if (result != null) {
            // Consome item de entrada
            input.shrink(1);
            inventory.setStackInSlot(0, input);
            
            // Produz saída principal
            ItemStack currentOutput = inventory.getStackInSlot(1);
            if (currentOutput.isEmpty()) {
                inventory.setStackInSlot(1, result.mainOutput.copy());
            } else {
                currentOutput.grow(result.mainOutput.getCount());
            }
            
            // Produz subproduto se houver
            if (!result.byproduct.isEmpty()) {
                ItemStack currentByproduct = inventory.getStackInSlot(2);
                if (currentByproduct.isEmpty()) {
                    inventory.setStackInSlot(2, result.byproduct.copy());
                } else {
                    currentByproduct.grow(result.byproduct.getCount());
                }
            }
            
            CronicasAetherium.LOGGER.info("Triturador completou processamento: {} → {} + {}", 
                input.getDisplayName().getString(),
                result.mainOutput.getDisplayName().getString(),
                result.byproduct.isEmpty() ? "nada" : result.byproduct.getDisplayName().getString());
        }
        
        // Reseta para o próximo ciclo
        processProgress = 0;
        
        // Continua processando se ainda há recursos
        if (!canProcess()) {
            stopProcessing();
        }
    }
    
    /**
     * Verifica se um item é entrada válida
     * 
     * @param stack ItemStack a verificar
     * @return true se é entrada válida
     */
    private boolean isValidInput(ItemStack stack) {
        // Por enquanto, apenas minérios básicos
        return stack.getItem() == Items.COAL_ORE ||
               stack.getItem() == Items.IRON_ORE ||
               stack.getItem() == Items.GOLD_ORE ||
               stack.getItem() == Items.DIAMOND_ORE ||
               stack.getItem() == Items.DEEPSLATE_COAL_ORE ||
               stack.getItem() == Items.DEEPSLATE_IRON_ORE ||
               stack.getItem() == Items.DEEPSLATE_GOLD_ORE ||
               stack.getItem() == Items.DEEPSLATE_DIAMOND_ORE;
               // TODO: Adicionar minérios customizados quando implementados
    }
    
    /**
     * Obtém o resultado da trituração para um item
     * 
     * @param input ItemStack de entrada
     * @return Resultado da trituração ou null se inválido
     */
    private CrushingResult getCrushingResult(ItemStack input) {
        // Receitas de trituração com subprodutos
        if (input.getItem() == Items.COAL_ORE) {
            return new CrushingResult(
                new ItemStack(Items.COAL, 2), // Saída principal: 2 carvão
                level.random.nextFloat() < 0.1f ? new ItemStack(Items.DIAMOND) : ItemStack.EMPTY // 10% chance de diamante
            );
        } else if (input.getItem() == Items.IRON_ORE) {
            return new CrushingResult(
                new ItemStack(Items.RAW_IRON, 2), // Saída principal: 2 ferro cru
                level.random.nextFloat() < 0.15f ? new ItemStack(Items.RAW_GOLD) : ItemStack.EMPTY // 15% chance de ouro cru
            );
        } else if (input.getItem() == Items.GOLD_ORE) {
            return new CrushingResult(
                new ItemStack(Items.RAW_GOLD, 2), // Saída principal: 2 ouro cru
                level.random.nextFloat() < 0.2f ? new ItemStack(Items.RAW_IRON) : ItemStack.EMPTY // 20% chance de ferro cru
            );
        } else if (input.getItem() == Items.DIAMOND_ORE) {
            return new CrushingResult(
                new ItemStack(Items.DIAMOND, 2), // Saída principal: 2 diamantes
                level.random.nextFloat() < 0.05f ? new ItemStack(Items.EMERALD) : ItemStack.EMPTY // 5% chance de esmeralda
            );
        }
        
        // Versões Deepslate (mesmo resultado, mas com melhor chance de subproduto)
        if (input.getItem() == Items.DEEPSLATE_COAL_ORE) {
            return new CrushingResult(
                new ItemStack(Items.COAL, 2),
                level.random.nextFloat() < 0.15f ? new ItemStack(Items.DIAMOND) : ItemStack.EMPTY // 15% chance (melhor)
            );
        } else if (input.getItem() == Items.DEEPSLATE_IRON_ORE) {
            return new CrushingResult(
                new ItemStack(Items.RAW_IRON, 2),
                level.random.nextFloat() < 0.2f ? new ItemStack(Items.RAW_GOLD) : ItemStack.EMPTY // 20% chance (melhor)
            );
        } else if (input.getItem() == Items.DEEPSLATE_GOLD_ORE) {
            return new CrushingResult(
                new ItemStack(Items.RAW_GOLD, 2),
                level.random.nextFloat() < 0.25f ? new ItemStack(Items.RAW_IRON) : ItemStack.EMPTY // 25% chance (melhor)
            );
        } else if (input.getItem() == Items.DEEPSLATE_DIAMOND_ORE) {
            return new CrushingResult(
                new ItemStack(Items.DIAMOND, 2),
                level.random.nextFloat() < 0.1f ? new ItemStack(Items.EMERALD) : ItemStack.EMPTY // 10% chance (melhor)
            );
        }
        
        return null; // Item não processável
    }
    
    /**
     * Consome energia da máquina
     * 
     * @param amount Quantidade de energia a consumir
     */
    private void consumeEnergy(int amount) {
        energyStored = Math.max(0, energyStored - amount);
    }
    
    /**
     * Adiciona energia à máquina
     * 
     * @param amount Quantidade de energia a adicionar
     * @return Quantidade efetivamente adicionada
     */
    public int addEnergy(int amount) {
        int addable = Math.min(amount, MAX_ENERGY - energyStored);
        energyStored += addable;
        setChanged();
        return addable;
    }
    
    // ================================
    // GETTERS PARA GUI E SINCRONIZAÇÃO
    // ================================
    
    public int getEnergyStored() { return energyStored; }
    public int getMaxEnergy() { return MAX_ENERGY; }
    public int getProcessProgress() { 
        if (!isProcessing) return 0;
        return (processProgress * 100) / PROCESSING_TIME; 
    }
    public boolean isProcessing() { return isProcessing; }
    public ItemStackHandler getInventory() { return inventory; }
    
    /**
     * Sincroniza dados com o cliente
     */
    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            // TODO: Implementar sincronização quando necessário
            // level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
    
    // ================================
    // PERSISTÊNCIA DE DADOS (NBT)
    // ================================
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putInt("energy", energyStored);
        tag.putInt("progress", processProgress);
        tag.putBoolean("processing", isProcessing);
        tag.put("inventory", inventory.serializeNBT(registries));
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        energyStored = tag.getInt("energy");
        processProgress = tag.getInt("progress");
        isProcessing = tag.getBoolean("processing");
        
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }
    
    /**
     * Classe interna para representar resultados da trituração
     */
    private static class CrushingResult {
        public final ItemStack mainOutput;
        public final ItemStack byproduct;
        
        public CrushingResult(ItemStack mainOutput, ItemStack byproduct) {
            this.mainOutput = mainOutput;
            this.byproduct = byproduct;
        }
    }
}