package com.cronicasaetherium.mod.blocks.decoration;

import com.cronicasaetherium.mod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Placa Rúnica - Bloco ritual que pode armazenar e exibir itens
 * 
 * Este bloco funciona como um pedestal ritual que pode armazenar um único item
 * e exibi-lo visualmente no topo. É essencial para a ativação de portais mágicos,
 * servindo como ponto de foco para ingredientes rituais.
 * 
 * Funcionalidades:
 * - Armazena um item (clique direito para colocar/retirar)
 * - Item é visível no topo do bloco (renderização 3D)
 * - Emite luz crescente baseada no valor/raridade do item armazenado
 * - Partículas mágicas quando contém itens raros
 * - Componente essencial para estruturas de portal 4x4
 * 
 * Interações:
 * - Clique direito com item: coloca item na placa
 * - Clique direito com mão vazia: retira item da placa
 * - Redstone: emite sinal se contém item
 * 
 * Usado no Portal Mágico: as quatro placas nos cantos precisam conter
 * ingredientes específicos para ativar o portal do Crisol Arcano.
 */
public class RunicPlateBlock extends BaseEntityBlock {
    
    // Formato da placa: menor que um bloco completo, como um pedestal baixo
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);
    
    public RunicPlateBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.5F, 6.0F) // Resistente mas não indestrutível
            .sound(SoundType.STONE)
            .lightLevel(state -> 4) // Luz base da placa
            .noOcclusion() // Permite ver através das bordas
        );
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RunicPlateBlockEntity runicPlate) {
                // Clique com mão vazia: tenta retirar item
                ItemStack storedItem = runicPlate.getStoredItem();
                if (!storedItem.isEmpty()) {
                    // Dá o item ao jogador ou dropa no mundo se inventário cheio
                    if (player.getInventory().add(storedItem)) {
                        runicPlate.setStoredItem(ItemStack.EMPTY);
                        runicPlate.setChanged();
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RunicPlateBlockEntity runicPlate) {
                ItemStack storedItem = runicPlate.getStoredItem();
                
                if (storedItem.isEmpty() && !stack.isEmpty()) {
                    // Placa vazia e jogador tem item: armazena item
                    ItemStack toStore = stack.copy();
                    toStore.setCount(1); // Apenas um item por placa
                    
                    runicPlate.setStoredItem(toStore);
                    stack.shrink(1); // Remove um item da mão do jogador
                    runicPlate.setChanged();
                    
                    return ItemInteractionResult.SUCCESS;
                } else if (!storedItem.isEmpty() && stack.isEmpty()) {
                    // Placa tem item e jogador com mão vazia: remove item
                    if (player.getInventory().add(storedItem)) {
                        runicPlate.setStoredItem(ItemStack.EMPTY);
                        runicPlate.setChanged();
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    /**
     * Emite sinal de redstone se contém item
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RunicPlateBlockEntity runicPlate) {
            return runicPlate.getStoredItem().isEmpty() ? 0 : 15;
        }
        return 0;
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunicPlateBlockEntity(pos, state);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.RUNIC_PLATE.get(),
            level.isClientSide ? RunicPlateBlockEntity::clientTick : RunicPlateBlockEntity::serverTick);
    }
    
    /**
     * Quando o bloco é quebrado, dropa o item armazenado
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RunicPlateBlockEntity runicPlate) {
                ItemStack storedItem = runicPlate.getStoredItem();
                if (!storedItem.isEmpty()) {
                    popResource(level, pos, storedItem);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}