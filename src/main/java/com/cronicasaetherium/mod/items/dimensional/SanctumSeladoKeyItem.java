package com.cronicasaetherium.mod.items.dimensional;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Chave do Sanctum Selado - Item para sintonização dimensional mágica
 * 
 * Esta chave permite acesso ao Sanctum Selado, a dimensão onde residem os
 * chefes mágicos finais. Criada através de rituais de infusão arcanos complexos,
 * representa a maestria do jogador sobre as artes místicas.
 * 
 * Crafting sugerido (Ritual de Infusão Tier 3):
 * - Essência do Guardião Arcano (drop do primeiro chefe mágico)
 * - Cristal de Aetherium Purificado (componente mágico Tier 3)
 * - Fragmento de Alma Ancestral (ingrediente ritual raro)
 * - Selo Rúnico Perfeito (maestria em runas)
 * 
 * Criação:
 * - Requere Altar de Infusão Tier 3
 * - Consome 500 de Essência Espiritual no processo
 * - Ritual leva 10 minutos para completar
 * - Chance de falha se ingredientes não forem perfeitos
 * 
 * Lore: "Selada com magia ancestral e banhada na luz de mil estrelas.
 *        Ressoa com os ecos de feitiços há muito esquecidos."
 */
public class SanctumSeladoKeyItem extends Item {
    
    public SanctumSeladoKeyItem() {
        super(new Item.Properties()
            .stacksTo(4) // Limitado devido à complexidade de criação
            .rarity(Rarity.EPIC) // Raridade épica como componente ritual
            .fireResistant() // Protegida por encantamentos mágicos
        );
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        // Tooltip explicativo
        tooltipComponents.add(Component.literal("§7Chave de Sintonização Dimensional"));
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§dUse no Nexus Dimensional para"));
        tooltipComponents.add(Component.literal("§dabrir um portal para o §5Sanctum Selado§d."));
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§4⚠ §5Dimensão de Alta Magia §4⚠"));
        tooltipComponents.add(Component.literal("§7Recomendado: Resistência Mágica Tier 3+"));
        
        // Informação sobre criação
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§9Criada através de Ritual de Infusão"));
        tooltipComponents.add(Component.literal("§9Requer Altar de Infusão Tier 3"));
        
        // Lore
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§o§5\"Selada com magia ancestral e banhada"));
        tooltipComponents.add(Component.literal("§o§5na luz de mil estrelas. Ressoa com os"));
        tooltipComponents.add(Component.literal("§o§5ecos de feitiços há muito esquecidos.\""));
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Sempre brilhante devido à natureza mágica pura
    }
    
    /**
     * Verifica se este item é uma chave dimensional válida
     * Usado pelo Nexus Dimensional para identificar o tipo
     */
    public String getDimensionType() {
        return "sanctum_selado";
    }
    
    /**
     * Retorna a cor da partícula/aura associada a esta dimensão
     */
    public int getParticleColor() {
        return 0x9932CC; // Cor roxa mágica (magia/misticismo)
    }
}