package com.cronicasaetherium.mod.world.biome;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;

/**
 * Registro e configuração dos biomas customizados do mod
 * 
 * Esta classe define os biomas únicos encontrados nas dimensões do mod,
 * incluindo suas características ambientais, efeitos especiais e spawns.
 * 
 * Biomas implementados:
 * - Estepes Cristalinas: Bioma principal do Crisol Arcano
 */
public class ModBiomes {
    
    /**
     * Estepes Cristalinas - Bioma principal da dimensão Crisol Arcano
     * 
     * Um bioma desolado coberto por cristais e terra cristalizada.
     * Características:
     * - Terreno predominantemente plano com formações cristalinas
     * - Partículas ambientais de energia mágica
     * - Minérios de Aetherium abundantes
     * - Clima frio e seco
     * - Efeitos de brilho noturno
     */
    public static final ResourceKey<Biome> CRYSTALLINE_WASTES = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "crystalline_wastes"));
    
    /**
     * Constrói e configura o bioma das Estepes Cristalinas
     * 
     * Este método define todas as propriedades do bioma, incluindo:
     * - Efeitos climáticos e ambientais
     * - Cores do céu, água e folhagem
     * - Música e sons ambientais
     * - Spawns de mobs (se houver)
     * - Features de geração de mundo
     * 
     * @param context Contexto de bootstrap para acessar registros
     * @return Biome configurado das Estepes Cristalinas
     */
    public static Biome crystallineWastes(BootstrapContext<Biome> context) {
        // Configuração dos efeitos especiais do bioma
        BiomeSpecialEffects.Builder effectsBuilder = new BiomeSpecialEffects.Builder()
            // Cor do céu - tom azulado cristalino
            .skyColor(0x87CEEB) // SkyBlue
            // Cor da névoa - tom roxo mágico
            .fogColor(0x9370DB) // MediumPurple
            // Cor da água - azul cristalino brilhante
            .waterColor(0x00CED1) // DarkTurquoise
            // Cor da névoa sobre a água
            .waterFogColor(0x4682B4) // SteelBlue
            // Cor da grama - tons cristalizados
            .grassColorOverride(0xB0E0E6) // PowderBlue
            // Cor das folhas - similar à grama
            .foliageColorOverride(0xAFEEEE) // PaleTurquoise
            // Música ambiente específica do bioma
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_DISC_CHIRP))
            // Som ambiente - vento suave
            .ambientLoopSound(SoundEvents.AMBIENT_CAVE)
            // Partículas ambientais serão adicionadas futuramente
            // .ambientParticle(new AmbientParticleSettings(...))
            ;
        
        // Configuração das propriedades climáticas
        Biome.ClimateSettings climate = new Biome.ClimateSettings.Builder()
            .precipitation(Biome.Precipitation.NONE) // Sem chuva
            .temperature(0.1F) // Frio
            .temperatureModifier(Biome.TemperatureModifier.NONE)
            .downfall(0.0F) // Seco
            .build();
        
        // Configuração dos spawns de mobs
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        
        // TODO: Adicionar spawns de mobs customizados quando implementados
        // spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(...));
        
        // Configuração das features de geração
        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE), 
            context.lookup(Registries.CONFIGURED_CARVER)
        );
        
        // Adiciona features básicas de cavernas e minérios
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generationBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generationBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationBuilder);
        BiomeDefaultFeatures.addDefaultSprings(generationBuilder);
        
        // TODO: Adicionar features customizadas como:
        // - Formações de cristais
        // - Minérios de Aetherium dimensional
        // - Estruturas antigas
        
        // Constrói e retorna o bioma final
        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(0.1F)
            .downfall(0.0F)
            .specialEffects(effectsBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(generationBuilder.build())
            .build();
    }
    
    /**
     * Método de bootstrap para registrar todos os biomas do mod
     * 
     * Este método é chamado durante a geração de dados para registrar
     * todos os biomas customizados com suas configurações.
     * 
     * @param context Contexto de bootstrap
     */
    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(CRYSTALLINE_WASTES, crystallineWastes(context));
        
        CronicasAetherium.LOGGER.info("Biomas customizados registrados:");
        CronicasAetherium.LOGGER.info("- Estepes Cristalinas: {}", CRYSTALLINE_WASTES.location());
    }
}