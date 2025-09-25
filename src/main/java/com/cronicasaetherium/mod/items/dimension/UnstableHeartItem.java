package com.cronicasaetherium.mod.items.dimension;

import com.cronicasaetherium.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Coração Instável - Ativador do Portal Arcano
 * 
 * Este item é usado para ativar estruturas de portal construídas com
 * blocos de Estrutura do Portal Arcano. Quando usado na estrutura correta,
 * cria um portal funcional para a dimensão Crisol Arcano.
 * 
 * Funcionalidades:
 * - Ativação de portais em estruturas válidas
 * - Validação da forma do portal (3x3 básico)
 * - Consumo do item ao ativar o portal
 * - Efeitos visuais e sonoros de ativação
 * - Integração com sistema de proficiência arcana
 */
public class UnstableHeartItem extends Item {
    
    /**
     * Construtor do Coração Instável
     * 
     * @param properties Propriedades do item
     */
    public UnstableHeartItem(Properties properties) {
        super(properties);
    }
    
    /**
     * Manipula o uso do item em blocos
     * 
     * Verifica se o bloco alvo faz parte de uma estrutura de portal válida
     * e ativa o portal se a estrutura estiver correta.
     * 
     * @param context Contexto do uso
     * @return Resultado da interação
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack heartStack = context.getItemInHand();
        
        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS;
        }
        
        BlockState clickedBlock = level.getBlockState(pos);
        
        // Verifica se foi clicado em um bloco de estrutura de portal
        if (clickedBlock.is(ModBlocks.ARCANE_PORTAL_FRAME.get())) {
            return attemptPortalActivation(level, pos, player, heartStack);
        }
        
        // Bloco inválido
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "§cO Coração Instável deve ser usado em uma Estrutura do Portal Arcano."));
        
        return InteractionResult.FAIL;
    }
    
    /**
     * Tenta ativar um portal na posição especificada
     * 
     * @param level Nível/mundo
     * @param centerPos Posição central do portal
     * @param player Jogador ativando
     * @param heartStack Stack do coração instável
     * @return Resultado da tentativa
     */
    private InteractionResult attemptPortalActivation(Level level, BlockPos centerPos, Player player, ItemStack heartStack) {
        // Verifica se há uma estrutura de portal válida ao redor
        if (!isValidPortalStructure(level, centerPos)) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "§cEstrutura de portal inválida! Construa um quadrado 3x3 de Estruturas do Portal Arcano."));
            return InteractionResult.FAIL;
        }
        
        // Encontra o centro da estrutura 3x3
        BlockPos portalCenter = findPortalCenter(level, centerPos);
        if (portalCenter == null) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "§cNão foi possível determinar o centro do portal."));
            return InteractionResult.FAIL;
        }
        
        // Ativa o portal
        activatePortal(level, portalCenter, player);
        
        // Consome o coração instável
        heartStack.shrink(1);
        
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "§5✨ Portal Arcano ativado! O caminho para o Crisol Arcano está aberto."));
        
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Verifica se há uma estrutura de portal válida ao redor da posição
     * 
     * @param level Nível/mundo
     * @param pos Posição a verificar
     * @return true se a estrutura é válida
     */
    private boolean isValidPortalStructure(Level level, BlockPos pos) {
        // Verifica um padrão 3x3 de blocos de estrutura de portal
        // com o centro vazio (onde será colocado o portal ativo)
        
        int frameCount = 0;
        BlockPos centerCandidate = null;
        
        // Procura por uma estrutura 3x3 válida na área
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos checkPos = pos.offset(dx, 0, dz);
                BlockState state = level.getBlockState(checkPos);
                
                if (dx == 0 && dz == 0) {
                    // Centro - deve estar vazio
                    if (state.isAir()) {
                        centerCandidate = checkPos;
                    }
                } else {
                    // Bordas - devem ser estruturas de portal
                    if (state.is(ModBlocks.ARCANE_PORTAL_FRAME.get())) {
                        frameCount++;
                    }
                }
            }
        }
        
        // Estrutura válida: 8 blocos de frame e centro vazio
        return frameCount >= 8 && centerCandidate != null;
    }
    
    /**
     * Encontra o centro de uma estrutura de portal válida
     * 
     * @param level Nível/mundo
     * @param startPos Posição inicial de busca
     * @return Posição do centro ou null se não encontrar
     */
    private BlockPos findPortalCenter(Level level, BlockPos startPos) {
        // Verifica as posições adjacentes para encontrar o centro vazio
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos testCenter = startPos.offset(dx, 0, dz);
                if (level.getBlockState(testCenter).isAir()) {
                    // Verifica se esta posição realmente está no centro de uma estrutura válida
                    if (isValidPortalStructure(level, testCenter)) {
                        return testCenter;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Ativa o portal na posição especificada
     * 
     * @param level Nível/mundo
     * @param centerPos Posição central do portal
     * @param player Jogador que ativou
     */
    private void activatePortal(Level level, BlockPos centerPos, Player player) {
        // Coloca o bloco de portal ativo no centro
        level.setBlock(centerPos, ModBlocks.ARCANE_PORTAL.get().defaultBlockState(), 3);
        
        // Efeitos visuais e sonoros
        level.playSound(null, centerPos, SoundEvents.END_PORTAL_TRIGGER, 
            SoundSource.BLOCKS, 1.0f, 1.0f);
        
        level.playSound(null, centerPos, SoundEvents.ENCHANTMENT_TABLE_USE, 
            SoundSource.BLOCKS, 0.8f, 1.2f);
        
        // TODO: Adicionar partículas especiais quando o sistema for expandido
        // TODO: Adicionar XP de proficiência arcana para o jogador
        // ModCapabilities.ifPlayerProficiencyPresent(player, proficiency -> {
        //     proficiency.addArcanaXp(ModConfig.BASE_ARCANA_XP.get() * 2); // Bônus por ativar portal
        // });
    }
}