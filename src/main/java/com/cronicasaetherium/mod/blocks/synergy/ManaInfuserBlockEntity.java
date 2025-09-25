package com.cronicasaetherium.mod.blocks.synergy;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * BlockEntity da Infusora de Mana - Lógica de processamento da máquina
 * 
 * Esta classe implementa toda a lógica interna da Infusora de Mana,
 * incluindo armazenamento de energia, processamento de itens e geração de mana.
 * 
 * Sistemas integrados:
 * - Armazenamento de Forge Energy para energia de entrada
 * - Inventário para catalisadores (entrada) e produtos (saída)  
 * - Sistema de receitas para conversão de energia em mana
 * - Sincronização client-server para atualizações de GUI
 * - Persistência de dados via NBT
 * 
 * Esta BlockEntity serve como exemplo de como integrar sistemas tecnológicos
 * e mágicos de forma que ambos os tipos de jogador possam se beneficiar.
 */
public class ManaInfuserBlockEntity extends BlockEntity {
    
    // Constantes de configuração da máquina
    private static final int MAX_ENERGY = 10000; // Capacidade máxima de energia
    private static final int ENERGY_PER_OPERATION = 100; // Energia consumida por operação
    private static final int PROCESSING_TIME = 100; // Ticks para completar uma operação
    
    // Estado interno da máquina
    private int energyStored = 0; // Energia atualmente armazenada
    private int processProgress = 0; // Progresso da operação atual (0-100)
    private boolean isProcessing = false; // Se a máquina está processando
    
    // TODO: Adicionar quando o sistema de inventário for implementado
    // private final ItemStackHandler inventory = new ItemStackHandler(3) {
    //     @Override
    //     protected void onContentsChanged(int slot) {
    //         setChanged(); // Marca a BlockEntity como modificada
    //         sync(); // Sincroniza com o cliente
    //     }
    // };
    
    /**
     * Construtor da BlockEntity
     * 
     * @param type Tipo da BlockEntity (registrado no ModBlockEntities)
     * @param pos Posição no mundo
     * @param state Estado do bloco
     */
    public ManaInfuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Construtor simplificado usando o tipo registrado
     * 
     * @param pos Posição no mundo
     * @param state Estado do bloco
     */
    public ManaInfuserBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.MANA_INFUSER.get(), pos, state);
    }
    
    /**
     * Método principal de processamento - executado a cada tick
     * 
     * Este método gerencia toda a lógica da máquina:
     * - Verifica se há energia suficiente
     * - Processa catalisadores se houver
     * - Atualiza progresso e estado
     * - Consome energia conforme necessário
     */
    public void tick() {
        if (level == null || level.isClientSide()) {
            return; // Processa apenas no servidor
        }
        
        boolean wasProcessing = isProcessing;
        
        // Verifica se pode iniciar ou continuar processamento
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
        
        // TODO: Implementar lógica de geração de Mana quando o sistema for adicionado
        // if (processProgress >= PROCESSING_TIME) {
        //     generateMana();
        //     completeProcessing();
        // }
    }
    
    /**
     * Verifica se a máquina pode processar
     * 
     * @return true se pode processar, false caso contrário
     */
    private boolean canProcess() {
        // Verifica energia suficiente
        if (energyStored < ENERGY_PER_OPERATION) {
            return false;
        }
        
        // TODO: Verificar se há catalisadores no slot de entrada
        // TODO: Verificar se há espaço no slot de saída
        
        return true; // Temporariamente sempre true para testes
    }
    
    /**
     * Inicia um novo processo de conversão
     */
    private void startProcessing() {
        isProcessing = true;
        processProgress = 0;
        
        CronicasAetherium.LOGGER.debug("Infusora de Mana iniciou processamento na posição {}", getBlockPos());
    }
    
    /**
     * Continua o processo atual
     */
    private void continueProcessing() {
        processProgress++;
        
        // Consome energia gradualmente durante o processo
        if (processProgress % 10 == 0) { // A cada 10 ticks
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
            
            CronicasAetherium.LOGGER.debug("Infusora de Mana parou processamento na posição {}", getBlockPos());
        }
    }
    
    /**
     * Completa um ciclo de processamento
     */
    private void completeProcessing() {
        // TODO: Implementar geração de mana quando o sistema for adicionado
        CronicasAetherium.LOGGER.info("Infusora de Mana completou um ciclo na posição {}", getBlockPos());
        
        // Reseta para o próximo ciclo
        processProgress = 0;
        
        // Continua processando se ainda há recursos
        if (!canProcess()) {
            stopProcessing();
        }
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
    
    /**
     * Obtém a energia atualmente armazenada
     * 
     * @return Energia armazenada
     */
    public int getEnergyStored() {
        return energyStored;
    }
    
    /**
     * Obtém a capacidade máxima de energia
     * 
     * @return Capacidade máxima
     */
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
    
    /**
     * Obtém o progresso atual do processamento (0-100)
     * 
     * @return Progresso em porcentagem
     */
    public int getProcessProgress() {
        if (!isProcessing) return 0;
        return (processProgress * 100) / PROCESSING_TIME;
    }
    
    /**
     * Verifica se a máquina está processando
     * 
     * @return true se está processando
     */
    public boolean isProcessing() {
        return isProcessing;
    }
    
    /**
     * Sincroniza dados com o cliente para atualização de GUI
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
    
    /**
     * Salva os dados da BlockEntity para NBT
     * 
     * @param tag Tag para salvar os dados
     * @param registries Lookup para registros
     */
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putInt("energy", energyStored);
        tag.putInt("progress", processProgress);
        tag.putBoolean("processing", isProcessing);
        
        // TODO: Salvar inventário quando implementado
        // tag.put("inventory", inventory.serializeNBT(registries));
    }
    
    /**
     * Carrega os dados da BlockEntity do NBT
     * 
     * @param tag Tag com os dados salvos
     * @param registries Lookup para registros
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        energyStored = tag.getInt("energy");
        processProgress = tag.getInt("progress");
        isProcessing = tag.getBoolean("processing");
        
        // TODO: Carregar inventário quando implementado
        // if (tag.contains("inventory")) {
        //     inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        // }
    }
}