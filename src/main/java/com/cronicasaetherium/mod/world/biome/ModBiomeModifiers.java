package com.cronicasaetherium.mod.world.biome;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.world.feature.ModPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Modificadores de bioma para adicionar geração de minérios do mod Crônicas de Aetherium
 * 
 * Esta classe define em quais biomas os minérios customizados do mod devem ser gerados,
 * usando BiomeModifiers do NeoForge para integrar com o sistema de worldgen do Minecraft.
 * 
 * Biome Modifiers são a forma moderna de adicionar features aos biomas existentes
 * sem substituir ou modificar diretamente os biomas vanilla.
 * 
 * Configurações de spawn:
 * - COPPER & TIN: Todos os biomas do Overworld (comuns)
 * - COBALT: Apenas biomas underground/caves (raro, profundo)
 * 
 * Todos os minérios são adicionados na fase UNDERGROUND_ORES para garantir
 * que sejam gerados na ordem correta com outros minérios do jogo.
 */
public class ModBiomeModifiers {
    
    // ResourceKeys para identificar cada biome modifier
    public static final ResourceKey<BiomeModifier> ADD_COPPER_ORE = 
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            CronicasAetherium.id("add_copper_ore"));
    
    public static final ResourceKey<BiomeModifier> ADD_TIN_ORE = 
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            CronicasAetherium.id("add_tin_ore"));
    
    public static final ResourceKey<BiomeModifier> ADD_COBALT_ORE = 
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, 
            CronicasAetherium.id("add_cobalt_ore"));
    
    /**
     * Método principal de bootstrap que registra todos os biome modifiers
     * 
     * @param context Contexto de bootstrap do DataGen
     */
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        
        // ========= COPPER ORE BIOME MODIFIER =========
        // Adiciona minério de cobre a todos os biomas do Overworld
        // Cobre é comum e deve estar disponível desde o início do jogo
        // Usa a tag IS_OVERWORLD para incluir todos os biomas apropriados
        
        context.register(ADD_COPPER_ORE, 
            new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), // Todos os biomas do Overworld
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.COPPER_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES // Fase de geração de minérios
            ));
        
        // ========= TIN ORE BIOME MODIFIER =========
        // Adiciona minério de estanho a todos os biomas do Overworld
        // Estanho é necessário para fazer Bronze (liga básica Tier 1)
        // Deve estar disponível nos mesmos lugares que o cobre para balanceamento
        
        context.register(ADD_TIN_ORE, 
            new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TIN_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES
            ));
        
        // ========= COBALT ORE BIOME MODIFIER =========
        // Adiciona minério de cobalto a todos os biomas do Overworld
        // Cobalto é raro e usado para Aço Reforçado (Tier 2)
        // Mesmo spawning em todos os biomas, mas com baixa frequência e profundidade extrema
        // O balanceamento é feito via placement (raridade + profundidade), não via biomas
        
        context.register(ADD_COBALT_ORE, 
            new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.COBALT_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES
            ));
    }
}