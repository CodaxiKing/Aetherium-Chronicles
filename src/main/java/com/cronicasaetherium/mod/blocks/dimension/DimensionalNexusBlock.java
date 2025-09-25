package com.cronicasaetherium.mod.blocks.dimension;

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
 * Nexus Dimensional - Bloco central para acesso às dimensões dos chefes
 * 
 * Este bloco representa a maestria do jogador sobre as dimensões e serve como
 * um hub central para acessar os domínios dos chefes finais. Ele pode ser
 * "sintonizado" usando Chaves de Sintonização específicas para cada dimensão.
 * 
 * Funcionalidades:
 * - Aceita Chaves de Sintonização para diferentes dimensões (Forja Morta, Sanctum Selado, etc.)
 * - Muda sua textura/aparência baseada na dimensão sintonizada
 * - Gera portais estáveis na frente do bloco quando ativado
 * - Mantém estado da sintonização até que uma nova chave seja usada
 * - Pode ser desativado clicando com mão vazia
 * 
 * Estados do Nexus:
 * - INACTIVE: Estado neutro, sem sintonização
 * - FORJA_MORTA: Sintonizado para a dimensão tecnológica dos chefes
 * - SANCTUM_SELADO: Sintonizado para a dimensão mágica dos chefes
 * - VOID_NEXUS: Sintonizado para dimensões especiais (futuro)
 * 
 * Mecânica de uso:
 * 1. Jogador usa Chave de Sintonização no Nexus
 * 2. Nexus consome a chave e muda para o estado correspondente
 * 3. Portal 2x3 é gerado na frente do Nexus
 * 4. Jogador pode usar mão vazia no Nexus para fechar portal
 * 5. Nova chave pode ser usada para trocar de dimensão
 */
public class DimensionalNexusBlock extends BaseEntityBlock {
    
    // Forma do bloco: um pouco maior que um bloco normal para dar imponência
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    
    public DimensionalNexusBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(8.0F, 15.0F) // Muito resistente
            .sound(SoundType.METAL)
            .lightLevel(state -> 8) // Luz moderada constante
            .requiresCorrectToolForDrops()
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
            if (blockEntity instanceof DimensionalNexusBlockEntity nexus) {
                // Mão vazia: fecha portal se ativo ou mostra status
                if (nexus.hasActivePortal()) {
                    nexus.closePortal();
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                        "§6Portal dimensional fechado."), true);
                } else {
                    // Mostra status atual do Nexus
                    String currentTuning = nexus.getCurrentDimension();
                    if (currentTuning.equals("none")) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                            "§7Nexus Dimensional está inativo. Use uma Chave de Sintonização."), true);
                    } else {
                        player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                            "§bNexus sintonizado para: §e" + currentTuning), true);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DimensionalNexusBlockEntity nexus) {
                
                // Verifica se o item é uma Chave de Sintonização válida
                String tuningKey = getTuningKeyType(stack);
                if (!tuningKey.equals("none")) {
                    // É uma chave válida! Sintonia o Nexus
                    nexus.tuneToDirection(tuningKey);
                    nexus.openPortal();
                    
                    // Consome a chave
                    stack.shrink(1);
                    
                    // Feedback para o jogador
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                        "§dNexus Dimensional sintonizado para §e" + tuningKey + "§d!"), true);
                    
                    // Efeitos sonoros e visuais
                    level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, 
                        net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                    
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    /**
     * Identifica o tipo de Chave de Sintonização baseada no item
     */
    private String getTuningKeyType(ItemStack stack) {
        String itemId = stack.getItem().toString();
        
        // Verifica cada tipo de chave dimensional
        if (itemId.contains("forja_morta_key")) {
            return "Forja Morta";
        } else if (itemId.contains("sanctum_selado_key")) {
            return "Sanctum Selado"; 
        } else if (itemId.contains("void_nexus_key")) {
            return "Nexus do Vazio";
        }
        
        return "none"; // Não é uma chave válida
    }
    
    /**
     * Emite sinal de redstone baseado no estado da sintonização
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DimensionalNexusBlockEntity nexus) {
            if (nexus.hasActivePortal()) {
                return 15; // Sinal máximo quando portal está ativo
            } else if (!nexus.getCurrentDimension().equals("none")) {
                return 7; // Sinal moderado quando sintonizado mas portal fechado
            }
        }
        return 0; // Sem sinal quando inativo
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DimensionalNexusBlockEntity(pos, state);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.DIMENSIONAL_NEXUS.get(),
            level.isClientSide ? DimensionalNexusBlockEntity::clientTick : DimensionalNexusBlockEntity::serverTick);
    }
    
    /**
     * Quando o bloco é quebrado, fecha qualquer portal ativo
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DimensionalNexusBlockEntity nexus) {
                nexus.closePortal(); // Fecha portal se ativo
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}