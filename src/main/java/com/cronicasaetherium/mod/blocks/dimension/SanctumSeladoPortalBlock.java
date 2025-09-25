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
 * Portal do Sanctum Selado - Portal para a dimensão mágica dos chefes
 * 
 * Este portal é criado pelo Nexus Dimensional quando sintonizado com a
 * Chave do Sanctum Selado. Leva para uma dimensão de magia pura onde
 * antigos guardiões arcanos protegem segredos místicos.
 * 
 * Características visuais:
 * - Cor roxa mística (magia/arcano)
 * - Partículas encantadas e místicas
 * - Som de magia antiga
 * - Textura etérea e translúcida
 */
public class SanctumSeladoPortalBlock extends Block {
    
    public SanctumSeladoPortalBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(-1.0F, 3600000.0F) // Indestrutível
            .sound(SoundType.GLASS)
            .lightLevel(state -> 14)
            .noCollision()
            .noOcclusion()
        );
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isAlive()) {
            // TODO: Teleportar para Sanctum Selado
            level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 
                1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Partículas mágicas
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() + random.nextDouble();
            double d2 = pos.getZ() + random.nextDouble();
            
            level.addParticle(ParticleTypes.ENCHANT, d0, d1, d2, 
                (random.nextDouble() - 0.5) * 0.2, 0.1, (random.nextDouble() - 0.5) * 0.2);
            level.addParticle(ParticleTypes.WITCH, d0, d1, d2, 0, 0.05, 0);
        }
        
        // Som místico ocasional
        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.4F, 1.8F, false);
        }
    }
}