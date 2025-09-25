package com.cronicasaetherium.mod.items.magic;

import com.cronicasaetherium.mod.blocks.decoration.PolishedTwistedWillowBlock;
import com.cronicasaetherium.mod.blocks.decoration.RunicPlateBlock;
import com.cronicasaetherium.mod.blocks.decoration.RunicPlateBlockEntity;
import com.cronicasaetherium.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Varinha da Floresta - Item mágico para ativação de portais rituais
 * 
 * Esta varinha é o catalisador essencial para ativar o Portal Mágico ao Crisol Arcano.
 * Ela verifica se uma estrutura ritual 4x4 está corretamente construída e contém
 * os ingredientes necessários, então inicia o processo de ativação portal.
 * 
 * Funcionalidades:
 * - Verifica estrutura portal 4x4 (Salgueiro Polido + Placas Rúnicas nos cantos)
 * - Confirma presença dos ingredientes rituais nas Placas Rúnicas
 * - Consome ingredientes e ativa o portal com efeitos espetaculares
 * - Fornece feedback visual e sonoro durante todo o processo
 * 
 * Estrutura esperada (4x4):
 * P = Placa Rúnica (com ingredientes)
 * S = Salgueiro Torcido Polido  
 * C = Centro (onde o jogador clica)
 * 
 * P S S P
 * S C C S
 * S C C S  
 * P S S P
 * 
 * Ingredientes necessários (um em cada Placa):
 * - Coração de Adamantina (drop do mini-boss)
 * - Espírito Arcano Purificado
 * - Cristal de Aetherium Refinado
 * - Essência Dimensional Instável
 */
public class ForestWandItem extends Item {
    
    public ForestWandItem() {
        super(new Item.Properties()
            .stacksTo(1) // Único, como ferramentas mágicas
            .rarity(Rarity.RARE) // Raridade épica
            .durability(64) // Durabilidade limitada
        );
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack wand = context.getItemInHand();
        
        if (level.isClientSide || player == null) {
            return InteractionResult.SUCCESS; // Só processa no servidor
        }
        
        // Verifica se o jogador clicou no centro de uma possível estrutura 4x4
        if (!isValidCenterPosition(level, clickedPos)) {
            player.sendSystemMessage(Component.literal("§cEsta não é uma posição central válida para um portal."));
            return InteractionResult.FAIL;
        }
        
        // Valida a estrutura completa
        PortalValidationResult validation = validatePortalStructure(level, clickedPos);
        
        if (!validation.isValid()) {
            player.sendSystemMessage(Component.literal("§c" + validation.getErrorMessage()));
            return InteractionResult.FAIL;
        }
        
        // Verifica e consome ingredientes rituais
        if (!validateAndConsumeRitualIngredients(level, validation.getRunicPlates(), player)) {
            return InteractionResult.FAIL;
        }
        
        // Tudo válido! Inicia sequência de ativação do portal
        activatePortal(level, clickedPos, validation.getPortalBlocks(), player);
        
        // Consome durabilidade da varinha
        wand.hurtAndBreak(1, player, context.getHand());
        
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Verifica se a posição clicada pode ser o centro de um portal 4x4
     */
    private boolean isValidCenterPosition(Level level, BlockPos center) {
        // O centro deve ter espaço para um portal 2x2
        for (int x = 0; x <= 1; x++) {
            for (int z = 0; z <= 1; z++) {
                BlockPos checkPos = center.offset(x, 0, z);
                if (!level.getBlockState(checkPos).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Valida toda a estrutura do portal 4x4
     */
    private PortalValidationResult validatePortalStructure(Level level, BlockPos center) {
        BlockPos basePos = center.offset(-1, 0, -1); // Ajusta para início da estrutura 4x4
        PortalValidationResult result = new PortalValidationResult();
        
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                BlockPos checkPos = basePos.offset(x, 0, z);
                BlockState state = level.getBlockState(checkPos);
                
                // Centro (2x2) deve estar vazio
                if (x >= 1 && x <= 2 && z >= 1 && z <= 2) {
                    if (!state.isAir()) {
                        result.setError("O centro do portal deve estar vazio.");
                        return result;
                    }
                    result.addPortalBlock(checkPos);
                }
                // Cantos devem ser Placas Rúnicas
                else if ((x == 0 && z == 0) || (x == 0 && z == 3) || 
                         (x == 3 && z == 0) || (x == 3 && z == 3)) {
                    if (!(state.getBlock() instanceof RunicPlateBlock)) {
                        result.setError("Os cantos devem ter Placas Rúnicas.");
                        return result;
                    }
                    result.addRunicPlate(checkPos);
                }
                // Outras posições devem ser Salgueiro Polido
                else {
                    if (!(state.getBlock() instanceof PolishedTwistedWillowBlock)) {
                        result.setError("A estrutura deve ser feita de Salgueiro Torcido Polido.");
                        return result;
                    }
                }
            }
        }
        
        result.setValid(true);
        return result;
    }
    
    /**
     * Verifica se as Placas Rúnicas contêm os ingredientes corretos e os consome
     */
    private boolean validateAndConsumeRitualIngredients(Level level, List<BlockPos> runicPlates, Player player) {
        // TODO: Definir ingredientes específicos necessários
        // Por ora, verifica se cada placa tem pelo menos um item
        
        for (BlockPos platePos : runicPlates) {
            BlockEntity be = level.getBlockEntity(platePos);
            if (be instanceof RunicPlateBlockEntity plate) {
                if (plate.isEmpty()) {
                    player.sendSystemMessage(Component.literal(
                        "§cTodas as Placas Rúnicas devem conter ingredientes rituais."));
                    return false;
                }
            }
        }
        
        // Consome os ingredientes
        for (BlockPos platePos : runicPlates) {
            BlockEntity be = level.getBlockEntity(platePos);
            if (be instanceof RunicPlateBlockEntity plate) {
                plate.setStoredItem(ItemStack.EMPTY); // Remove o item
            }
        }
        
        player.sendSystemMessage(Component.literal("§aIngredientes rituais consumidos. Portal ativando..."));
        return true;
    }
    
    /**
     * Ativa o portal com efeitos espetaculares
     */
    private void activatePortal(Level level, BlockPos center, List<BlockPos> portalBlocks, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        
        // Efeitos sonoros dramáticos
        serverLevel.playSound(null, center, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 
            1.0F, 1.0F);
        
        // Cria portal após delay para os efeitos
        serverLevel.scheduleTick(center, ModBlocks.POLISHED_TWISTED_WILLOW.get(), 60); // 3 segundos
        
        // Partículas espetaculares
        createPortalActivationEffects(serverLevel, center, portalBlocks);
        
        // Preenche os blocos centrais com portal
        for (BlockPos portalPos : portalBlocks) {
            serverLevel.setBlock(portalPos, ModBlocks.CRISOL_ARCANO_PORTAL.get().defaultBlockState(), 3);
        }
        
        player.sendSystemMessage(Component.literal("§dPortal para o Crisol Arcano ativado com sucesso!"));
    }
    
    /**
     * Cria os efeitos visuais de ativação do portal
     */
    private void createPortalActivationEffects(ServerLevel level, BlockPos center, List<BlockPos> portalBlocks) {
        // Explosão de partículas no centro
        for (int i = 0; i < 100; i++) {
            double d0 = center.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2;
            double d1 = center.getY() + 0.5 + level.random.nextDouble() * 2;
            double d2 = center.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2;
            
            level.sendParticles(ParticleTypes.ENCHANT, d0, d1, d2, 1, 0, 0.1, 0, 0.1);
            level.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, 0, 0.05, 0, 0.05);
        }
        
        // Raios de energia das placas para o centro
        // TODO: Implementar efeito de partículas fluindo das placas rúnicas
    }
    
    /**
     * Classe auxiliar para resultado da validação
     */
    private static class PortalValidationResult {
        private boolean valid = false;
        private String errorMessage = "";
        private List<BlockPos> runicPlates = new java.util.ArrayList<>();
        private List<BlockPos> portalBlocks = new java.util.ArrayList<>();
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getErrorMessage() { return errorMessage; }
        public void setError(String error) { this.errorMessage = error; }
        public List<BlockPos> getRunicPlates() { return runicPlates; }
        public void addRunicPlate(BlockPos pos) { runicPlates.add(pos); }
        public List<BlockPos> getPortalBlocks() { return portalBlocks; }
        public void addPortalBlock(BlockPos pos) { portalBlocks.add(pos); }
    }
}