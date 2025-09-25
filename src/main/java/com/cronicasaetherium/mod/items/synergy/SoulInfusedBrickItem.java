package com.cronicasaetherium.mod.items.synergy;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Tijolo Infundido com Almas - Item de sinergia entre Tecnologia e Magia
 * 
 * Este é o primeiro item que força a interação entre os dois pilares do mod.
 * Criado através de transmutação mágica (Pedra Rúnica) usando Tijolos + Essência Espiritual,
 * ele é essencial para avançar para o Tier 2 da tecnologia.
 * 
 * Características:
 * - Aparência: Tijolo com runas sutis que brilham fracamente no escuro
 * - Obtenção: Transmutação na Pedra Rúnica (1 Brick + 5 Essência Espiritual)
 * - Uso: Ingrediente obrigatório na receita do Alto-Forno Industrial (Tier 2)
 * - Significado: Representa a fusão entre matéria mundana e energia espiritual
 * 
 * Este item simboliza que a progressão tecnológica avançada requer compreensão
 * dos mistérios espirituais, forçando os jogadores tech a explorar a magia básica.
 */
public class SoulInfusedBrickItem extends Item {
    
    /**
     * Construtor do Tijolo Infundido com Almas
     * Configura propriedades básicas e aparência especial
     */
    public SoulInfusedBrickItem() {
        super(new Properties()
            .stacksTo(64) // Empilha até 64 unidades
            .rarity(net.minecraft.world.item.Rarity.UNCOMMON) // Raridade incomum (cor amarela)
        );
    }
    
    /**
     * Adiciona tooltip explicativo ao item
     * Informa como obter e para que serve
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        
        // Descrição principal do item
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.soul_infused_brick.desc1")
            .withStyle(net.minecraft.ChatFormatting.GRAY));
        
        // Como obter
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.soul_infused_brick.desc2")
            .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
        
        // Para que serve
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.soul_infused_brick.desc3")
            .withStyle(net.minecraft.ChatFormatting.GOLD));
        
        // Linha vazia para separação
        tooltipComponents.add(Component.empty());
        
        // Dica de sinergia
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.synergy.hint")
            .withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE, net.minecraft.ChatFormatting.ITALIC));
    }
    
    /**
     * Define que o item tem brilho sutil (como itens encantados)
     * Representa a infusão espiritual
     */
    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Sempre brilha como item mágico
    }
    
    /**
     * Verifica se o item pode ser usado em receitas de crafting
     * Por enquanto, apenas crafting normal (não automático)
     */
    public boolean isValidForCrafting() {
        return true;
    }
    
    /**
     * Obtém o brilho da luz que o item emite
     * Brilho fraco que representa a energia espiritual contida
     */
    public int getLightEmission() {
        return 2; // Luz fraca (nível 2 de 15)
    }
}