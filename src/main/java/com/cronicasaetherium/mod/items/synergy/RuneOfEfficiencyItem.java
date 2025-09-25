package com.cronicasaetherium.mod.items.synergy;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Runa de Eficiência - Item de sinergia Magia → Tecnologia
 * 
 * Esta runa representa a aplicação de conhecimento mágico para aprimorar
 * sistemas tecnológicos. Quando aplicada em máquinas, concede bônus de
 * eficiência, velocidade ou capacidade.
 * 
 * Funcionalidades:
 * - Aplicação em máquinas para conceder buffs
 * - Efeito visual mágico quando usada
 * - Consumo único (runa é destruída ao usar)
 * - Integração com sistema de proficiência arcana
 * - Diferentes níveis de eficiência baseados no conhecimento do jogador
 * 
 * A runa é fundamental para jogadores que focam em magia mas querem
 * otimizar sistemas tecnológicos, promovendo sinergia entre os paths.
 */
public class RuneOfEfficiencyItem extends Item {
    
    // Constantes de configuração da runa
    private static final int BASE_EFFICIENCY_BONUS = 25; // Bônus base de eficiência em %
    private static final int PARTICLE_COUNT = 20; // Número de partículas do efeito visual
    private static final double PARTICLE_SPREAD = 1.5; // Dispersão das partículas
    
    /**
     * Construtor da Runa de Eficiência
     * 
     * @param properties Propriedades do item (durabilidade, stack size, etc.)
     */
    public RuneOfEfficiencyItem(Properties properties) {
        super(properties);
    }
    
    /**
     * Manipula o uso da runa em blocos (máquinas)
     * 
     * Este método é chamado quando o jogador usa a runa (botão direito)
     * em um bloco. Verifica se o bloco é uma máquina válida e aplica
     * o efeito de eficiência se for o caso.
     * 
     * @param context Contexto do uso (jogador, posição, bloco, etc.)
     * @return Resultado da interação
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack runeStack = context.getItemInHand();
        
        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS; // Processa apenas no servidor
        }
        
        BlockState blockState = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        
        // Verifica se o bloco é uma máquina válida
        if (isMachineBlock(blockState, blockEntity)) {
            // Aplica o efeito de eficiência
            boolean success = applyEfficiencyEffect(level, pos, blockState, blockEntity, player);
            
            if (success) {
                // Consome a runa
                runeStack.shrink(1);
                
                // Efeitos audiovisuais
                playEffectSounds(level, pos);
                spawnMagicalParticles((ServerLevel) level, pos);
                
                // Mensagem de sucesso para o jogador
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "§5✨ Runa de Eficiência aplicada! A máquina agora opera com " + 
                    BASE_EFFICIENCY_BONUS + "% mais eficiência."));
                
                CronicasAetherium.LOGGER.info("Jogador {} aplicou Runa de Eficiência na posição {}", 
                    player.getName().getString(), pos);
                
                return InteractionResult.SUCCESS;
            }
        }
        
        // Falha na aplicação - máquina inválida ou já aprimorada
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "§cEsta runa não pode ser aplicada aqui. Certifique-se de usar em uma máquina válida."));
        
        return InteractionResult.FAIL;
    }
    
    /**
     * Verifica se um bloco é uma máquina válida para aplicar a runa
     * 
     * @param blockState Estado do bloco
     * @param blockEntity BlockEntity do bloco (se houver)
     * @return true se for uma máquina válida
     */
    private boolean isMachineBlock(BlockState blockState, BlockEntity blockEntity) {
        if (blockEntity == null) {
            return false; // Máquinas sempre têm BlockEntity
        }
        
        // TODO: Expandir com verificações específicas quando mais máquinas forem adicionadas
        // Por ora, verifica se é uma das máquinas conhecidas do mod
        String blockName = blockState.getBlock().toString().toLowerCase();
        
        return blockName.contains("forge") || 
               blockName.contains("mill") || 
               blockName.contains("infuser") ||
               blockName.contains("altar");
    }
    
    /**
     * Aplica o efeito de eficiência na máquina
     * 
     * @param level Nível/mundo
     * @param pos Posição da máquina
     * @param blockState Estado do bloco da máquina
     * @param blockEntity BlockEntity da máquina
     * @param player Jogador que aplicou a runa
     * @return true se o efeito foi aplicado com sucesso
     */
    private boolean applyEfficiencyEffect(Level level, BlockPos pos, BlockState blockState, 
                                        BlockEntity blockEntity, Player player) {
        
        // TODO: Implementar o sistema real de bônus de eficiência quando as máquinas forem expandidas
        // Por ora, apenas registra que o efeito foi aplicado
        
        // Verifica se a máquina já tem bônus aplicado
        if (hasEfficiencyBonus(blockEntity)) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "§6Esta máquina já possui um bônus de eficiência ativo."));
            return false;
        }
        
        // Calcula o bônus baseado na proficiência arcana do jogador
        int effectivenessBonus = calculateBonusFromProficiency(player);
        
        // Aplica o bônus na máquina
        applyBonusToMachine(blockEntity, effectivenessBonus);
        
        CronicasAetherium.LOGGER.debug("Bônus de eficiência de {}% aplicado na máquina na posição {}", 
            effectivenessBonus, pos);
        
        return true;
    }
    
    /**
     * Verifica se uma máquina já possui bônus de eficiência
     * 
     * @param blockEntity BlockEntity da máquina
     * @return true se já possui bônus
     */
    private boolean hasEfficiencyBonus(BlockEntity blockEntity) {
        // TODO: Implementar verificação real de bônus quando o sistema for expandido
        // Por ora, assume que não há bônus aplicado
        return false;
    }
    
    /**
     * Calcula o bônus de eficiência baseado na proficiência arcana do jogador
     * 
     * @param player Jogador que aplicou a runa
     * @return Porcentagem de bônus adicional
     */
    private int calculateBonusFromProficiency(Player player) {
        // TODO: Integrar com o sistema de proficiência quando implementado
        // Por ora, retorna o bônus base
        
        // PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        // if (proficiency != null) {
        //     int arcanaLevel = proficiency.getArcanaLevel();
        //     return BASE_EFFICIENCY_BONUS + (arcanaLevel * 5); // +5% por nível
        // }
        
        return BASE_EFFICIENCY_BONUS;
    }
    
    /**
     * Aplica o bônus de eficiência na BlockEntity da máquina
     * 
     * @param blockEntity BlockEntity da máquina
     * @param bonusPercentage Porcentagem de bônus a aplicar
     */
    private void applyBonusToMachine(BlockEntity blockEntity, int bonusPercentage) {
        // TODO: Implementar aplicação real do bônus quando o sistema de máquinas for expandido
        // Isso pode envolver:
        // - Marcar a máquina como aprimorada
        // - Armazenar o valor do bônus em NBT
        // - Modificar velocidade de processamento
        // - Reduzir consumo de energia
        
        CronicasAetherium.LOGGER.debug("Aplicando bônus de {}% na máquina {}", 
            bonusPercentage, blockEntity.getClass().getSimpleName());
    }
    
    /**
     * Reproduz sons mágicos quando a runa é aplicada
     * 
     * @param level Nível/mundo
     * @param pos Posição onde reproduzir o som
     */
    private void playEffectSounds(Level level, BlockPos pos) {
        // Som de encantamento quando a runa é aplicada
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, 
            SoundSource.BLOCKS, 0.8f, 1.2f);
        
        // Som adicional de sino mágico
        level.playSound(null, pos, SoundEvents.BELL_RESONATE, 
            SoundSource.BLOCKS, 0.5f, 1.5f);
    }
    
    /**
     * Gera partículas mágicas ao redor da máquina aprimorada
     * 
     * @param level Nível do servidor
     * @param pos Posição central das partículas
     */
    private void spawnMagicalParticles(ServerLevel level, BlockPos pos) {
        // Partículas de encantamento ao redor da máquina
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD;
            double offsetY = level.random.nextDouble() * PARTICLE_SPREAD;
            double offsetZ = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD;
            
            level.sendParticles(ParticleTypes.ENCHANT,
                pos.getX() + 0.5 + offsetX,
                pos.getY() + 0.5 + offsetY,
                pos.getZ() + 0.5 + offsetZ,
                1, 0, 0.1, 0, 0.02);
        }
        
        // Partículas douradas para indicar aprimoramento
        for (int i = 0; i < PARTICLE_COUNT / 2; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD * 0.7;
            double offsetY = level.random.nextDouble() * PARTICLE_SPREAD * 0.7;
            double offsetZ = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD * 0.7;
            
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5 + offsetX,
                pos.getY() + 0.5 + offsetY,
                pos.getZ() + 0.5 + offsetZ,
                1, 0, 0.05, 0, 0.01);
        }
    }
}