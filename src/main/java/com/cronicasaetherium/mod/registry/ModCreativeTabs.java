package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para as abas do modo criativo do mod Crônicas de Aetherium
 * 
 * Esta classe define as abas personalizadas que aparecem no modo criativo,
 * organizando os itens do mod de forma lógica para facilitar o acesso.
 * 
 * Organização das abas:
 * - Aba Principal: Itens básicos e materiais fundamentais
 * - Aba Tecnologia: Máquinas, componentes e sistemas tecnológicos
 * - Aba Magia: Itens mágicos, essências e artefatos
 * - Aba Blocos: Todos os blocos decorativos e funcionais
 */
public class ModCreativeTabs {
    
    // DeferredRegister para registro eficiente das abas criativas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CronicasAetherium.MODID);
    
    /**
     * Aba Principal do Mod - Crônicas de Aetherium
     * Contém os itens mais importantes e básicos do mod
     * 
     * Inclui:
     * - Materiais básicos Tier 1 e 2
     * - Guias e livros do mod
     * - Itens de invocação de chefes
     * - Materiais básicos essenciais
     */
    public static final Supplier<CreativeModeTab> CRONICAS_AETHERIUM_TAB = CREATIVE_MODE_TABS.register("main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.main"))
            .icon(() -> new ItemStack(ModItems.AETHERIUM_CRYSTAL.get()))
            .displayItems((parameters, output) -> {
                // TIER 1: Materiais Tecnológicos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.RAW_COPPER.get());
                // output.accept(ModItems.COPPER_INGOT.get());
                // output.accept(ModItems.RAW_TIN.get());
                // output.accept(ModItems.TIN_INGOT.get());
                // output.accept(ModItems.BRONZE_INGOT.get());
                // output.accept(ModItems.COPPER_DUST.get());
                // output.accept(ModItems.TIN_DUST.get());
                
                // TIER 1: Materiais Mágicos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.SOUL_FRAGMENT.get());
                // output.accept(ModItems.SPIRIT_ESSENCE.get());
                
                // TIER 2: Materiais Avançados - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.RAW_COBALT.get());
                // output.accept(ModItems.COBALT_INGOT.get());
                // output.accept(ModItems.REINFORCED_STEEL_INGOT.get());
                // output.accept(ModItems.ADAMANTINE_HEART.get());
                
                // TIER 2: Materiais Mágicos Avançados - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.MALIGNANT_SPIRIT.get());
                // output.accept(ModItems.PURE_SPIRIT.get());
                // output.accept(ModItems.ARCANE_SPIRIT.get());
                // output.accept(ModItems.CONCENTRATED_MAGIC_ESSENCE.get());
                
                // Materiais Aetherium Originais
                output.accept(ModItems.AETHERIUM_CRYSTAL.get());
                output.accept(ModItems.AETHERIUM_FRAGMENT.get());
                output.accept(ModItems.MAGIC_ESSENCE.get());
                
                // Guias e Livros
                output.accept(ModItems.CHRONICLES_TOME.get());
                // output.accept(ModItems.HIDDEN_CODEX.get()); // TEMPORARIAMENTE COMENTADO
                output.accept(ModItems.WORN_JOURNAL.get());
                
                // Itens de invocação de chefes
                output.accept(ModItems.ANCIENT_DRAGON_AMULET.get());
                output.accept(ModItems.CORRUPTED_CORE.get());
                output.accept(ModItems.VOID_ORB.get());
                
                // Dimensão
                output.accept(ModItems.UNSTABLE_HEART.get());
                output.accept(ModBlocks.ARCANE_PORTAL_FRAME_ITEM.get());
            })
            .build());
    
    /**
     * Aba de Tecnologia - Sistema Tecnológico
     * Contém máquinas, componentes e itens relacionados à automação
     * 
     * Inclui:
     * - TIER 1: Tecnologia a vapor e máquinas básicas
     * - TIER 2: Geração avançada de energia e automação
     * - Componentes e circuitos de aprimoramento
     */
    public static final Supplier<CreativeModeTab> TECHNOLOGY_TAB = CREATIVE_MODE_TABS.register("technology",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.technology"))
            .icon(() -> new ItemStack(ModItems.AETHERIUM_GEAR.get()))
            .displayItems((parameters, output) -> {
                // TIER 1: Ferramentas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.CALIBRATION_WRENCH.get());
                
                // TIER 1: Componentes Básicos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.BRONZE_GEAR.get());
                // output.accept(ModItems.BRONZE_PLATE.get());
                
                // TIER 1: Energia e Fluidos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.STEAM_ENGINE_ITEM.get());
                // output.accept(ModBlocks.MANUAL_PUMP_ITEM.get());
                // output.accept(ModBlocks.BRONZE_PIPE_ITEM.get());
                
                // TIER 1: Máquinas Básicas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.MECHANICAL_CRUSHER_ITEM.get());
                // output.accept(ModBlocks.GEAR_PRESS_ITEM.get());
                // output.accept(ModBlocks.BRONZE_FURNACE_ITEM.get());
                
                // TIER 1: Automação Inicial - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.PNEUMATIC_DUCT_ITEM.get());
                
                // TIER 2: Fontes de Energia Avançadas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.GEOTHERMAL_GENERATOR_ITEM.get());
                // output.accept(ModBlocks.SOLAR_PANEL_ITEM.get());
                
                // TIER 2: Automação Avançada - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.CONVEYOR_BELT_ITEM.get());
                // output.accept(ModBlocks.MECHANICAL_ARM_ITEM.get());
                
                // TIER 2: Máquinas Avançadas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.ALLOY_SMELTER_ITEM.get());
                // output.accept(ModBlocks.INDUSTRIAL_BLAST_FURNACE_ITEM.get());
                // output.accept(ModBlocks.AUTOMATED_ASSEMBLER_ITEM.get());
                
                // TIER 2: Componentes Avançados - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.REINFORCED_STEEL_GEAR.get());
                // output.accept(ModItems.SPEED_UPGRADE_CIRCUIT.get());
                // output.accept(ModItems.EFFICIENCY_UPGRADE_CIRCUIT.get());
                // output.accept(ModItems.FORTUNE_UPGRADE_CIRCUIT.get());
                
                // Componentes Originais
                output.accept(ModItems.AETHERIUM_GEAR.get());
                output.accept(ModItems.MAGIC_CIRCUIT.get());
                output.accept(ModBlocks.AETHERIUM_FORGE_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_MILL_ITEM.get());
                
                // Sinergia
                output.accept(ModBlocks.MANA_INFUSER_ITEM.get());
            })
            .build());
    
    /**
     * Aba de Magia - Sistema Mágico
     * Contém itens mágicos, essências e componentes do sistema de magia
     * 
     * Inclui:
     * - TIER 1: Magia básica e primeiros amuletos
     * - TIER 2: Sistema de rituais e magia avançada
     * - Plantas mágicas e fontes de mana
     */
    public static final Supplier<CreativeModeTab> MAGIC_TAB = CREATIVE_MODE_TABS.register("magic",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.magic"))
            .icon(() -> new ItemStack(ModItems.MAGIC_ESSENCE.get()))
            .displayItems((parameters, output) -> {
                // TIER 1: Ferramentas Mágicas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.SACRIFICE_KNIFE.get());
                // output.accept(ModItems.FOREST_WAND.get());
                
                // TIER 1: Amuletos e Anéis - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.PENUMBRA_AMULET.get());
                // output.accept(ModItems.HUNTER_RING.get());
                
                // TIER 1: Estruturas Básicas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.RUNIC_STONE_ITEM.get());
                
                // TIER 2: Amuletos Avançados - TEMPORARIAMENTE COMENTADO
                // output.accept(ModItems.REGENERATION_RING.get());
                // output.accept(ModItems.ANIMIC_CORE.get());
                // output.accept(ModItems.SPIRIT_POUCH.get());
                
                // TIER 2: Estruturas de Rituais - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.ARCANE_ALTAR_ITEM.get());
                // output.accept(ModBlocks.RUNIC_PEDESTAL_ITEM.get());
                // output.accept(ModBlocks.INFUSION_ALTAR_ITEM.get());
                // output.accept(ModBlocks.WILLOW_PEDESTAL_ITEM.get());
                
                // Plantas e Flores Mágicas - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.THERMAL_ROSE_ITEM.get());
                // output.accept(ModBlocks.LUNAR_MUSHROOM_ITEM.get());
                // output.accept(ModBlocks.MANA_POOL_ITEM.get());
                
                // Blocos Funcionais - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.REPULSION_SEAL_ITEM.get());
                
                // Madeira Mágica - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.TWISTED_WILLOW_LOG_ITEM.get());
                // output.accept(ModBlocks.TWISTED_WILLOW_WOOD_ITEM.get());
                // output.accept(ModBlocks.TWISTED_WILLOW_LEAVES_ITEM.get());
                
                // Materiais Mágicos Originais
                output.accept(ModItems.MAGIC_ESSENCE.get());
                output.accept(ModItems.RUNE_OF_EFFICIENCY.get());
                output.accept(ModBlocks.MAGIC_ALTAR_ITEM.get());
                output.accept(ModBlocks.MANA_CRYSTAL_ITEM.get());
                output.accept(ModBlocks.AETHERIUM_FLOWER_ITEM.get());
            })
            .build());
    
    /**
     * Aba de Minérios e Blocos - Recursos Naturais
     * Contém todos os minérios, blocos de armazenamento e blocos da dimensão
     * 
     * Inclui:
     * - Minérios Tier 1 e 2
     * - Blocos de armazenamento
     * - Blocos da dimensão especial
     */
    public static final Supplier<CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.blocks"))
            .icon(() -> new ItemStack(ModBlocks.AETHERIUM_ORE_ITEM.get()))
            .displayItems((parameters, output) -> {
                // TIER 1: Minérios Tecnológicos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.COPPER_ORE_ITEM.get());
                // output.accept(ModBlocks.DEEPSLATE_COPPER_ORE_ITEM.get());
                // output.accept(ModBlocks.TIN_ORE_ITEM.get());
                // output.accept(ModBlocks.DEEPSLATE_TIN_ORE_ITEM.get());
                // output.accept(ModBlocks.BRONZE_BLOCK_ITEM.get());
                
                // TIER 2: Minérios Avançados - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.COBALT_ORE_ITEM.get());
                // output.accept(ModBlocks.DEEPSLATE_COBALT_ORE_ITEM.get());
                // output.accept(ModBlocks.REINFORCED_STEEL_BLOCK_ITEM.get());
                
                // Minérios e Blocos Mágicos - TEMPORARIAMENTE COMENTADO
                // output.accept(ModBlocks.SOUL_FRAGMENT_CRYSTAL_ITEM.get());
                
                // Minérios e Blocos Aetherium Originais
                output.accept(ModBlocks.AETHERIUM_ORE_ITEM.get());
                output.accept(ModBlocks.DEEPSLATE_AETHERIUM_ORE_ITEM.get());
                output.accept(ModBlocks.AETHERIUM_BLOCK_ITEM.get());
                
                // Blocos da Dimensão
                output.accept(ModBlocks.CRYSTALLIZED_SOIL_ITEM.get());
                output.accept(ModBlocks.DIMENSIONAL_AETHERIUM_ORE_ITEM.get());
            })
            .build());
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}