package com.cronicasaetherium.mod.world.feature;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

/**
 * Configurações de placement para geração de minérios do mod Crônicas de Aetherium
 * 
 * Esta classe define onde e com que frequência os minérios customizados do mod
 * são colocados no mundo, incluindo raridade, distribuição e filtros de bioma.
 * 
 * Placed Features são o que o jogo usa para determinar onde spawnar as
 * Configured Features no mundo durante a geração de chunks.
 * 
 * Configurações de balanceamento:
 * - COPPER: Comum, 12 tentativas por chunk, Y 20-60
 * - TIN: Menos comum, 8 tentativas por chunk, Y 10-40  
 * - COBALT: Raro, 4 tentativas por chunk, Y 5-25
 * 
 * Todos os minérios usam filtros para garantir distribuição equilibrada.
 */
public class ModPlacedFeatures {
    
    // ResourceKeys para identificar cada placed feature
    public static final ResourceKey<PlacedFeature> COPPER_ORE_PLACED = 
        ResourceKey.create(Registries.PLACED_FEATURE, CronicasAetherium.id("copper_ore_placed"));
    
    public static final ResourceKey<PlacedFeature> TIN_ORE_PLACED = 
        ResourceKey.create(Registries.PLACED_FEATURE, CronicasAetherium.id("tin_ore_placed"));
    
    public static final ResourceKey<PlacedFeature> COBALT_ORE_PLACED = 
        ResourceKey.create(Registries.PLACED_FEATURE, CronicasAetherium.id("cobalt_ore_placed"));
    
    /**
     * Método principal de bootstrap que registra todas as placed features
     * 
     * @param context Contexto de bootstrap do DataGen
     */
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // ========= COPPER ORE PLACEMENT =========
        // Minério comum do Tier 1, base da tecnologia a vapor
        // Frequência: Alta (12 tentativas por chunk)
        // Profundidade: Y 20-60 (camadas médias)
        // Raridade: Sem filtro de raridade (comum)
        
        Holder<ConfiguredFeature<?, ?>> copperOreFeature = 
            configuredFeatures.getOrThrow(ModConfiguredFeatures.COPPER_ORE);
        
        context.register(COPPER_ORE_PLACED, new PlacedFeature(copperOreFeature,
            List.of(
                CountPlacement.of(12), // 12 tentativas de spawn por chunk
                InSquarePlacement.spread(), // Distribui aleatoriamente no chunk
                HeightRangePlacement.uniform( // Distribuição uniforme entre Y levels
                    VerticalAnchor.absolute(20), // Altura mínima: Y 20
                    VerticalAnchor.absolute(60)  // Altura máxima: Y 60
                ),
                BiomeFilter.biome() // Aplica apenas em biomas apropriados
            )));
        
        // ========= TIN ORE PLACEMENT =========
        // Segundo componente do Tier 1, necessário para Bronze
        // Frequência: Média (8 tentativas por chunk)
        // Profundidade: Y 10-40 (mais profundo que cobre)
        // Raridade: Sem filtro (comum o suficiente para progressão)
        
        Holder<ConfiguredFeature<?, ?>> tinOreFeature = 
            configuredFeatures.getOrThrow(ModConfiguredFeatures.TIN_ORE);
        
        context.register(TIN_ORE_PLACED, new PlacedFeature(tinOreFeature,
            List.of(
                CountPlacement.of(8), // 8 tentativas de spawn por chunk
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(10), // Altura mínima: Y 10 (mais profundo)
                    VerticalAnchor.absolute(40)  // Altura máxima: Y 40
                ),
                BiomeFilter.biome()
            )));
        
        // ========= COBALT ORE PLACEMENT =========
        // Minério raro do Tier 2, necessário para Aço Reforçado
        // Frequência: Baixa (4 tentativas por chunk)
        // Profundidade: Y 5-25 (profundidades extremas)
        // Raridade: Filtro de raridade 3 (apenas 1/3 das tentativas succedem)
        
        Holder<ConfiguredFeature<?, ?>> cobaltOreFeature = 
            configuredFeatures.getOrThrow(ModConfiguredFeatures.COBALT_ORE);
        
        context.register(COBALT_ORE_PLACED, new PlacedFeature(cobaltOreFeature,
            List.of(
                RarityFilter.onAverageOnceEvery(3), // 1/3 chance de tentar spawnar
                CountPlacement.of(4), // 4 tentativas de spawn por chunk
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(5),  // Altura mínima: Y 5 (muito profundo)
                    VerticalAnchor.absolute(25)  // Altura máxima: Y 25
                ),
                BiomeFilter.biome()
            )));
    }
    
    /**
     * Método de conveniência para criar placement modifiers comuns
     * 
     * @param count Número de tentativas por chunk
     * @param minY Altura mínima
     * @param maxY Altura máxima
     * @return Lista de placement modifiers
     */
    private static List<PlacementModifier> commonOrePlacement(int count, int minY, int maxY) {
        return List.of(
            CountPlacement.of(count),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(minY),
                VerticalAnchor.absolute(maxY)
            ),
            BiomeFilter.biome()
        );
    }
    
    /**
     * Método para placement de minérios raros com filtro de raridade
     * 
     * @param count Número de tentativas por chunk
     * @param rarity Raridade (1 = comum, 10 = muito raro)
     * @param minY Altura mínima
     * @param maxY Altura máxima
     * @return Lista de placement modifiers com raridade
     */
    private static List<PlacementModifier> rareOrePlacement(int count, int rarity, int minY, int maxY) {
        return List.of(
            RarityFilter.onAverageOnceEvery(rarity),
            CountPlacement.of(count),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(minY),
                VerticalAnchor.absolute(maxY)
            ),
            BiomeFilter.biome()
        );
    }
}