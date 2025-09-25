package com.cronicasaetherium.mod.items.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Armadura de Bronze - Equipamento especializado tecnológico Tier 1
 * 
 * Esta armadura representa o caminho tecnológico puro, oferecendo proteção
 * física superior em troca de nenhum benefício mágico. É fabricada usando
 * Placas de Bronze criadas na Prensa de Engrenagens.
 * 
 * Características:
 * - Proteção física alta (equivalente ao ferro vanilla)
 * - Resistência a knockback aumentada por peça
 * - Aparência robusta e industrial
 * - Sem benefícios mágicos
 * - Durabilidade elevada
 * 
 * Esta armadura incentiva o jogador a se especializar na tecnologia,
 * oferecendo excelente proteção para explorar minas e enfrentar mobs.
 */
public class BronzeArmorItem extends ArmorItem {
    
    // Bônus de resistência a knockback por peça (10% por peça, máximo 40%)
    private static final float KNOCKBACK_RESISTANCE_PER_PIECE = 0.1f;
    
    /**
     * Construtor da Armadura de Bronze
     * 
     * @param material Material da armadura (definido em ModArmorMaterials)
     * @param type Tipo da peça (capacete, peitoral, pernas, botas)
     * @param properties Propriedades básicas do item
     */
    public BronzeArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }
    
    /**
     * Adiciona tooltip explicativo à armadura
     * Mostra benefícios tecnológicos e estatísticas
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        
        // Descrição da especialização tecnológica
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.bronze_armor.desc")
            .withStyle(net.minecraft.ChatFormatting.GRAY));
        
        // Bônus de knockback resistance
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.bronze_armor.knockback_resistance", 
            (int)(KNOCKBACK_RESISTANCE_PER_PIECE * 100))
            .withStyle(net.minecraft.ChatFormatting.BLUE));
        
        // Indicação de conjunto
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.bronze_armor.set_bonus")
            .withStyle(net.minecraft.ChatFormatting.GREEN));
        
        // Linha de separação
        tooltipComponents.add(Component.empty());
        
        // Identificação tecnológica
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.tech_path")
            .withStyle(net.minecraft.ChatFormatting.GOLD, net.minecraft.ChatFormatting.ITALIC));
    }
    
    /**
     * Aplicado quando o jogador equipa a peça
     * Adiciona resistência a knockback
     */
    public void onEquip(LivingEntity entity) {
        if (entity instanceof Player player) {
            // Atualiza atributos quando equipada
            updateKnockbackResistance(player);
        }
    }
    
    /**
     * Aplicado quando o jogador desequipa a peça
     * Remove resistência a knockback
     */
    public void onUnequip(LivingEntity entity) {
        if (entity instanceof Player player) {
            // Atualiza atributos quando removida
            updateKnockbackResistance(player);
        }
    }
    
    /**
     * Atualiza a resistência a knockback baseada no número de peças equipadas
     * 
     * @param player Jogador cujos atributos devem ser atualizados
     */
    private void updateKnockbackResistance(Player player) {
        int bronzeArmorPieces = 0;
        
        // Conta quantas peças de bronze estão equipadas
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof BronzeArmorItem) {
                bronzeArmorPieces++;
            }
        }
        
        // Calcula resistência total (10% por peça)
        float totalResistance = bronzeArmorPieces * KNOCKBACK_RESISTANCE_PER_PIECE;
        
        // TODO: Aplicar o modificador de atributo quando o sistema de atributos for implementado
        // player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(totalResistance);
    }
    
    /**
     * Obtém o nível de proteção extra desta armadura
     * Usado para cálculos de dano avançados
     */
    public float getExtraProtection(Type armorType) {
        switch (armorType) {
            case HELMET: return 2.5f;
            case CHESTPLATE: return 6.0f;
            case LEGGINGS: return 5.0f;
            case BOOTS: return 2.0f;
            default: return 0.0f;
        }
    }
    
    /**
     * Verifica se esta peça faz parte do conjunto bronze
     * Usado para cálculos de bônus de conjunto
     */
    public boolean isBronzeArmorPiece() {
        return true;
    }
    
    /**
     * Durabilidade extra da armadura de bronze
     * Armadura tecnológica é construída para durar
     */
    public int getExtraDurability() {
        return switch (getType()) {
            case HELMET -> 50;
            case CHESTPLATE -> 80;
            case LEGGINGS -> 70;
            case BOOTS -> 40;
        };
    }
}