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
 * Armadura de Salgueiro Torcido - Equipamento especializado mágico Tier 1
 * 
 * Esta armadura representa o caminho mágico puro, oferecendo baixa proteção
 * física em troca de poderosos benefícios mágicos. É fabricada usando tábuas
 * de Salgueiro Torcido e Fragmentos de Alma.
 * 
 * Características:
 * - Proteção física baixa (equivalente ao couro vanilla)
 * - Bônus de Conjunto: Reduz custo de Essência Espiritual em 5% por peça
 * - Aparência orgânica com runas pulsantes
 * - Fragmentos de Alma brilham suavemente
 * - Durabilidade média
 * 
 * Esta armadura incentiva o jogador a se especializar na magia,
 * tornando rituais e transmutações mais eficientes.
 */
public class TwistedWillowArmorItem extends ArmorItem {
    
    // Redução de custo de Essência Espiritual por peça (5% por peça, máximo 20%)
    private static final float ESSENCE_COST_REDUCTION_PER_PIECE = 0.05f;
    
    /**
     * Construtor da Armadura de Salgueiro Torcido
     * 
     * @param material Material da armadura (definido em ModArmorMaterials)
     * @param type Tipo da peça (capacete, peitoral, pernas, botas)
     * @param properties Propriedades básicas do item
     */
    public TwistedWillowArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }
    
    /**
     * Adiciona tooltip explicativo à armadura
     * Mostra benefícios mágicos e estatísticas
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        
        // Descrição da especialização mágica
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.twisted_willow_armor.desc")
            .withStyle(net.minecraft.ChatFormatting.GRAY));
        
        // Bônus de redução de custo
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.twisted_willow_armor.essence_reduction", 
            (int)(ESSENCE_COST_REDUCTION_PER_PIECE * 100))
            .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
        
        // Indicação de conjunto
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.twisted_willow_armor.set_bonus")
            .withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE));
        
        // Aviso sobre proteção baixa
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.twisted_willow_armor.low_protection")
            .withStyle(net.minecraft.ChatFormatting.RED));
        
        // Linha de separação
        tooltipComponents.add(Component.empty());
        
        // Identificação mágica
        tooltipComponents.add(Component.translatable("item.cronicasaetherium.magic_path")
            .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE, net.minecraft.ChatFormatting.ITALIC));
    }
    
    /**
     * Define que a armadura tem brilho mágico sutil
     * Os Fragmentos de Alma nas peças brilham fracamente
     */
    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Brilho mágico constante
    }
    
    /**
     * Aplicado quando o jogador equipa a peça
     * Adiciona benefícios mágicos
     */
    public void onEquip(LivingEntity entity) {
        if (entity instanceof Player player) {
            updateMagicalBenefits(player);
        }
    }
    
    /**
     * Aplicado quando o jogador desequipa a peça
     * Remove benefícios mágicos
     */
    public void onUnequip(LivingEntity entity) {
        if (entity instanceof Player player) {
            updateMagicalBenefits(player);
        }
    }
    
    /**
     * Atualiza os benefícios mágicos baseados no número de peças equipadas
     * 
     * @param player Jogador cujos benefícios devem ser atualizados
     */
    private void updateMagicalBenefits(Player player) {
        int twistedWillowPieces = 0;
        
        // Conta quantas peças de Salgueiro Torcido estão equipadas
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof TwistedWillowArmorItem) {
                twistedWillowPieces++;
            }
        }
        
        // Calcula redução total de custo (5% por peça, máximo 20%)
        float totalReduction = twistedWillowPieces * ESSENCE_COST_REDUCTION_PER_PIECE;
        
        // TODO: Aplicar o benefício quando o sistema de magia for expandido
        // Armazenar o valor em uma capability do player para uso nos rituais
        // player.getCapability(ModCapabilities.MAGICAL_BENEFITS).ifPresent(cap -> {
        //     cap.setEssenceCostReduction(totalReduction);
        // });
    }
    
    /**
     * Obtém a redução de custo de essência para rituais
     * Usado pelas máquinas mágicas e rituais
     * 
     * @param player Jogador a verificar
     * @return Redução percentual (0.0 a 0.20)
     */
    public static float getEssenceCostReduction(Player player) {
        int twistedWillowPieces = 0;
        
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof TwistedWillowArmorItem) {
                twistedWillowPieces++;
            }
        }
        
        return twistedWillowPieces * ESSENCE_COST_REDUCTION_PER_PIECE;
    }
    
    /**
     * Verifica se esta peça faz parte do conjunto Salgueiro Torcido
     * Usado para cálculos de bônus de conjunto
     */
    public boolean isTwistedWillowArmorPiece() {
        return true;
    }
    
    /**
     * Obtém o brilho da luz que a armadura emite
     * As runas e Fragmentos de Alma brilham suavemente
     */
    public int getLightEmission() {
        return 3; // Luz fraca (nível 3 de 15)
    }
    
    /**
     * Resistência a dano mágico da armadura
     * Armadura mágica oferece proteção contra feitiços
     */
    public float getMagicalResistance(Type armorType) {
        return switch (armorType) {
            case HELMET -> 0.1f;    // 10% resistência mágica
            case CHESTPLATE -> 0.15f; // 15% resistência mágica
            case LEGGINGS -> 0.12f;   // 12% resistência mágica
            case BOOTS -> 0.08f;      // 8% resistência mágica
        };
    }
}