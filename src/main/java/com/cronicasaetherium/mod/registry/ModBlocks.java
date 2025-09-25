package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.util.valueproviders.UniformInt;
import com.cronicasaetherium.mod.blocks.synergy.ManaInfuserBlock;
import com.cronicasaetherium.mod.blocks.synergy.ArcanePortalBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todos os blocos do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todos os blocos customizados do mod,
 * incluindo minérios, máquinas, blocos decorativos e estruturais.
 * 
 * Organização dos blocos:
 * - Minérios e materiais básicos
 * - Máquinas e componentes tecnológicos
 * - Blocos mágicos e rituais
 * - Blocos decorativos e estruturais
 * - Plantas e vegetação especial
 */
public class ModBlocks {
    
    // DeferredRegister para registro eficiente de blocos
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, CronicasAetherium.MODID);
    
    // ================================
    // MINÉRIOS E MATERIAIS BÁSICOS
    // ================================
    
    // ========= MINÉRIOS TIER 1: TECNOLOGIA ==========
    
    /**
     * Minério de Cobre - Minério básico do sistema tecnológico Tier 1
     * Encontrado em camadas médias (Y 20-60), base da tecnologia a vapor
     * Dropa cobre cru que deve ser fundido para uso
     */
    public static final Supplier<Block> COPPER_ORE = BLOCKS.register("copper_ore",
        () -> new DropExperienceBlock(UniformInt.of(0, 2), BlockBehaviour.Properties.of()
            .strength(3.0f, 3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)));
    
    /**
     * Minério de Cobre Deepslate - Versão mais profunda do minério de cobre
     * Mais raro e difícil de minerar, mas com maior rendimento
     */
    public static final Supplier<Block> DEEPSLATE_COPPER_ORE = BLOCKS.register("deepslate_copper_ore",
        () -> new DropExperienceBlock(UniformInt.of(1, 3), BlockBehaviour.Properties.of()
            .strength(4.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)));
    
    /**
     * Minério de Estanho - Segundo minério do sistema tecnológico Tier 1
     * Mais raro que cobre, essencial para criar bronze
     * Encontrado em veios menores nas profundezas médias (Y 10-40)
     */
    public static final Supplier<Block> TIN_ORE = BLOCKS.register("tin_ore",
        () -> new DropExperienceBlock(UniformInt.of(0, 3), BlockBehaviour.Properties.of()
            .strength(3.5f, 3.5f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)));
    
    /**
     * Minério de Estanho Deepslate - Versão mais profunda do minério de estanho
     */
    public static final Supplier<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
        () -> new DropExperienceBlock(UniformInt.of(1, 4), BlockBehaviour.Properties.of()
            .strength(4.5f, 4.5f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)));
    
    /**
     * Bloco de Bronze - Bloco de armazenamento da primeira liga
     * Usado como componente estrutural e decorativo
     * Base para estruturas de máquinas Tier 1
     */
    public static final Supplier<Block> BRONZE_BLOCK = BLOCKS.register("bronze_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(5.0f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)));
    
    // ========= MINÉRIOS TIER 2: TECNOLOGIA ==========
    
    /**
     * Minério de Cobalto - Minério raro do sistema tecnológico Tier 2
     * Encontrado apenas em veios raros no subsolo profundo (Y 5-25)
     * Necessário para criar Aço Reforçado na Fundidora de Ligas
     */
    public static final Supplier<Block> COBALT_ORE = BLOCKS.register("cobalt_ore",
        () -> new DropExperienceBlock(UniformInt.of(3, 6), BlockBehaviour.Properties.of()
            .strength(4.5f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 2)));
    
    /**
     * Minério de Cobalto Deepslate - Versão deepslate do cobalto
     * Ainda mais raro, encontrado nas camadas mais profundas
     */
    public static final Supplier<Block> DEEPSLATE_COBALT_ORE = BLOCKS.register("deepslate_cobalt_ore",
        () -> new DropExperienceBlock(UniformInt.of(4, 7), BlockBehaviour.Properties.of()
            .strength(5.5f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)
            .lightLevel(state -> 3)));
    
    /**
     * Bloco de Aço Reforçado - Bloco de armazenamento da liga avançada Tier 2
     * Material de construção extremamente resistente
     * Usado nas estruturas de máquinas avançadas
     */
    public static final Supplier<Block> REINFORCED_STEEL_BLOCK = BLOCKS.register("reinforced_steel_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(8.0f, 12.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)));
    
    // ========= MINÉRIOS MÁGICOS TIER 1 ==========
    
    /**
     * Cristal de Fragmentos de Alma - Fonte natural de fragmentos de alma
     * Cresce naturalmente em cavernas escuras e biomas sombrios  
     * Deve ser colhido cuidadosamente para não quebrar
     */
    public static final Supplier<Block> SOUL_FRAGMENT_CRYSTAL = BLOCKS.register("soul_fragment_crystal",
        () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
            .strength(1.5f, 2.0f)
            .sound(SoundType.AMETHYST)
            .lightLevel(state -> 4)
            .noOcclusion()));
    
    /**
     * Minério de Aetherium - Minério principal do mod
     * Encontrado em camadas profundas e estruturas especiais
     * Dropa cristais de Aetherium quando minerado com picareta encantada
     */
    public static final Supplier<Block> AETHERIUM_ORE = BLOCKS.register("aetherium_ore",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(5.0f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)));
    
    /**
     * Minério de Aetherium Profundo - Versão mais rara encontrada nas profundezas
     * Dropa mais cristais e tem chance de dropar fragmentos diretamente
     * Requer ferramentas de diamante ou superiores
     */
    public static final Supplier<Block> DEEPSLATE_AETHERIUM_ORE = BLOCKS.register("deepslate_aetherium_ore",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(6.0f, 7.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)));
    
    /**
     * Bloco de Aetherium - Bloco de armazenamento e decorativo
     * Usado em receitas avançadas e como componente estrutural
     * Emite luz fraca quando ativado por redstone
     */
    public static final Supplier<Block> AETHERIUM_BLOCK = BLOCKS.register("aetherium_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(7.0f, 8.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .lightLevel(state -> 3)));
    
    // ================================
    // MÁQUINAS TECNOLÓGICAS BÁSICAS
    // ================================
    
    // ========= MÁQUINAS TIER 1: TECNOLOGIA ==========
    
    /**
     * Motor a Vapor - Coração do sistema tecnológico Tier 1
     * Queima combustível sólido (carvão, madeira) + água para gerar FE
     * Primeira fonte de energia do jogador
     */
    public static final Supplier<Block> STEAM_ENGINE = BLOCKS.register("steam_engine",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.ANVIL)
            .lightLevel(state -> 5)));
    
    /**
     * Bomba Manual - Sistema básico de transporte de fluidos
     * Puxa água de fontes próximas quando clicada
     * Deve ser conectada a canos de bronze
     */
    public static final Supplier<Block> MANUAL_PUMP = BLOCKS.register("manual_pump",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)));
    
    /**
     * Cano de Bronze - Transporte de fluidos Tier 1
     * Conecta bombas a máquinas que precisam de água
     * Componente essencial da logística de fluidos inicial
     */
    public static final Supplier<Block> BRONZE_PIPE = BLOCKS.register("bronze_pipe",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .sound(SoundType.METAL)
            .noOcclusion()));
    
    /**
     * Triturador Mecânico - Primeira máquina de processamento
     * Transforma 1 minério em 2 pós, duplicando rendimento
     * Funciona com energia do Motor a Vapor
     */
    public static final Supplier<Block> MECHANICAL_CRUSHER = BLOCKS.register("mechanical_crusher",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.ANVIL)));
    
    /**
     * Prensa de Engrenagens - Máquina de componentes
     * Transforma lingotes em engrenagens e placas
     * Componentes essenciais para outras máquinas
     */
    public static final Supplier<Block> GEAR_PRESS = BLOCKS.register("gear_press",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.ANVIL)));
    
    /**
     * Fornalha de Bronze - Fornalha melhorada Tier 1
     * Queima mais rápido e eficientemente que fornalha de pedra
     * Usa combustível de forma mais inteligente
     */
    public static final Supplier<Block> BRONZE_FURNACE = BLOCKS.register("bronze_furnace",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 7)));
    
    /**
     * Duto Pneumático - Primeira automação Tier 1
     * Tubos simples e lentos que movem itens
     * Base da primeira linha de produção automatizada
     */
    public static final Supplier<Block> PNEUMATIC_DUCT = BLOCKS.register("pneumatic_duct",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .sound(SoundType.METAL)
            .noOcclusion()));
    
    // ========= MÁQUINAS TIER 2: TECNOLOGIA ==========
    
    /**
     * Gerador Geotérmico - Fonte de energia Tier 2
     * Deve ser colocado perto de lava para gerar FE passivamente
     * Recompensa exploração de cavernas
     */
    public static final Supplier<Block> GEOTHERMAL_GENERATOR = BLOCKS.register("geothermal_generator",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(5.0f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .lightLevel(state -> 6)));
    
    /**
     * Painel Solar - Energia limpa e passiva Tier 2
     * Funciona apenas durante o dia, menos eficaz na chuva
     * Alternativa sustentável para geração de energia
     */
    public static final Supplier<Block> SOLAR_PANEL = BLOCKS.register("solar_panel",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.GLASS)
            .lightLevel(state -> 2)
            .noOcclusion()));
    
    /**
     * Esteira Transportadora - Automação avançada Tier 2
     * Move itens e mobs lentamente de um ponto a outro
     * Componente básico da logística avançada
     */
    public static final Supplier<Block> CONVEYOR_BELT = BLOCKS.register("conveyor_belt",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .noCollission()));
    
    /**
     * Braço Mecânico - Automação inteligente Tier 2
     * Pega itens de inventários e coloca em outros ou em esteiras
     * Componente chave da automação avançada
     */
    public static final Supplier<Block> MECHANICAL_ARM = BLOCKS.register("mechanical_arm",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .noOcclusion()));
    
    /**
     * Fundidora de Ligas - Máquina avançada Tier 2
     * Combina Ferro + Cobalto + FE para criar Aço Reforçado
     * Porta de entrada para o Tier 2 tecnológico
     */
    public static final Supplier<Block> ALLOY_SMELTER = BLOCKS.register("alloy_smelter",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(6.0f, 8.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .lightLevel(state -> 8)));
    
    /**
     * Alto-Forno Industrial - Estrutura multi-bloco Tier 2
     * Estrutura 3x3x4 que consome muita energia
     * Transforma ferro e pó de carvão em Aço Reforçado
     */
    public static final Supplier<Block> INDUSTRIAL_BLAST_FURNACE = BLOCKS.register("industrial_blast_furnace",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(7.0f, 10.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .lightLevel(state -> 10)));
    
    /**
     * Montador Automatizado - Mesa de trabalho programável Tier 2
     * Fabrica itens em massa seguindo padrões definidos
     * Essencial para produção em larga escala
     */
    public static final Supplier<Block> AUTOMATED_ASSEMBLER = BLOCKS.register("automated_assembler",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(5.0f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .lightLevel(state -> 4)));
    
    /**
     * Forja de Aetherium - Máquina básica para processamento de materiais
     * Primeira máquina que os jogadores podem construir
     * Funciona com energia cinética ou carvão
     */
    public static final Supplier<Block> AETHERIUM_FORGE = BLOCKS.register("aetherium_forge",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.ANVIL)));
    
    /**
     * Moinho de Cristais - Máquina para moer cristais em pó
     * Produz pó de Aetherium usado em receitas alquímicas
     * Funciona apenas com energia rotacional
     */
    public static final Supplier<Block> CRYSTAL_MILL = BLOCKS.register("crystal_mill",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 5.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)));
    
    // ================================
    // BLOCOS MÁGICOS E RITUAIS
    // ================================
    
    // ========= ESTRUTURAS MÁGICAS TIER 1 ==========
    
    /**
     * Pedra Rúnica - "Mesa de trabalho" mágica Tier 1
     * Bloco único que consome Essência Espiritual
     * Realiza transmutações simples com itens jogados sobre ela
     */
    public static final Supplier<Block> RUNIC_STONE = BLOCKS.register("runic_stone",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 6)));
    
    /**
     * Tronco de Salgueiro Torcido - Madeira mágica do bioma sombrio
     * Material base para componentes mágicos Tier 1
     * Cresce apenas nos Bosques Sombrios
     */
    public static final Supplier<Block> TWISTED_WILLOW_LOG = BLOCKS.register("twisted_willow_log",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .sound(SoundType.WOOD)
            .lightLevel(state -> 2)));
    
    /**
     * Madeira de Salgueiro Torcido - Versão processada do tronco
     * Usado em receitas mágicas básicas
     * Retém propriedades mágicas da árvore original
     */
    public static final Supplier<Block> TWISTED_WILLOW_WOOD = BLOCKS.register("twisted_willow_wood",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .sound(SoundType.WOOD)
            .lightLevel(state -> 1)));
    
    /**
     * Folhas de Salgueiro Torcido - Folhas da árvore mágica
     * Podem ser processadas para extrair essências menores
     * Crescem naturalmente nas árvores dos Bosques Sombrios
     */
    public static final Supplier<Block> TWISTED_WILLOW_LEAVES = BLOCKS.register("twisted_willow_leaves",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(0.2f)
            .sound(SoundType.GRASS)
            .noOcclusion()
            .lightLevel(state -> 3)));
    
    // ========= ESTRUTURAS MÁGICAS TIER 2 ==========
    
    /**
     * Altar Arcano - Bloco central do sistema de rituais Tier 2
     * Evolução da Pedra Rúnica para magia avançada
     * Núcleo das estruturas multi-bloco de rituais
     */
    public static final Supplier<Block> ARCANE_ALTAR = BLOCKS.register("arcane_altar",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(4.0f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 10)));
    
    /**
     * Pedestal Rúnico - Componente de rituais Tier 2
     * Deve ser colocado ao redor do Altar Arcano
     * Segura ingredientes durante os rituais mágicos
     */
    public static final Supplier<Block> RUNIC_PEDESTAL = BLOCKS.register("runic_pedestal",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 4)
            .noOcclusion()));
    
    /**
     * Altar de Infusão - Estrutura multi-bloco avançada Tier 2
     * Evolução da Pedra Rúnica com Altar Central + Pedestais
     * Usado para encantamentos permanentes e criação de artefatos
     */
    public static final Supplier<Block> INFUSION_ALTAR = BLOCKS.register("infusion_altar",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(5.0f, 7.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 12)));
    
    /**
     * Pedestal de Salgueiro - Componente do Altar de Infusão
     * Feito com madeira de Salgueiro Torcido
     * Parte essencial da estrutura multi-bloco
     */
    public static final Supplier<Block> WILLOW_PEDESTAL = BLOCKS.register("willow_pedestal",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.WOOD)
            .lightLevel(state -> 5)
            .noOcclusion()));
    
    // ========= PLANTAS E FLORES MÁGICAS ==========
    
    /**
     * Rosa Térmica - Fonte de mana Tier 2 baseada no calor
     * Gera mana rapidamente quando exposta ao calor (fogo, lava)
     * Deve ser cultivada próxima a fontes de calor
     */
    public static final Supplier<Block> THERMAL_ROSE = BLOCKS.register("thermal_rose",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(0.1f)
            .sound(SoundType.GRASS)
            .noCollission()
            .lightLevel(state -> 6)));
    
    /**
     * Cogumelo Lunar - Fonte de mana Tier 2 noturna
     * Só cresce no escuro e gera grande quantidade de mana à noite
     * Alternativa para jogadores que preferem exploração noturna
     */
    public static final Supplier<Block> LUNAR_MUSHROOM = BLOCKS.register("lunar_mushroom",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(0.1f)
            .sound(SoundType.FUNGUS)
            .noCollission()
            .lightLevel(state -> 8)));
    
    /**
     * Piscina de Mana - Armazenamento de energia mágica
     * Deve ser construída próxima aos rituais para fornecer mana
     * Alimentada pelas plantas mágicas e flores
     */
    public static final Supplier<Block> MANA_POOL = BLOCKS.register("mana_pool",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 8)
            .noOcclusion()));
    
    // ========= BLOCOS FUNCIONAIS MÁGICOS ==========
    
    /**
     * Selo de Repulsão - Bloco funcional mágico Tier 2
     * Empurra mobs hostis para longe quando recebe mana
     * Ferramenta de defesa para bases mágicas
     */
    public static final Supplier<Block> REPULSION_SEAL = BLOCKS.register("repulsion_seal",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 5)));
    
    /**
     * Altar Mágico - Bloco central para rituais mágicos
     * Usado para criar feitiços e encantar itens
     * Requer estrutura específica ao redor para funcionar
     */
    public static final Supplier<Block> MAGIC_ALTAR = BLOCKS.register("magic_altar",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 4.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .lightLevel(state -> 7)));
    
    /**
     * Cristal de Mana - Bloco que armazena e transmite energia mágica
     * Cresce naturalmente em áreas com alta concentração mágica
     * Pode ser cultivado pelos jogadores usando sementes especiais
     */
    public static final Supplier<Block> MANA_CRYSTAL = BLOCKS.register("mana_crystal",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(2.0f, 3.0f)
            .sound(SoundType.AMETHYST)
            .lightLevel(state -> 10)
            .noOcclusion()));
    
    // ================================
    // PLANTAS MÁGICAS
    // ================================
    
    /**
     * Flor de Aetherium - Planta básica que gera mana
     * Cresce lentamente e deve ser cuidada adequadamente
     * Base do sistema de magia natural do mod
     */
    public static final Supplier<Block> AETHERIUM_FLOWER = BLOCKS.register("aetherium_flower",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(0.1f)
            .sound(SoundType.GRASS)
            .noCollission()
            .lightLevel(state -> 5)));
    
    // ================================
    // BLOCOS DA DIMENSÃO CRISOL ARCANO
    // ================================
    
    /**
     * Estrutura do Portal Arcano - Bloco que forma a moldura do portal
     * Este bloco é usado para construir a estrutura que será ativada pelo Coração Instável
     * Tem propriedades especiais para indicar sua função como portal dimensional
     */
    public static final Supplier<Block> ARCANE_PORTAL_FRAME = BLOCKS.register("arcane_portal_frame", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(50.0F, 1200.0F) // Resistente como obsidiana
            .sound(SoundType.STONE)
            .lightLevel(state -> 8) // Brilho sutil
            .emissiveRendering((state, level, pos) -> true)));
    
    /**
     * Terra Cristalizada - Bloco base da dimensão Crisol Arcano
     * Substitui a terra comum na nova dimensão, com propriedades cristalinas
     * Pode ser minerada e usada como material de construção especial
     */
    public static final Supplier<Block> CRYSTALLIZED_SOIL = BLOCKS.register("crystallized_soil", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .instrument(NoteBlockInstrument.SNARE)
            .strength(2.0F, 6.0F) // Mais resistente que terra comum
            .sound(SoundType.GRAVEL) // Som cristalino
            .lightLevel(state -> 2))); // Brilho muito suave
    
    /**
     * Minério de Aetherium da Dimensão - Versão exclusiva do Crisol Arcano
     * Contém mais cristais que a versão do mundo normal
     * Só pode ser encontrado na dimensão especial
     */
    public static final Supplier<Block> DIMENSIONAL_AETHERIUM_ORE = BLOCKS.register("dimensional_aetherium_ore", 
        () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_CYAN)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.0F, 3.0F) // Mais fácil de quebrar que obsidiana
            .sound(SoundType.STONE)
            .lightLevel(state -> 12))); // Mais brilhante que o normal
    
    // ================================
    // BLOCOS DE SINERGIA (TECNOLOGIA + MAGIA)
    // ================================
    
    /**
     * Infusora de Mana - Máquina de sinergia Tecnologia → Magia
     * Converte energia tecnológica em mana utilizável pelo sistema mágico
     * Interface entre os dois sistemas de progressão
     */
    public static final Supplier<Block> MANA_INFUSER = BLOCKS.register("mana_infuser", 
        () -> new ManaInfuserBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL)
            .lightLevel(state -> 6)
            .emissiveRendering((state, level, pos) -> true)));
    
    /**
     * Portal Arcano Ativo - Bloco de teleportação para o Crisol Arcano
     * Criado quando a estrutura do portal é ativada com Coração Instável
     * Permite viagem bidirecional entre dimensões
     */
    public static final Supplier<Block> ARCANE_PORTAL = BLOCKS.register("arcane_portal", 
        () -> new ArcanePortalBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(-1.0F, 3600000.0F) // Indestrutível como portal do Nether
            .sound(SoundType.GLASS)
            .lightLevel(state -> 15) // Brilho máximo
            .emissiveRendering((state, level, pos) -> true)
            .noCollission()));
    
    // ================================
    // REGISTRO DE BLOCKITEMS
    // ================================
    
    /**
     * BlockItems - Itens correspondentes aos blocos para o inventário
     * Todos os blocos precisam de um BlockItem para aparecer no inventário
     */
    
    // ========= BLOCKITEMS TIER 1: TECNOLOGIA ==========
    
    public static final Supplier<Item> COPPER_ORE_ITEM = ModItems.ITEMS.register("copper_ore",
        () -> new BlockItem(COPPER_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> DEEPSLATE_COPPER_ORE_ITEM = ModItems.ITEMS.register("deepslate_copper_ore",
        () -> new BlockItem(DEEPSLATE_COPPER_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> TIN_ORE_ITEM = ModItems.ITEMS.register("tin_ore",
        () -> new BlockItem(TIN_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> DEEPSLATE_TIN_ORE_ITEM = ModItems.ITEMS.register("deepslate_tin_ore",
        () -> new BlockItem(DEEPSLATE_TIN_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> BRONZE_BLOCK_ITEM = ModItems.ITEMS.register("bronze_block",
        () -> new BlockItem(BRONZE_BLOCK.get(), new Item.Properties()));
    
    // ========= BLOCKITEMS TIER 2: TECNOLOGIA ==========
    
    public static final Supplier<Item> COBALT_ORE_ITEM = ModItems.ITEMS.register("cobalt_ore",
        () -> new BlockItem(COBALT_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> DEEPSLATE_COBALT_ORE_ITEM = ModItems.ITEMS.register("deepslate_cobalt_ore",
        () -> new BlockItem(DEEPSLATE_COBALT_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> REINFORCED_STEEL_BLOCK_ITEM = ModItems.ITEMS.register("reinforced_steel_block",
        () -> new BlockItem(REINFORCED_STEEL_BLOCK.get(), new Item.Properties()));
    
    // ========= BLOCKITEMS MÁQUINAS TIER 1 ==========
    
    public static final Supplier<Item> STEAM_ENGINE_ITEM = ModItems.ITEMS.register("steam_engine",
        () -> new BlockItem(STEAM_ENGINE.get(), new Item.Properties()));
    
    public static final Supplier<Item> MANUAL_PUMP_ITEM = ModItems.ITEMS.register("manual_pump",
        () -> new BlockItem(MANUAL_PUMP.get(), new Item.Properties()));
    
    public static final Supplier<Item> BRONZE_PIPE_ITEM = ModItems.ITEMS.register("bronze_pipe",
        () -> new BlockItem(BRONZE_PIPE.get(), new Item.Properties()));
    
    public static final Supplier<Item> MECHANICAL_CRUSHER_ITEM = ModItems.ITEMS.register("mechanical_crusher",
        () -> new BlockItem(MECHANICAL_CRUSHER.get(), new Item.Properties()));
    
    public static final Supplier<Item> GEAR_PRESS_ITEM = ModItems.ITEMS.register("gear_press",
        () -> new BlockItem(GEAR_PRESS.get(), new Item.Properties()));
    
    public static final Supplier<Item> BRONZE_FURNACE_ITEM = ModItems.ITEMS.register("bronze_furnace",
        () -> new BlockItem(BRONZE_FURNACE.get(), new Item.Properties()));
    
    public static final Supplier<Item> PNEUMATIC_DUCT_ITEM = ModItems.ITEMS.register("pneumatic_duct",
        () -> new BlockItem(PNEUMATIC_DUCT.get(), new Item.Properties()));
    
    // ========= BLOCKITEMS MÁQUINAS TIER 2 ==========
    
    public static final Supplier<Item> GEOTHERMAL_GENERATOR_ITEM = ModItems.ITEMS.register("geothermal_generator",
        () -> new BlockItem(GEOTHERMAL_GENERATOR.get(), new Item.Properties()));
    
    public static final Supplier<Item> SOLAR_PANEL_ITEM = ModItems.ITEMS.register("solar_panel",
        () -> new BlockItem(SOLAR_PANEL.get(), new Item.Properties()));
    
    public static final Supplier<Item> CONVEYOR_BELT_ITEM = ModItems.ITEMS.register("conveyor_belt",
        () -> new BlockItem(CONVEYOR_BELT.get(), new Item.Properties()));
    
    public static final Supplier<Item> MECHANICAL_ARM_ITEM = ModItems.ITEMS.register("mechanical_arm",
        () -> new BlockItem(MECHANICAL_ARM.get(), new Item.Properties()));
    
    public static final Supplier<Item> ALLOY_SMELTER_ITEM = ModItems.ITEMS.register("alloy_smelter",
        () -> new BlockItem(ALLOY_SMELTER.get(), new Item.Properties()));
    
    public static final Supplier<Item> INDUSTRIAL_BLAST_FURNACE_ITEM = ModItems.ITEMS.register("industrial_blast_furnace",
        () -> new BlockItem(INDUSTRIAL_BLAST_FURNACE.get(), new Item.Properties()));
    
    public static final Supplier<Item> AUTOMATED_ASSEMBLER_ITEM = ModItems.ITEMS.register("automated_assembler",
        () -> new BlockItem(AUTOMATED_ASSEMBLER.get(), new Item.Properties()));
    
    // ========= BLOCKITEMS ESTRUTURAS MÁGICAS ==========
    
    public static final Supplier<Item> SOUL_FRAGMENT_CRYSTAL_ITEM = ModItems.ITEMS.register("soul_fragment_crystal",
        () -> new BlockItem(SOUL_FRAGMENT_CRYSTAL.get(), new Item.Properties()));
    
    public static final Supplier<Item> RUNIC_STONE_ITEM = ModItems.ITEMS.register("runic_stone",
        () -> new BlockItem(RUNIC_STONE.get(), new Item.Properties()));
    
    public static final Supplier<Item> TWISTED_WILLOW_LOG_ITEM = ModItems.ITEMS.register("twisted_willow_log",
        () -> new BlockItem(TWISTED_WILLOW_LOG.get(), new Item.Properties()));
    
    public static final Supplier<Item> TWISTED_WILLOW_WOOD_ITEM = ModItems.ITEMS.register("twisted_willow_wood",
        () -> new BlockItem(TWISTED_WILLOW_WOOD.get(), new Item.Properties()));
    
    public static final Supplier<Item> TWISTED_WILLOW_LEAVES_ITEM = ModItems.ITEMS.register("twisted_willow_leaves",
        () -> new BlockItem(TWISTED_WILLOW_LEAVES.get(), new Item.Properties()));
    
    public static final Supplier<Item> ARCANE_ALTAR_ITEM = ModItems.ITEMS.register("arcane_altar",
        () -> new BlockItem(ARCANE_ALTAR.get(), new Item.Properties()));
    
    public static final Supplier<Item> RUNIC_PEDESTAL_ITEM = ModItems.ITEMS.register("runic_pedestal",
        () -> new BlockItem(RUNIC_PEDESTAL.get(), new Item.Properties()));
    
    public static final Supplier<Item> INFUSION_ALTAR_ITEM = ModItems.ITEMS.register("infusion_altar",
        () -> new BlockItem(INFUSION_ALTAR.get(), new Item.Properties()));
    
    public static final Supplier<Item> WILLOW_PEDESTAL_ITEM = ModItems.ITEMS.register("willow_pedestal",
        () -> new BlockItem(WILLOW_PEDESTAL.get(), new Item.Properties()));
    
    public static final Supplier<Item> THERMAL_ROSE_ITEM = ModItems.ITEMS.register("thermal_rose",
        () -> new BlockItem(THERMAL_ROSE.get(), new Item.Properties()));
    
    public static final Supplier<Item> LUNAR_MUSHROOM_ITEM = ModItems.ITEMS.register("lunar_mushroom",
        () -> new BlockItem(LUNAR_MUSHROOM.get(), new Item.Properties()));
    
    public static final Supplier<Item> MANA_POOL_ITEM = ModItems.ITEMS.register("mana_pool",
        () -> new BlockItem(MANA_POOL.get(), new Item.Properties()));
    
    public static final Supplier<Item> REPULSION_SEAL_ITEM = ModItems.ITEMS.register("repulsion_seal",
        () -> new BlockItem(REPULSION_SEAL.get(), new Item.Properties()));
    
    // ========= BLOCKITEMS ORIGINAIS ==========
    public static final Supplier<Item> AETHERIUM_ORE_ITEM = ModItems.ITEMS.register("aetherium_ore",
        () -> new BlockItem(AETHERIUM_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> DEEPSLATE_AETHERIUM_ORE_ITEM = ModItems.ITEMS.register("deepslate_aetherium_ore",
        () -> new BlockItem(DEEPSLATE_AETHERIUM_ORE.get(), new Item.Properties()));
    
    public static final Supplier<Item> AETHERIUM_BLOCK_ITEM = ModItems.ITEMS.register("aetherium_block",
        () -> new BlockItem(AETHERIUM_BLOCK.get(), new Item.Properties()));
    
    public static final Supplier<Item> AETHERIUM_FORGE_ITEM = ModItems.ITEMS.register("aetherium_forge",
        () -> new BlockItem(AETHERIUM_FORGE.get(), new Item.Properties()));
    
    public static final Supplier<Item> CRYSTAL_MILL_ITEM = ModItems.ITEMS.register("crystal_mill",
        () -> new BlockItem(CRYSTAL_MILL.get(), new Item.Properties()));
    
    public static final Supplier<Item> MAGIC_ALTAR_ITEM = ModItems.ITEMS.register("magic_altar",
        () -> new BlockItem(MAGIC_ALTAR.get(), new Item.Properties()));
    
    public static final Supplier<Item> MANA_CRYSTAL_ITEM = ModItems.ITEMS.register("mana_crystal",
        () -> new BlockItem(MANA_CRYSTAL.get(), new Item.Properties()));
    
    public static final Supplier<Item> AETHERIUM_FLOWER_ITEM = ModItems.ITEMS.register("aetherium_flower",
        () -> new BlockItem(AETHERIUM_FLOWER.get(), new Item.Properties()));
    
    // BlockItems para blocos da dimensão
    public static final Supplier<Item> ARCANE_PORTAL_FRAME_ITEM = ModItems.ITEMS.register("arcane_portal_frame",
        () -> new BlockItem(ARCANE_PORTAL_FRAME.get(), new Item.Properties()));
    
    public static final Supplier<Item> CRYSTALLIZED_SOIL_ITEM = ModItems.ITEMS.register("crystallized_soil",
        () -> new BlockItem(CRYSTALLIZED_SOIL.get(), new Item.Properties()));
    
    public static final Supplier<Item> DIMENSIONAL_AETHERIUM_ORE_ITEM = ModItems.ITEMS.register("dimensional_aetherium_ore",
        () -> new BlockItem(DIMENSIONAL_AETHERIUM_ORE.get(), new Item.Properties()));
    
    // BlockItems para blocos de sinergia
    public static final Supplier<Item> MANA_INFUSER_ITEM = ModItems.ITEMS.register("mana_infuser",
        () -> new BlockItem(MANA_INFUSER.get(), new Item.Properties()));
    
    // Portal arcano não precisa de BlockItem (criado dinamicamente)
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}