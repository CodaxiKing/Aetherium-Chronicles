package com.cronicasaetherium.mod.items.dimensional;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Chave da Forja Morta - Item para sintonização dimensional tecnológica
 * 
 * Esta chave permite ao jogador sintonizar o Nexus Dimensional para acessar
 * a dimensão da Forja Morta, onde residem os chefes tecnológicos finais.
 * Representa a maestria do jogador sobre a tecnologia avançada.
 * 
 * Crafting sugerido:
 * - Fragmentos do Autômato Corrompido (drop do primeiro chefe)
 * - Núcleo de Aço Reforçado (componente Tier 3 tecnológico)
 * - Cristal de Energia Concentrada (energia FE cristalizada)
 * - Placa de Circuito Avançado (tecnologia de ponta)
 * 
 * Uso:
 * - Clique direito no Nexus Dimensional para sintonizar
 * - Consumido no processo de sintonização
 * - Abre portal estável para a Forja Morta
 * - Portal permanece ativo até nova chave ser usada ou Nexus ser desativado
 * 
 * Lore: "Uma chave forjada nas profundezas da tecnologia perdida.
 *        Seus circuitos pulsam com o poder dos autômatos ancestrais."
 */
public class ForjaMortaKeyItem extends Item {
    
    public ForjaMortaKeyItem() {
        super(new Item.Properties()
            .stacksTo(4) // Limitado, mas permite alguns extras
            .rarity(Rarity.EPIC) // Raridade épica como drop de chefe
            .fireResistant() // Resistente ao fogo, como tecnologia avançada
        );
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        // Tooltip explicativo
        tooltipComponents.add(Component.literal("§7Chave de Sintonização Dimensional"));
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§6Use no Nexus Dimensional para"));
        tooltipComponents.add(Component.literal("§6abrir um portal para a §cForja Morta§6."));
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§4⚠ §cDimensão de Alto Perigo §4⚠"));
        tooltipComponents.add(Component.literal("§7Recomendado: Equipamentos Tier 3+"));
        
        // Lore
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§o§7\"Uma chave forjada nas profundezas"));
        tooltipComponents.add(Component.literal("§o§7da tecnologia perdida. Seus circuitos"));
        tooltipComponents.add(Component.literal("§o§7pulsam com o poder dos autômatos ancestrais.\""));
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Sempre brilhante devido à natureza mágico-tecnológica
    }
    
    /**
     * Verifica se este item é uma chave dimensional válida
     * Usado pelo Nexus Dimensional para identificar o tipo
     */
    public String getDimensionType() {
        return "forja_morta";
    }
    
    /**
     * Retorna a cor da partícula/aura associada a esta dimensão
     */
    public int getParticleColor() {
        return 0xFF6B47; // Cor laranja-avermelhada (forja/tecnologia)
    }
}