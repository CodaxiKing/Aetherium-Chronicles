package com.cronicasaetherium.mod.blocks.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Salgueiro Torcido Polido - Bloco decorativo refinado para construção de portais
 * 
 * Esta é uma versão processada e polida da madeira de Salgueiro Torcido,
 * criada especificamente para rituais mágicos e construção de estruturas portais.
 * É o componente principal para a estrutura do Portal Mágico ao Crisol Arcano.
 * 
 * Características:
 * - Mais resistente que madeira comum (4.0F vs 2.0F)
 * - Emite luz suave (nível 2) devido ao polimento com essências mágicas
 * - Textura refinada com veios dourados arcanos
 * - Som cristalino quando quebrado ou pisado
 * - Fundamental para estruturas rituais 4x4
 * 
 * Receita sugerida:
 * - 4 Pranchas de Salgueiro Torcido + 1 Essência Espiritual = 4 Salgueiro Polido
 */
public class PolishedTwistedWillowBlock extends Block {
    
    public PolishedTwistedWillowBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(4.0F, 4.0F) // Mais resistente que madeira comum
            .sound(SoundType.WOOD)
            .lightLevel(state -> 2) // Brilho suave devido ao polimento mágico
            .ignitedByLava() // Ainda é madeira, pode pegar fogo
        );
    }
    
    /**
     * Verifica se este bloco pode ser usado como parte de uma estrutura portal
     * Este método é usado pela lógica de validação do portal mágico
     */
    public boolean isValidPortalStructure() {
        return true;
    }
    
    /**
     * Sobrescreve a luminosidade para garantir que a luz seja consistente
     * O polimento com essências faz o bloco emitir uma luz dourada suave
     */
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 2;
    }
}