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
     * - Cristais e fragmentos de Aetherium
     * - Tomo das Crônicas (guia do mod)
     * - Itens de invocação de chefes
     * - Materiais básicos essenciais
     */
    public static final Supplier<CreativeModeTab> CRONICAS_AETHERIUM_TAB = CREATIVE_MODE_TABS.register("main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.main"))
            .icon(() -> new ItemStack(ModItems.AETHERIUM_CRYSTAL.get()))
            .displayItems((parameters, output) -> {
                // Materiais básicos
                output.accept(ModItems.AETHERIUM_CRYSTAL.get());
                output.accept(ModItems.AETHERIUM_FRAGMENT.get());
                output.accept(ModItems.MAGIC_ESSENCE.get());
                
                // Guia do mod
                output.accept(ModItems.CHRONICLES_TOME.get());
                
                // Itens de invocação
                output.accept(ModItems.ANCIENT_DRAGON_AMULET.get());
                output.accept(ModItems.CORRUPTED_CORE.get());
                output.accept(ModItems.VOID_ORB.get());
                
                // Blocos básicos
                output.accept(ModBlocks.AETHERIUM_ORE_ITEM.get());
                output.accept(ModBlocks.DEEPSLATE_AETHERIUM_ORE_ITEM.get());
                output.accept(ModBlocks.AETHERIUM_BLOCK_ITEM.get());
            })
            .build());
    
    /**
     * Aba de Tecnologia - Sistema Tecnológico
     * Contém máquinas, componentes e itens relacionados à automação
     * 
     * Inclui:
     * - Máquinas básicas e avançadas
     * - Componentes tecnológicos
     * - Sistemas de energia e automação
     */
    public static final Supplier<CreativeModeTab> TECHNOLOGY_TAB = CREATIVE_MODE_TABS.register("technology",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.technology"))
            .icon(() -> new ItemStack(ModItems.AETHERIUM_GEAR.get()))
            .displayItems((parameters, output) -> {
                // Componentes tecnológicos
                output.accept(ModItems.AETHERIUM_GEAR.get());
                output.accept(ModItems.MAGIC_CIRCUIT.get());
                
                // Máquinas
                output.accept(ModBlocks.AETHERIUM_FORGE_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_MILL_ITEM.get());
            })
            .build());
    
    /**
     * Aba de Magia - Sistema Mágico
     * Contém itens mágicos, essências e componentes do sistema de magia
     * 
     * Inclui:
     * - Essências e materiais mágicos
     * - Blocos rituais e mágicos
     * - Plantas e cristais mágicos
     */
    public static final Supplier<CreativeModeTab> MAGIC_TAB = CREATIVE_MODE_TABS.register("magic",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cronicasaetherium.magic"))
            .icon(() -> new ItemStack(ModItems.MAGIC_ESSENCE.get()))
            .displayItems((parameters, output) -> {
                // Materiais mágicos
                output.accept(ModItems.MAGIC_ESSENCE.get());
                
                // Blocos mágicos
                output.accept(ModBlocks.MAGIC_ALTAR_ITEM.get());
                output.accept(ModBlocks.MANA_CRYSTAL_ITEM.get());
                output.accept(ModBlocks.AETHERIUM_FLOWER_ITEM.get());
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