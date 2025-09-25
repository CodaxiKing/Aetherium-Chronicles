package com.cronicasaetherium.mod.world.dimension;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * Registro e gerenciamento das dimensões customizadas do mod
 * 
 * Esta classe define as dimensões especiais do Crônicas de Aetherium,
 * incluindo suas propriedades, chaves de acesso e configurações especiais.
 * 
 * Dimensões implementadas:
 * - Crisol Arcano: Dimensão mágica com cristais e energia arcana
 * 
 * As dimensões são registradas através de arquivos de data generation
 * em resources/data/cronicasaetherium/dimension/
 */
public class ModDimensions {
    
    /**
     * Crisol Arcano - Dimensão mágica principal do mod
     * 
     * Uma dimensão instável cheia de energia arcana cristalizada.
     * Características:
     * - Ambiente cristalino com partículas mágicas
     * - Minérios de Aetherium mais abundantes
     * - Estruturas antigas de civilizações perdidas
     * - Biomas únicos com propriedades mágicas especiais
     * 
     * Acesso: Portal ativado com Coração Instável
     */
    public static final ResourceKey<Level> ARCANE_CRUCIBLE = ResourceKey.create(Registries.DIMENSION,
        ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "arcane_crucible"));
    
    /**
     * Tipo de dimensão para o Crisol Arcano
     * Define as propriedades físicas da dimensão como altura, efeitos ambientais, etc.
     */
    public static final ResourceKey<DimensionType> ARCANE_CRUCIBLE_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
        ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "arcane_crucible"));
    
    /**
     * Método de inicialização das dimensões
     * 
     * Este método registra as configurações necessárias para as dimensões customizadas.
     * É chamado durante a inicialização do mod para garantir que todas as dimensões
     * estejam disponíveis quando o mundo for carregado.
     * 
     * Nota: As dimensões em si são registradas através de arquivos JSON
     * na pasta de data generation, este método apenas inicializa referências.
     */
    public static void initialize() {
        CronicasAetherium.LOGGER.info("Inicializando dimensões customizadas:");
        CronicasAetherium.LOGGER.info("- Crisol Arcano: {}", ARCANE_CRUCIBLE.location());
        
        // TODO: Adicionar validações de configuração se necessário
        // TODO: Registrar event handlers para entrada/saída da dimensão
    }
    
    /**
     * Verifica se um nível corresponde a uma das dimensões do mod
     * 
     * @param level Nível a verificar
     * @return true se for uma dimensão do mod, false caso contrário
     */
    public static boolean isModDimension(Level level) {
        ResourceKey<Level> dimensionKey = level.dimension();
        return ARCANE_CRUCIBLE.equals(dimensionKey);
    }
    
    /**
     * Verifica se um nível é especificamente o Crisol Arcano
     * 
     * @param level Nível a verificar
     * @return true se for o Crisol Arcano, false caso contrário
     */
    public static boolean isArcaneCrucible(Level level) {
        return ARCANE_CRUCIBLE.equals(level.dimension());
    }
}