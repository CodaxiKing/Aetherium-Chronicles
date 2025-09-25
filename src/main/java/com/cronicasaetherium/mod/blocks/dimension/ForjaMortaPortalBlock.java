package com.cronicasaetherium.mod.blocks.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Portal da Forja Morta - Portal para a dimensão tecnológica dos chefes
 * 
 * Este portal é criado pelo Nexus Dimensional quando sintonizado com a
 * Chave da Forja Morta. Leva para uma dimensão industrial decadente onde
 * antigas máquinas ainda funcionam e chefes mecânicos aguardam.
 * 
 * Características visuais:
 * - Cor laranja-avermelhada (forja/tecnologia)
 * - Partículas de lava e faíscas
 * - Som de maquinário industrial
 * - Textura que lembra metal fundido
 */
public class ForjaMortaPortalBlock extends Block {
    
    public ForjaMortaPortalBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(-1.0F, 3600000.0F) // Indestrutível
            .sound(SoundType.METAL)
            .lightLevel(state -> 12)
            .noCollision()
            .noOcclusion()
        );
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isAlive()) {
            // TODO: Teleportar para Forja Morta
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 
                0.8F, 0.5F + level.random.nextFloat() * 0.5F);
        }
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Partículas de lava e faíscas
        for (int i = 0; i < 3; ++i) {
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() + random.nextDouble();
            double d2 = pos.getZ() + random.nextDouble();
            
            level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0, 0.1, 0);
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0.05, 0);
        }
        
        // Som industrial ocasional
        if (random.nextInt(80) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.3F, 1.5F, false);
        }
    }
}