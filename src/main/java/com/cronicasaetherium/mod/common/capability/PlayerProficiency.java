package com.cronicasaetherium.mod.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Capacidade do jogador para armazenar dados de proficiência
 * 
 * Esta classe gerencia os pontos de experiência de diferentes proficiências
 * que o jogador pode desenvolver ao longo do mod. Cada proficiência representa
 * um aspecto diferente do conhecimento: engenharia (tecnologia) e arcana (magia).
 * 
 * Os dados persistem através de morte, viagens dimensionais e logout/login.
 * 
 * Proficiências disponíveis:
 * - Engenharia: Experiência com máquinas, automação e tecnologia
 * - Arcana: Experiência com magia, rituais e conhecimento místico
 */
public class PlayerProficiency implements INBTSerializable<CompoundTag> {
    
    // Constantes para as chaves de NBT
    private static final String ENGINEERING_XP_KEY = "engineering_xp";
    private static final String ARCANA_XP_KEY = "arcana_xp";
    
    // Constantes para valores mínimos e máximos
    private static final int MIN_XP = 0;
    private static final int MAX_XP = Integer.MAX_VALUE;
    
    // Armazenamento dos pontos de experiência
    private int engineeringXp;
    private int arcanaXp;
    
    /**
     * Construtor padrão
     * Inicializa ambas as proficiências com 0 pontos de experiência
     */
    public PlayerProficiency() {
        this.engineeringXp = 0;
        this.arcanaXp = 0;
    }
    
    // ================================
    // MÉTODOS DE ACESSO À ENGENHARIA
    // ================================
    
    /**
     * Obtém os pontos de experiência de engenharia do jogador
     * 
     * @return Quantidade atual de XP de engenharia
     */
    public int getEngineeringXp() {
        return this.engineeringXp;
    }
    
    /**
     * Define os pontos de experiência de engenharia do jogador
     * 
     * @param xp Nova quantidade de XP (será limitada entre MIN_XP e MAX_XP)
     */
    public void setEngineeringXp(int xp) {
        this.engineeringXp = Math.max(MIN_XP, Math.min(MAX_XP, xp));
    }
    
    /**
     * Adiciona pontos de experiência de engenharia
     * 
     * @param amount Quantidade de XP a ser adicionada (pode ser negativa para remover)
     * @return Quantidade final de XP de engenharia após a adição
     */
    public int addEngineeringXp(int amount) {
        this.setEngineeringXp(this.engineeringXp + amount);
        return this.engineeringXp;
    }
    
    // ================================
    // MÉTODOS DE ACESSO À ARCANA
    // ================================
    
    /**
     * Obtém os pontos de experiência arcana do jogador
     * 
     * @return Quantidade atual de XP arcana
     */
    public int getArcanaXp() {
        return this.arcanaXp;
    }
    
    /**
     * Define os pontos de experiência arcana do jogador
     * 
     * @param xp Nova quantidade de XP (será limitada entre MIN_XP e MAX_XP)
     */
    public void setArcanaXp(int xp) {
        this.arcanaXp = Math.max(MIN_XP, Math.min(MAX_XP, xp));
    }
    
    /**
     * Adiciona pontos de experiência arcana
     * 
     * @param amount Quantidade de XP a ser adicionada (pode ser negativa para remover)
     * @return Quantidade final de XP arcana após a adição
     */
    public int addArcanaXp(int amount) {
        this.setArcanaXp(this.arcanaXp + amount);
        return this.arcanaXp;
    }
    
    // ================================
    // MÉTODOS UTILITÁRIOS
    // ================================
    
    /**
     * Obtém XP por nome do tipo de proficiência
     * 
     * @param proficiencyType Tipo de proficiência ("engenharia" ou "arcana")
     * @return Quantidade de XP da proficiência especificada, ou 0 se o tipo for inválido
     */
    public int getXpByType(String proficiencyType) {
        return switch (proficiencyType.toLowerCase()) {
            case "engenharia", "engineering" -> this.engineeringXp;
            case "arcana", "arcane" -> this.arcanaXp;
            default -> 0;
        };
    }
    
    /**
     * Adiciona XP por nome do tipo de proficiência
     * 
     * @param proficiencyType Tipo de proficiência ("engenharia" ou "arcana")
     * @param amount Quantidade de XP a ser adicionada
     * @return true se a operação foi bem-sucedida, false se o tipo for inválido
     */
    public boolean addXpByType(String proficiencyType, int amount) {
        return switch (proficiencyType.toLowerCase()) {
            case "engenharia", "engineering" -> {
                this.addEngineeringXp(amount);
                yield true;
            }
            case "arcana", "arcane" -> {
                this.addArcanaXp(amount);
                yield true;
            }
            default -> false;
        };
    }
    
    /**
     * Calcula o nível aproximado com base no XP
     * Fórmula: sqrt(xp / 100) para crescimento não-linear
     * 
     * @param xp Pontos de experiência
     * @return Nível calculado (mínimo 1)
     */
    public int calculateLevel(int xp) {
        return Math.max(1, (int) Math.sqrt(xp / 100.0));
    }
    
    /**
     * Obtém o nível de engenharia do jogador
     * 
     * @return Nível atual de engenharia
     */
    public int getEngineeringLevel() {
        return calculateLevel(this.engineeringXp);
    }
    
    /**
     * Obtém o nível arcana do jogador
     * 
     * @return Nível atual arcana
     */
    public int getArcanaLevel() {
        return calculateLevel(this.arcanaXp);
    }
    
    /**
     * Reseta todas as proficiências para 0
     * Usado principalmente para testes ou eventos especiais
     */
    public void resetAll() {
        this.engineeringXp = 0;
        this.arcanaXp = 0;
    }
    
    // ================================
    // SERIALIZAÇÃO NBT
    // ================================
    
    /**
     * Serializa os dados da proficiência para NBT
     * Permite que os dados persistam entre sessões e viagens dimensionais
     * 
     * @param provider Provider para registros de holder
     * @return CompoundTag contendo todos os dados serializados
     */
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(ENGINEERING_XP_KEY, this.engineeringXp);
        tag.putInt(ARCANA_XP_KEY, this.arcanaXp);
        return tag;
    }
    
    /**
     * Deserializa os dados da proficiência do NBT
     * Carrega os dados salvos quando o jogador entra no mundo
     * 
     * @param provider Provider para registros de holder
     * @param nbt CompoundTag contendo os dados salvos
     */
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.engineeringXp = nbt.getInt(ENGINEERING_XP_KEY);
        this.arcanaXp = nbt.getInt(ARCANA_XP_KEY);
    }
    
    /**
     * Método toString para depuração
     * 
     * @return Representação em string dos dados de proficiência
     */
    @Override
    public String toString() {
        return String.format("PlayerProficiency{engineering=%d (lvl %d), arcana=%d (lvl %d)}", 
            this.engineeringXp, this.getEngineeringLevel(),
            this.arcanaXp, this.getArcanaLevel());
    }
}