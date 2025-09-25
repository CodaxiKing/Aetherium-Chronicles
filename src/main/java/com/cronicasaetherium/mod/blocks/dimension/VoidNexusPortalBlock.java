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
 * Portal do Nexus do Vazio - Portal para dimensões especiais caóticas
 * 
 * Este portal é criado pelo Nexus Dimensional quando sintonizado com chaves
 * especiais do vazio. Representa o caos entre dimensões e leva a regiões
 * onde a realidade se torna instável.
 * 
 * Características visuais:
 * - Cor caótica que muda constantemente
 * - Partículas multicolor erráticas
 * - Sons distorcidos e glitchados
 * - Textura que pulsa e se distorce
 */
public class VoidNexusPortalBlock extends Block {
    
    public VoidNexusPortalBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(-1.0F, 3600000.0F) // Indestrutível
            .sound(SoundType.GLASS)
            .lightLevel(state -> 15) // Luz máxima mas instável
            .noCollision()
            .noOcclusion()
        );
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isAlive()) {
            // TODO: Teleportar para Vazio Intersticial
            level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 
                1.2F, 0.1F + level.random.nextFloat() * 1.8F);
        }
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Partículas caóticas multicolor
        for (int i = 0; i < 6; ++i) {
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() + random.nextDouble();
            double d2 = pos.getZ() + random.nextDouble();
            double d3 = (random.nextDouble() - 0.5) * 0.5;
            double d4 = (random.nextDouble() - 0.5) * 0.5;
            double d5 = (random.nextDouble() - 0.5) * 0.5;
            
            // Mistura diferentes tipos de partículas para efeito caótico
            switch (random.nextInt(4)) {
                case 0 -> level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
                case 1 -> level.addParticle(ParticleTypes.WITCH, d0, d1, d2, d3, d4, d5);
                case 2 -> level.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1, d2, d3, d4, d5);
                case 3 -> level.addParticle(ParticleTypes.REVERSE_PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }
        
        // Sons caóticos e distorcidos
        if (random.nextInt(60) == 0) {
            float pitch = 0.2F + random.nextFloat() * 2.0F; // Pitch muito variado
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.6F, pitch, false);
        }
    }
}