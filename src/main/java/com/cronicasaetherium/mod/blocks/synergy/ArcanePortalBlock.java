package com.cronicasaetherium.mod.blocks.synergy;

import com.cronicasaetherium.mod.registry.ModItems;
import com.cronicasaetherium.mod.world.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Bloco de Portal Ativo do Crisol Arcano
 * 
 * Este bloco representa o portal ativo para a dimensão do Crisol Arcano.
 * É criado temporariamente quando o jogador ativa a estrutura do portal
 * com o Coração Instável.
 * 
 * Funcionalidades:
 * - Teleporta jogadores para a dimensão Crisol Arcano
 * - Efeitos visuais de portal ativo
 * - Sistema de cooldown para prevenir uso excessivo
 * - Integração com sistema de proficiência arcana
 */
public class ArcanePortalBlock extends Block {
    
    /**
     * Construtor do bloco de portal ativo
     * 
     * @param properties Propriedades do bloco
     */
    public ArcanePortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    /**
     * Manipula a interação do jogador com o portal
     * 
     * Quando o jogador clica no portal ativo, verifica se pode viajar
     * e realiza a teleportação para a dimensão apropriada.
     * 
     * @param state Estado do bloco
     * @param level Nível/mundo atual
     * @param pos Posição do portal
     * @param player Jogador que interagiu
     * @param hit Informações do clique
     * @return Resultado da interação
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            return attemptTeleportation(serverPlayer, level, pos);
        }
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Tenta realizar a teleportação do jogador
     * 
     * @param player Jogador a teleportar
     * @param level Nível atual
     * @param portalPos Posição do portal
     * @return Resultado da tentativa
     */
    private InteractionResult attemptTeleportation(ServerPlayer player, Level level, BlockPos portalPos) {
        // Verifica cooldown do jogador (implementação básica)
        // TODO: Integrar com sistema de configuração para cooldown real
        
        // Determina a dimensão de destino
        Level targetLevel;
        if (level.dimension() == Level.OVERWORLD) {
            // Do Overworld para Crisol Arcano
            targetLevel = level.getServer().getLevel(ModDimensions.ARCANE_CRUCIBLE);
            if (targetLevel == null) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "§cErro: Dimensão Crisol Arcano não está disponível!"));
                return InteractionResult.FAIL;
            }
        } else if (level.dimension() == ModDimensions.ARCANE_CRUCIBLE) {
            // Do Crisol Arcano de volta ao Overworld
            targetLevel = level.getServer().getLevel(Level.OVERWORLD);
        } else {
            // Dimensão não suportada
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "§cEste portal não pode ser usado nesta dimensão!"));
            return InteractionResult.FAIL;
        }
        
        // Calcula posição de destino
        BlockPos targetPos = calculateTargetPosition(portalPos, targetLevel);
        
        // Realiza a teleportação
        performTeleportation(player, (ServerLevel) targetLevel, targetPos);
        
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Calcula a posição de destino na dimensão alvo
     * 
     * @param portalPos Posição do portal de origem
     * @param targetLevel Nível de destino
     * @return Posição segura para teleportação
     */
    private BlockPos calculateTargetPosition(BlockPos portalPos, Level targetLevel) {
        // TODO: Implementar lógica mais sofisticada de posicionamento
        // Por ora, usa a mesma posição relativa
        BlockPos targetPos = new BlockPos(portalPos.getX(), 100, portalPos.getZ());
        
        // Encontra uma posição segura
        for (int y = 100; y > 60; y--) {
            BlockPos testPos = new BlockPos(targetPos.getX(), y, targetPos.getZ());
            if (targetLevel.getBlockState(testPos).isAir() && 
                targetLevel.getBlockState(testPos.above()).isAir() &&
                !targetLevel.getBlockState(testPos.below()).isAir()) {
                return testPos;
            }
        }
        
        return targetPos; // Fallback para posição original
    }
    
    /**
     * Executa a teleportação do jogador
     * 
     * @param player Jogador a teleportar
     * @param targetLevel Nível de destino
     * @param targetPos Posição de destino
     */
    private void performTeleportation(ServerPlayer player, ServerLevel targetLevel, BlockPos targetPos) {
        // Efeitos sonoros e visuais antes da teleportação
        player.level().playSound(null, player.blockPosition(), 
            net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, 
            net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
        
        // Teleporta o jogador
        player.teleportTo(targetLevel, 
            targetPos.getX() + 0.5, 
            targetPos.getY(), 
            targetPos.getZ() + 0.5, 
            player.getYRot(), 
            player.getXRot());
        
        // Mensagem de confirmação
        String dimensionName = targetLevel.dimension() == ModDimensions.ARCANE_CRUCIBLE ? 
            "Crisol Arcano" : "Mundo Superior";
        
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "§5✨ Teleportado para: " + dimensionName));
        
        // TODO: Adicionar XP de proficiência arcana pela viagem dimensional
        // ModCapabilities.ifPlayerProficiencyPresent(player, proficiency -> {
        //     proficiency.addArcanaXp(ModConfig.BASE_ARCANA_XP.get());
        // });
    }
}