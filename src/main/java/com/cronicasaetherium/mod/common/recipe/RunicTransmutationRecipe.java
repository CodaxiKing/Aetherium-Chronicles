package com.cronicasaetherium.mod.common.recipe;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * Receita de Transmutação na Pedra Rúnica - Sistema mágico Tier 1
 * 
 * Este tipo de receita permite que a Pedra Rúnica realize transmutações mágicas
 * convertendo itens mundanos em versões mágicas consumindo Essência Espiritual.
 * 
 * Funcionalidades:
 * - Item de entrada (ex: Tijolo comum)
 * - Custo de Essência Espiritual (ex: 5 essências)
 * - Item de saída (ex: Tijolo Infundido com Almas)
 * - Validação de itens e recursos necessários
 * 
 * Receitas implementadas:
 * - Tijolo comum + 5 Essência Espiritual = Tijolo Infundido com Almas
 * - Ferro + 3 Essência = Ferro Amaldiçoado
 * - Madeira + 2 Essência = Madeira Espiritual
 */
public class RunicTransmutationRecipe implements Recipe<SingleRecipeInput> {
    
    private final ResourceLocation id;
    private final Ingredient inputItem;
    private final int essenceCost;
    private final ItemStack result;
    
    /**
     * Construtor da receita de transmutação
     * 
     * @param id ID único da receita
     * @param inputItem Item de entrada para transmutação
     * @param essenceCost Quantidade de Essência Espiritual necessária
     * @param result Item resultante da transmutação
     */
    public RunicTransmutationRecipe(ResourceLocation id, Ingredient inputItem, int essenceCost, ItemStack result) {
        this.id = id;
        this.inputItem = inputItem;
        this.essenceCost = essenceCost;
        this.result = result;
    }
    
    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return inputItem.test(input.getItem(0));
    }
    
    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider) {
        return result.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }
    
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }
    
    /**
     * Obtém o ingrediente necessário para esta receita
     */
    public Ingredient getInputItem() {
        return inputItem;
    }
    
    /**
     * Obtém o custo em Essência Espiritual para esta transmutação
     */
    public int getEssenceCost() {
        return essenceCost;
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.RUNIC_TRANSMUTATION_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.RUNIC_TRANSMUTATION.get();
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }
    
    /**
     * Serializer para receitas de Transmutação Rúnica
     * Responsável por serializar/deserializar as receitas para JSON e rede
     */
    public static class Serializer implements RecipeSerializer<RunicTransmutationRecipe> {
        
        private static final MapCodec<RunicTransmutationRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(recipe -> recipe.id),
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.inputItem),
                Codec.INT.fieldOf("essence_cost").forGetter(recipe -> recipe.essenceCost),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(instance, RunicTransmutationRecipe::new)
        );
        
        private static final StreamCodec<RegistryFriendlyByteBuf, RunicTransmutationRecipe> STREAM_CODEC = 
            StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, recipe -> recipe.id,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.inputItem,
                ByteBufCodecs.VAR_INT, recipe -> recipe.essenceCost,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                RunicTransmutationRecipe::new
            );
        
        @Override
        public MapCodec<RunicTransmutationRecipe> codec() {
            return CODEC;
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RunicTransmutationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}