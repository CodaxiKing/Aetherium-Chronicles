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