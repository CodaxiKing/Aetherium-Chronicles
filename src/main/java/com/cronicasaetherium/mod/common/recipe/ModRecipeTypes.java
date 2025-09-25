package com.cronicasaetherium.mod.common.recipe;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todos os tipos de receita do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todos os RecipeTypes customizados do mod,
 * que são necessários para criar receitas específicas das nossas máquinas.
 * 
 * RecipeTypes registrados:
 * - Crushing: Receitas do Triturador Mecânico
 * - Steam Generation: Receitas do Motor a Vapor
 * - Mana Infusion: Receitas da Infusora de Mana
 * - Alloy Smelting: Receitas da Fundidora de Ligas (futuro)
 * - Crystal Processing: Receitas do Moinho de Cristais (futuro)
 * 
 * Cada RecipeType define um novo sistema de processamento que as máquinas
 * podem usar para determinar quais itens podem ser processados e seus resultados.
 */
public class ModRecipeTypes {
    
    // DeferredRegister para registro eficiente de RecipeTypes
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
        DeferredRegister.create(Registries.RECIPE_TYPE, CronicasAetherium.MODID);
    
    /**
     * Tipo de receita para o Triturador Mecânico
     * Define como minérios são processados para dobrar rendimento
     */
    public static final Supplier<RecipeType<CrushingRecipe>> CRUSHING = 
        RECIPE_TYPES.register("crushing", () -> 
            RecipeType.simple(CronicasAetherium.id("crushing")));
    
    /**
     * Tipo de receita para o Motor a Vapor
     * Define quais combustíveis podem ser usados e sua eficiência
     */
    public static final Supplier<RecipeType<SteamGenerationRecipe>> STEAM_GENERATION = 
        RECIPE_TYPES.register("steam_generation", () -> 
            RecipeType.simple(CronicasAetherium.id("steam_generation")));
    
    /**
     * Tipo de receita para a Infusora de Mana
     * Define como energia tecnológica é convertida em mana
     */
    public static final Supplier<RecipeType<ManaInfusionRecipe>> MANA_INFUSION = 
        RECIPE_TYPES.register("mana_infusion", () -> 
            RecipeType.simple(CronicasAetherium.id("mana_infusion")));
    
    /**
     * Tipo de receita para Fundidora de Ligas (Tier 2)
     * Define como criar ligas avançadas como Aço Reforçado
     */
    public static final Supplier<RecipeType<AlloySmelterRecipe>> ALLOY_SMELTING = 
        RECIPE_TYPES.register("alloy_smelting", () -> 
            RecipeType.simple(CronicasAetherium.id("alloy_smelting")));
    
    /**
     * Tipo de receita para Moinho de Cristais
     * Define como cristais são processados em pós mágicos
     */
    public static final Supplier<RecipeType<CrystalProcessingRecipe>> CRYSTAL_PROCESSING = 
        RECIPE_TYPES.register("crystal_processing", () -> 
            RecipeType.simple(CronicasAetherium.id("crystal_processing")));
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
    }
}