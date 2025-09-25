package com.cronicasaetherium.mod.common.ritual;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Ritual da Veia Oculta - Efeito mágico que auxilia a mineração tecnológica
 * 
 * Este ritual representa a segunda ferramenta de utilidade cruzada, onde a magia
 * auxilia diretamente atividades tecnológicas (mineração).
 * 
 * Funcionamento:
 * - Ritual executado no Altar de Infusão (Tier 2 magic)
 * - Ingredientes: Bússola + Fragmentos de Alma + Espíritos Arcanos
 * - Duração: 2 minutos (2400 ticks)
 * - Raio de ação: 32 blocos ao redor do jogador
 * 
 * Efeito:
 * - Todos os minérios ficam visíveis através das paredes
 * - Minérios brilham com cores distintas por tipo
 * - Efeito segue o jogador conforme ele se move
 * - Funciona em todas as dimensões
 * 
 * Significado da Sinergia:
 * - Jogadores magic podem auxiliar eficientemente mineração tech
 * - Jogadores tech se beneficiam de rituais mágicos
 * - Alternativa mágica a tecnologias de scanning/radar
 */
public class VeinRitualEffect extends MobEffect {
    
    // Configurações do efeito
    private static final int DETECTION_RADIUS = 32; // Raio de detecção em blocos
    private static final int UPDATE_INTERVAL = 20;   // Atualiza a cada segundo
    
    /**
     * Construtor do efeito Veia Oculta
     */
    public VeinRitualEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x9400D3); // Cor roxa para magia
    }
    
    /**
     * Aplicado a cada tick enquanto o efeito está ativo
     * 
     * @param entity Entidade com o efeito
     * @param amplifier Nível do efeito (não usado)
     */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide()) {
            // Atualiza detecção de minérios a cada segundo
            if (entity.tickCount % UPDATE_INTERVAL == 0) {
                updateOreDetection(player);
            }
        }
        
        return true;
    }
    
    /**
     * Verifica se o efeito deve ser aplicado neste tick
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // Aplica a cada tick para detecção contínua
    }
    
    /**
     * Quando o efeito é removido do jogador
     * Remove highlighting dos minérios
     */
    @Override
    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        super.onEffectRemoved(entity, amplifier);
        
        if (entity instanceof Player player && !player.level().isClientSide()) {
            clearOreHighlighting(player);
            
            // Mensagem de feedback
            player.sendSystemMessage(Component.translatable("effect.cronicasaetherium.vein_ritual.ended")
                .withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }
    
    /**
     * Quando o efeito é adicionado ao jogador
     * Inicia detecção de minérios
     */
    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        super.onEffectAdded(entity, amplifier);
        
        if (entity instanceof Player player && !player.level().isClientSide()) {
            // Mensagem de boas-vindas
            player.sendSystemMessage(Component.translatable("effect.cronicasaetherium.vein_ritual.started", DETECTION_RADIUS)
                .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
            
            // Primeira detecção imediata
            updateOreDetection(player);
        }
    }
    
    /**
     * Atualiza a detecção de minérios ao redor do jogador
     * 
     * @param player Jogador com o efeito
     */
    private void updateOreDetection(Player player) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        List<OreDetection> detectedOres = new ArrayList<>();
        
        // Varre área ao redor do jogador
        for (int x = -DETECTION_RADIUS; x <= DETECTION_RADIUS; x++) {
            for (int y = -DETECTION_RADIUS; y <= DETECTION_RADIUS; y++) {
                for (int z = -DETECTION_RADIUS; z <= DETECTION_RADIUS; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    
                    // Verifica se é um minério
                    OreType oreType = getOreType(blockState.getBlock());
                    if (oreType != OreType.NONE) {
                        detectedOres.add(new OreDetection(checkPos, oreType));
                    }
                }
            }
        }
        
        // Aplica highlighting aos minérios detectados
        applyOreHighlighting(player, detectedOres);
        
        // Log para debug
        if (!detectedOres.isEmpty()) {
            CronicasAetherium.LOGGER.debug("Ritual da Veia detectou {} minérios ao redor de {}",
                detectedOres.size(), player.getName().getString());
        }
    }
    
    /**
     * Determina o tipo de minério de um bloco
     * 
     * @param block Bloco a verificar
     * @return Tipo do minério
     */
    private OreType getOreType(Block block) {
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) {
            return OreType.IRON;
        } else if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE) {
            return OreType.COAL;
        } else if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE) {
            return OreType.GOLD;
        } else if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) {
            return OreType.DIAMOND;
        } else if (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) {
            return OreType.REDSTONE;
        } else if (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE) {
            return OreType.LAPIS;
        } else if (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE) {
            return OreType.EMERALD;
        }
        
        // TODO: Adicionar minérios customizados do mod
        // } else if (block == ModBlocks.COPPER_ORE.get() || block == ModBlocks.DEEPSLATE_COPPER_ORE.get()) {
        //     return OreType.COPPER;
        // } else if (block == ModBlocks.TIN_ORE.get() || block == ModBlocks.DEEPSLATE_TIN_ORE.get()) {
        //     return OreType.TIN;
        // } else if (block == ModBlocks.COBALT_ORE.get() || block == ModBlocks.DEEPSLATE_COBALT_ORE.get()) {
        //     return OreType.COBALT;
        
        return OreType.NONE;
    }
    
    /**
     * Aplica highlighting visual aos minérios detectados
     * 
     * @param player Jogador que vê o highlighting
     * @param ores Lista de minérios detectados
     */
    private void applyOreHighlighting(Player player, List<OreDetection> ores) {
        if (player instanceof ServerPlayer serverPlayer) {
            // TODO: Implementar highlighting visual quando possível
            // Por enquanto, envia informação via chat para debug
            if (!ores.isEmpty() && player.tickCount % (20 * 10) == 0) { // A cada 10 segundos
                int ironCount = (int) ores.stream().filter(o -> o.type == OreType.IRON).count();
                int coalCount = (int) ores.stream().filter(o -> o.type == OreType.COAL).count();
                int goldCount = (int) ores.stream().filter(o -> o.type == OreType.GOLD).count();
                int diamondCount = (int) ores.stream().filter(o -> o.type == OreType.DIAMOND).count();
                
                if (ironCount + coalCount + goldCount + diamondCount > 0) {
                    player.sendSystemMessage(Component.translatable("effect.cronicasaetherium.vein_ritual.detection",
                        ironCount, coalCount, goldCount, diamondCount)
                        .withStyle(net.minecraft.ChatFormatting.AQUA));
                }
            }
        }
    }
    
    /**
     * Remove highlighting dos minérios
     * 
     * @param player Jogador para limpar highlighting
     */
    private void clearOreHighlighting(Player player) {
        // TODO: Implementar limpeza do highlighting visual
        CronicasAetherium.LOGGER.debug("Limpando highlighting de minérios para {}", player.getName().getString());
    }
    
    /**
     * Aplica o efeito Veia Oculta a um jogador
     * 
     * @param player Jogador alvo
     * @param durationSeconds Duração em segundos
     */
    public static void applyToPlayer(Player player, int durationSeconds) {
        MobEffectInstance effectInstance = new MobEffectInstance(
            ModEffects.VEIN_RITUAL.get(), 
            durationSeconds * 20, // Converte para ticks
            0, // Amplifier 0
            false, // Não é ambiente
            true,  // Mostra partículas
            true   // Mostra ícone
        );
        
        player.addEffect(effectInstance);
        
        CronicasAetherium.LOGGER.info("Aplicado Ritual da Veia Oculta em {} por {} segundos",
            player.getName().getString(), durationSeconds);
    }
    
    /**
     * Enumeration para tipos de minério
     */
    private enum OreType {
        NONE, IRON, COAL, GOLD, DIAMOND, REDSTONE, LAPIS, EMERALD, COPPER, TIN, COBALT
    }
    
    /**
     * Classe para armazenar detecções de minério
     */
    private static class OreDetection {
        public final BlockPos position;
        public final OreType type;
        
        public OreDetection(BlockPos position, OreType type) {
            this.position = position;
            this.type = type;
        }
    }
}

/**
 * Placeholder para o registro de efeitos
 * TODO: Mover para classe de registro apropriada
 */
class ModEffects {
    public static java.util.function.Supplier<VeinRitualEffect> VEIN_RITUAL = 
        () -> new VeinRitualEffect();
}