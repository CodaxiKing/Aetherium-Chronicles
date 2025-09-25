package com.cronicasaetherium.mod.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Sistema de configuração do mod Crônicas de Aetherium
 * 
 * Esta classe define todas as opções configuráveis do mod, permitindo que
 * administradores de servidor e jogadores personalizem a experiência de
 * acordo com suas preferências.
 * 
 * Categorias de configuração:
 * - Proficiência: Multiplicadores de XP e velocidade de progressão
 * - Dimensões: Habilitação e configurações de dimensões customizadas  
 * - Geração: Frequência de minérios, estruturas e spawns
 * - Sinergia: Configurações dos sistemas de integração
 * - Performance: Otimizações e limites de recursos
 * 
 * As configurações são aplicadas no lado do servidor e sincronizadas
 * automaticamente com clientes conectados quando necessário.
 */
public class ModConfig {
    
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    
    // ================================
    // CONFIGURAÇÕES DE PROFICIÊNCIA
    // ================================
    
    /**
     * Multiplicador global para ganho de XP de proficiência
     * 
     * Controla quão rapidamente os jogadores ganham experiência nas
     * proficiências de engenharia e arcana. Valores maiores aceleram
     * a progressão, valores menores a tornam mais desafiadora.
     * 
     * Padrão: 1.0 (velocidade normal)
     * Faixa: 0.1 - 5.0
     */
    public static final ModConfigSpec.DoubleValue PROFICIENCY_XP_MULTIPLIER;
    
    /**
     * XP base concedido por atividades de engenharia
     * 
     * Define quanto XP de engenharia é ganho por ações como:
     * - Criar máquinas
     * - Processar materiais
     * - Automatizar sistemas
     * 
     * Padrão: 10
     * Faixa: 1 - 100
     */
    public static final ModConfigSpec.IntValue BASE_ENGINEERING_XP;
    
    /**
     * XP base concedido por atividades arcanas
     * 
     * Define quanto XP arcana é ganho por ações como:
     * - Realizar rituais
     * - Cultivar plantas mágicas
     * - Criar itens encantados
     * 
     * Padrão: 15
     * Faixa: 1 - 100
     */
    public static final ModConfigSpec.IntValue BASE_ARCANA_XP;
    
    // ================================
    // CONFIGURAÇÕES DE DIMENSÕES
    // ================================
    
    /**
     * Habilita ou desabilita a dimensão do Crisol Arcano
     * 
     * Se desabilitada, o portal não pode ser criado e jogadores
     * já na dimensão são teleportados de volta ao Overworld.
     * Útil para servidores que querem focar apenas no conteúdo
     * do mundo normal.
     * 
     * Padrão: true (habilitada)
     */
    public static final ModConfigSpec.BooleanValue IS_ARCANE_CRUCIBLE_ENABLED;
    
    /**
     * Tempo de cooldown do portal em segundos
     * 
     * Previne uso excessivo do portal, criando um período
     * de espera entre viagens. Valor 0 desabilita o cooldown.
     * 
     * Padrão: 30 segundos
     * Faixa: 0 - 600 (10 minutos)
     */
    public static final ModConfigSpec.IntValue PORTAL_COOLDOWN_SECONDS;
    
    // ================================
    // CONFIGURAÇÕES DE GERAÇÃO
    // ================================
    
    /**
     * Chance de spawn do Minério de Aetherium por chunk
     * 
     * Controla a frequência com que minérios de Aetherium são
     * gerados durante a criação de chunks. Valores maiores
     * tornam o minério mais comum, valores menores mais raro.
     * 
     * Padrão: 0.15 (15% de chance por chunk)
     * Faixa: 0.01 - 1.0
     */
    public static final ModConfigSpec.DoubleValue AETHERIUM_ORE_SPAWN_CHANCE;
    
    /**
     * Quantidade máxima de veios de minério por chunk
     * 
     * Define quantos veios separados de Aetherium podem
     * aparecer em um único chunk. Veios menores e mais
     * distribuídos vs. veios maiores e concentrados.
     * 
     * Padrão: 3
     * Faixa: 1 - 8
     */
    public static final ModConfigSpec.IntValue MAX_AETHERIUM_VEINS_PER_CHUNK;
    
    /**
     * Tamanho médio dos veios de minério
     * 
     * Controla quantos blocos de minério aparecem em cada veio.
     * Valores maiores criam depósitos mais abundantes.
     * 
     * Padrão: 6
     * Faixa: 2 - 15
     */
    public static final ModConfigSpec.IntValue AETHERIUM_VEIN_SIZE;
    
    // ================================
    // CONFIGURAÇÕES DE SINERGIA
    // ================================
    
    /**
     * Eficiência base das Runas de Eficiência
     * 
     * Define o bônus percentual base que as runas concedem
     * às máquinas. Jogadores com maior proficiência arcana
     * recebem bônus adicionais sobre este valor.
     * 
     * Padrão: 25% 
     * Faixa: 10 - 100
     */
    public static final ModConfigSpec.IntValue RUNE_BASE_EFFICIENCY_BONUS;
    
    /**
     * Multiplicador de conversão de energia para mana
     * 
     * Controla quanta mana é gerada por unidade de energia
     * na Infusora de Mana. Valores maiores tornam a conversão
     * mais eficiente, incentivando sinergia tecnologia→magia.
     * 
     * Padrão: 1.0
     * Faixa: 0.5 - 3.0
     */
    public static final ModConfigSpec.DoubleValue ENERGY_TO_MANA_MULTIPLIER;
    
    // ================================
    // CONFIGURAÇÕES DE PERFORMANCE
    // ================================
    
    /**
     * Limite máximo de partículas por área
     * 
     * Previne lag causado por excesso de efeitos visuais
     * em áreas densas. Partículas são priorizadas por
     * importância quando o limite é atingido.
     * 
     * Padrão: 100
     * Faixa: 20 - 500
     */
    public static final ModConfigSpec.IntValue MAX_PARTICLES_PER_AREA;
    
    /**
     * Intervalo de atualização de máquinas em ticks
     * 
     * Define com que frequência as máquinas processam
     * operações. Valores maiores reduzem carga do servidor
     * mas tornam as máquinas mais lentas.
     * 
     * Padrão: 20 ticks (1 segundo)
     * Faixa: 1 - 100
     */
    public static final ModConfigSpec.IntValue MACHINE_UPDATE_INTERVAL;
    
    // Inicialização estática das configurações
    static {
        BUILDER.comment("Configurações de Proficiência")
               .comment("Controla o sistema de habilidades e progressão do jogador")
               .push("proficiency");
        
        PROFICIENCY_XP_MULTIPLIER = BUILDER
            .comment("Multiplicador global para ganho de XP de proficiência")
            .comment("Valores maiores aceleram a progressão")
            .defineInRange("proficiencyXpMultiplier", 1.0, 0.1, 5.0);
        
        BASE_ENGINEERING_XP = BUILDER
            .comment("XP base concedido por atividades de engenharia")
            .defineInRange("baseEngineeringXp", 10, 1, 100);
        
        BASE_ARCANA_XP = BUILDER
            .comment("XP base concedido por atividades arcanas")
            .defineInRange("baseArcanaXp", 15, 1, 100);
        
        BUILDER.pop();
        
        BUILDER.comment("Configurações de Dimensões")
               .comment("Controla as dimensões customizadas do mod")
               .push("dimensions");
        
        IS_ARCANE_CRUCIBLE_ENABLED = BUILDER
            .comment("Habilita ou desabilita a dimensão do Crisol Arcano")
            .define("isArcaneCrucibleEnabled", true);
        
        PORTAL_COOLDOWN_SECONDS = BUILDER
            .comment("Tempo de cooldown do portal em segundos")
            .defineInRange("portalCooldownSeconds", 30, 0, 600);
        
        BUILDER.pop();
        
        BUILDER.comment("Configurações de Geração")
               .comment("Controla spawn de minérios, estruturas e recursos")
               .push("generation");
        
        AETHERIUM_ORE_SPAWN_CHANCE = BUILDER
            .comment("Chance de spawn do Minério de Aetherium por chunk")
            .defineInRange("aetheriumOreSpawnChance", 0.15, 0.01, 1.0);
        
        MAX_AETHERIUM_VEINS_PER_CHUNK = BUILDER
            .comment("Quantidade máxima de veios de minério por chunk")
            .defineInRange("maxAetheriumVeinsPerChunk", 3, 1, 8);
        
        AETHERIUM_VEIN_SIZE = BUILDER
            .comment("Tamanho médio dos veios de minério")
            .defineInRange("aetheriumVeinSize", 6, 2, 15);
        
        BUILDER.pop();
        
        BUILDER.comment("Configurações de Sinergia")
               .comment("Controla a integração entre sistemas tecnológicos e mágicos")
               .push("synergy");
        
        RUNE_BASE_EFFICIENCY_BONUS = BUILDER
            .comment("Eficiência base das Runas de Eficiência em porcentagem")
            .defineInRange("runeBaseEfficiencyBonus", 25, 10, 100);
        
        ENERGY_TO_MANA_MULTIPLIER = BUILDER
            .comment("Multiplicador de conversão de energia para mana")
            .defineInRange("energyToManaMultiplier", 1.0, 0.5, 3.0);
        
        BUILDER.pop();
        
        BUILDER.comment("Configurações de Performance")
               .comment("Otimizações e limites para melhor desempenho")
               .push("performance");
        
        MAX_PARTICLES_PER_AREA = BUILDER
            .comment("Limite máximo de partículas por área")
            .defineInRange("maxParticlesPerArea", 100, 20, 500);
        
        MACHINE_UPDATE_INTERVAL = BUILDER
            .comment("Intervalo de atualização de máquinas em ticks")
            .defineInRange("machineUpdateInterval", 20, 1, 100);
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
}