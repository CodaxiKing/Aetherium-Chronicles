package com.cronicasaetherium.mod.blocks.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Portal do Crisol Arcano - Bloco que forma o portal ativo para a dimensão mágica
 * 
 * Este bloco é criado dinamicamente quando o Portal Mágico é ativado através
 * do ritual com Varinha da Floresta. Não pode ser obtido ou colocado manualmente.
 * 
 * Características:
 * - Intangível (pode passar através dele)
 * - Textura animada nebulosa roxa pulsante
 * - Emite luz máxima (15)
 * - Partículas mágicas constantes
 * - Teleporta entidades para o Crisol Arcano
 * - Som ambiente arcano
 * 
 * Funcionalidade:
 * - Detecta entidades que colidem e as teleporta
 * - Verifica se a estrutura portal ainda está válida
 * - Se a estrutura for quebrada, o portal se desativa
 * - Efeitos visuais e sonoros contínuos
 */
public class CrisolArcanoPortalBlock extends Block {
    
    public CrisolArcanoPortalBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(-1.0F, 3600000.0F) // Indestrutível como portal do Nether
            .sound(SoundType.GLASS)
            .lightLevel(state -> 15) // Luz máxima
            .noCollision() // Entidades podem passar através
            .noOcclusion() // Transparente para iluminação
        );
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }
    
    /**
     * Chamado quando uma entidade entra no bloco do portal
     * Inicia o processo de teleportação para o Crisol Arcano
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isAlive()) {
            // TODO: Implementar teleportação para dimensão do Crisol Arcano
            // Por ora, apenas reproduz som e efeitos
            
            level.playSound(null, pos, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 
                0.5F, level.random.nextFloat() * 0.4F + 0.8F);
            
            // Partículas de teleportação
            if (level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = pos.getX() + level.random.nextDouble();
                    double d1 = pos.getY() + level.random.nextDouble();
                    double d2 = pos.getZ() + level.random.nextDouble();
                    double d3 = (level.random.nextDouble() - 0.5D) * 0.5D;
                    double d4 = (level.random.nextDouble() - 0.5D) * 0.5D;
                    double d5 = (level.random.nextDouble() - 0.5D) * 0.5D;
                    
                    serverLevel.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, d3, d4, d5, 0.0D);
                }
            }
            
            // TODO: Teleportar entidade
            // entity.changeDimension(CrisolArcanoLevel);
        }
    }
    
    /**
     * Efeitos de partículas contínuas do portal
     * Chamado aleatoriamente para criar o efeito visual constante
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Partículas roxas flutuando
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() + random.nextDouble();
            double d2 = pos.getZ() + random.nextDouble();
            double d3 = (random.nextDouble() - 0.5D) * 0.25D;
            double d4 = (random.nextDouble() - 0.5D) * 0.25D;
            double d5 = (random.nextDouble() - 0.5D) * 0.25D;
            
            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
        
        // Som ambiente ocasional
        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, 
                random.nextFloat() * 0.4F + 0.8F, false);
        }
    }
    
    /**
     * Verifica se a estrutura do portal ainda é válida
     * Se não for, remove o bloco do portal
     */
    public boolean isValidPortalStructure(Level level, BlockPos portalCenter) {
        // Verifica estrutura 4x4 centrada no portal
        BlockPos basePos = portalCenter.offset(-2, 0, -2);
        
        // Verifica os blocos da estrutura
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                BlockPos checkPos = basePos.offset(x, 0, z);
                
                // Centro (2x2) deve ser ar ou portal
                if (x >= 1 && x <= 2 && z >= 1 && z <= 2) {
                    continue; // Pula verificação do centro
                }
                
                // Cantos devem ser Placas Rúnicas
                if ((x == 0 && z == 0) || (x == 0 && z == 3) || 
                    (x == 3 && z == 0) || (x == 3 && z == 3)) {
                    if (!(level.getBlockState(checkPos).getBlock() instanceof 
                          com.cronicasaetherium.mod.blocks.decoration.RunicPlateBlock)) {
                        return false;
                    }
                } else {
                    // Outras posições devem ser Salgueiro Polido
                    if (!(level.getBlockState(checkPos).getBlock() instanceof 
                          com.cronicasaetherium.mod.blocks.decoration.PolishedTwistedWillowBlock)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Tick do servidor para verificar estrutura e manter o portal
     */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            // Agenda verificação da estrutura
            level.scheduleTick(pos, this, 40); // Verifica a cada 2 segundos
        }
    }
}