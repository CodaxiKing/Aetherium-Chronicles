package com.cronicasaetherium.mod.common.capability;

import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * Implementação customizada de armazenamento de energia para as máquinas do mod
 * 
 * Esta classe extends EnergyStorage do NeoForge para fornecer funcionalidades
 * específicas das nossas máquinas, incluindo callbacks de mudança de estado.
 * 
 * Funcionalidades:
 * - Armazenamento configurável de Forge Energy (FE)
 * - Callbacks quando energia é adicionada/removida
 * - Controle de taxa de entrada/saída
 * - Compatibilidade completa com outros mods de energia
 */
public class ModEnergyStorage extends EnergyStorage {
    
    private final Runnable onEnergyChanged;
    
    /**
     * Construtor para máquinas que apenas consomem energia (como Crusher)
     * 
     * @param capacity Capacidade máxima de energia
     * @param maxReceive Taxa máxima de recepção por tick
     * @param onEnergyChanged Callback executado quando energia muda
     */
    public ModEnergyStorage(int capacity, int maxReceive, Runnable onEnergyChanged) {
        super(capacity, maxReceive, 0); // Apenas recebe energia
        this.onEnergyChanged = onEnergyChanged;
    }
    
    /**
     * Construtor para máquinas que produzem energia (como Steam Engine)
     * 
     * @param capacity Capacidade máxima de energia
     * @param maxReceive Taxa máxima de recepção por tick
     * @param maxExtract Taxa máxima de extração por tick
     * @param onEnergyChanged Callback executado quando energia muda
     */
    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onEnergyChanged) {
        super(capacity, maxReceive, maxExtract);
        this.onEnergyChanged = onEnergyChanged;
    }
    
    /**
     * Construtor para máquinas com energia inicial
     * 
     * @param capacity Capacidade máxima de energia
     * @param maxReceive Taxa máxima de recepção por tick
     * @param maxExtract Taxa máxima de extração por tick
     * @param energy Energia inicial
     * @param onEnergyChanged Callback executado quando energia muda
     */
    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Runnable onEnergyChanged) {
        super(capacity, maxReceive, maxExtract, energy);
        this.onEnergyChanged = onEnergyChanged;
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && received > 0 && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        return received;
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && extracted > 0 && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        return extracted;
    }
    
    /**
     * Força a mudança da quantidade de energia (para uso interno das máquinas)
     * 
     * @param energy Nova quantidade de energia
     */
    public void setEnergyStored(int energy) {
        this.energy = Math.max(0, Math.min(energy, capacity));
        if (onEnergyChanged != null) {
            onEnergyChanged.run();
        }
    }
    
    /**
     * Adiciona energia diretamente (para geradores internos)
     * 
     * @param energy Quantidade de energia a adicionar
     * @return Quantidade efetivamente adicionada
     */
    public int addEnergyDirect(int energy) {
        int oldEnergy = this.energy;
        this.energy = Math.min(capacity, this.energy + energy);
        int added = this.energy - oldEnergy;
        
        if (added > 0 && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        
        return added;
    }
    
    /**
     * Remove energia diretamente (para consumo interno)
     * 
     * @param energy Quantidade de energia a remover
     * @return Quantidade efetivamente removida
     */
    public int removeEnergyDirect(int energy) {
        int oldEnergy = this.energy;
        this.energy = Math.max(0, this.energy - energy);
        int removed = oldEnergy - this.energy;
        
        if (removed > 0 && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        
        return removed;
    }
    
    /**
     * Verifica se pode fornecer energia
     * 
     * @param amount Quantidade desejada
     * @return true se pode fornecer
     */
    public boolean canExtract(int amount) {
        return maxExtract > 0 && energy >= amount;
    }
    
    /**
     * Verifica se pode receber energia
     * 
     * @param amount Quantidade desejada
     * @return true se pode receber
     */
    public boolean canReceive(int amount) {
        return maxReceive > 0 && (energy + amount) <= capacity;
    }
    
    /**
     * Obtém o nível de energia como porcentagem
     * 
     * @return Nível de energia (0-100)
     */
    public int getEnergyPercentage() {
        if (capacity == 0) return 0;
        return (energy * 100) / capacity;
    }
    
    /**
     * Verifica se está completamente cheio
     * 
     * @return true se cheio
     */
    public boolean isFull() {
        return energy >= capacity;
    }
    
    /**
     * Verifica se está completamente vazio
     * 
     * @return true se vazio
     */
    public boolean isEmpty() {
        return energy <= 0;
    }
}