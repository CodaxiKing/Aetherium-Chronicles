package com.cronicasaetherium.mod.world.feature;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

/**
 * Configurações de features para geração de minérios do mod Crônicas de Aetherium
 * 
 * Esta classe define como os minérios customizados do mod são gerados no mundo,
 * incluindo tamanho dos veios, frequência e profundidades de spawn.
 * 
 * Minérios configurados:
 * - Tier 1 Tech: Cobre e Estanho (base da tecnologia a vapor)
 * - Tier 2 Tech: Cobalto (minério raro para Aço Reforçado)
 * - Tier 1 Magic: Fragmentos de Alma (futuro, base da magia)
 * - Tier 2 Magic: Essência Espiritual (futuro, magia avançada)
 * 
 * Cada minério tem configurações específicas para balanceamento:
 * - Tamanho do veio (quantos blocos por veio)
 * - Camadas de spawn (profundidade no mundo)
 * - Blocos de substituição (stone vs deepslate)
 */
public class ModConfiguredFeatures {
    
    // ResourceKeys para identificar cada feature
    public static final ResourceKey<ConfiguredFeature<?, ?>> COPPER_ORE = 
        ResourceKey.create(Registries.CONFIGURED_FEATURE, CronicasAetherium.id("copper_ore"));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE = 
        ResourceKey.create(Registries.CONFIGURED_FEATURE, CronicasAetherium.id("tin_ore"));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> COBALT_ORE = 
        ResourceKey.create(Registries.CONFIGURED_FEATURE, CronicasAetherium.id("cobalt_ore"));
    
    // RuleTests para determinar em quais blocos os minérios podem substituir
    private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    
    /**
     * Método principal de bootstrap que registra todas as configured features
     * 
     * @param context Contexto de bootstrap do DataGen
     */
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        
        // ========= MINÉRIO DE COBRE =========
        // Base do sistema tecnológico Tier 1
        // Spawn: Y 20-60, veios médios, comum
        List<OreConfiguration.TargetBlockState> copperTargets = List.of(
            OreConfiguration.target(STONE_ORE_REPLACEABLES, ModBlocks.COPPER_ORE.get().defaultBlockState()),
            OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_COPPER_ORE.get().defaultBlockState())
        );
        
        context.register(COPPER_ORE, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(copperTargets, 8))); // Veios de 8 blocos
        
        // ========= MINÉRIO DE ESTANHO =========
        // Segundo componente tecnológico Tier 1 (para Bronze)
        // Spawn: Y 10-40, veios menores, menos comum que cobre
        List<OreConfiguration.TargetBlockState> tinTargets = List.of(
            OreConfiguration.target(STONE_ORE_REPLACEABLES, ModBlocks.TIN_ORE.get().defaultBlockState()),
            OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
        );
        
        context.register(TIN_ORE, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(tinTargets, 6))); // Veios de 6 blocos (menor que cobre)
        
        // ========= MINÉRIO DE COBALTO =========
        // Minério raro tecnológico Tier 2 (para Aço Reforçado)
        // Spawn: Y 5-25, veios pequenos, muito raro
        List<OreConfiguration.TargetBlockState> cobaltTargets = List.of(
            OreConfiguration.target(STONE_ORE_REPLACEABLES, ModBlocks.COBALT_ORE.get().defaultBlockState()),
            OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_COBALT_ORE.get().defaultBlockState())
        );
        
        context.register(COBALT_ORE, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(cobaltTargets, 4))); // Veios de apenas 4 blocos (muito raro)
    }
}