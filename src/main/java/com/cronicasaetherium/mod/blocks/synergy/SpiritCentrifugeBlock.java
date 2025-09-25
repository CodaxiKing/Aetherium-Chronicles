package com.cronicasaetherium.mod.blocks.synergy;

import com.cronicasaetherium.mod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Centrífuga Espiritual - Máquina de sinergia entre Tecnologia e Magia
 * 
 * Esta é a primeira máquina que permite que a tecnologia (FE) processe
 * elementos mágicos (Bolsas de Espírito) de forma automatizada.
 * 
 * Funcionalidade:
 * - Consome Forge Energy para operar
 * - Processa Bolsas de Espírito cheias (entrada)
 * - Separa os espíritos em tipos específicos (3 saídas)
 * - Interface gráfica para visualizar o processo
 * - Automação através de pipes/conduítes de outros mods
 * 
 * Significado da Sinergia:
 * - Jogadores tech podem processar itens mágicos eficientemente
 * - Jogadores magic se beneficiam da automação tecnológica
 * - Ponte entre os dois sistemas de progressão
 */
public class SpiritCentrifugeBlock extends BaseEntityBlock {
    
    /**
     * Construtor da Centrífuga Espiritual
     */
    public SpiritCentrifugeBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpiritCentrifugeBlockEntity(pos, state);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    /**
     * Interação do jogador com a máquina
     * Abre a interface gráfica
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SpiritCentrifugeBlockEntity centrifuge && player instanceof ServerPlayer serverPlayer) {
                // TODO: Abrir GUI quando implementada
                // centrifuge.openMenu(serverPlayer);
                return InteractionResult.CONSUME;
            }
        }
        
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    /**
     * Ticker da BlockEntity para processamento contínuo
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        
        return createTickerHelper(blockEntityType, ModBlockEntities.SPIRIT_CENTRIFUGE.get(),
            (level1, pos, state1, blockEntity) -> blockEntity.tick());
    }
    
    /**
     * Quando a máquina é removida, dropa seus itens
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SpiritCentrifugeBlockEntity centrifuge) {
                centrifuge.dropContents();
            }
        }
        
        super.onRemove(state, level, pos, newState, isMoving);
    }
}