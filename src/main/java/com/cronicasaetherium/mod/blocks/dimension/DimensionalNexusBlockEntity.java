package com.cronicasaetherium.mod.blocks.dimension;

import com.cronicasaetherium.mod.registry.ModBlockEntities;
import com.cronicasaetherium.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Block Entity do Nexus Dimensional - Gerencia estado e lógica do portal dimensional
 * 
 * Esta classe mantém o estado interno do Nexus Dimensional, incluindo:
 * - Qual dimensão está atualmente sintonizada
 * - Se há um portal ativo e sua localização
 * - Lógica de geração e destruição de portais
 * - Efeitos visuais e sonoros baseados no estado
 * 
 * Estados persistentes:
 * - currentDimension: Dimensão para a qual está sintonizado
 * - hasActivePortal: Se há um portal atualmente aberto
 * - portalBlocks: Lista de posições dos blocos de portal criados
 * - animationTicks: Para efeitos visuais contínuos
 */
public class DimensionalNexusBlockEntity extends BlockEntity {
    
    private String currentDimension = "none";
    private boolean hasActivePortal = false;
    private List<BlockPos> portalBlocks = new ArrayList<>();
    private int animationTicks = 0;
    private Direction portalDirection = Direction.NORTH; // Direção padrão do portal
    
    public DimensionalNexusBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DIMENSIONAL_NEXUS.get(), pos, blockState);
    }
    
    /**
     * Sintonia o Nexus para uma dimensão específica
     */
    public void tuneToDirection(String dimension) {
        // Se já estava sintonizado para outra dimensão, fecha portal anterior
        if (hasActivePortal) {
            closePortal();
        }
        
        this.currentDimension = dimension;
        setChanged();
        
        // Sincroniza com cliente para mudanças visuais
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
    
    /**
     * Abre um portal na frente do Nexus
     */
    public void openPortal() {
        if (level == null || level.isClientSide || currentDimension.equals("none")) {
            return;
        }
        
        // Fecha portal anterior se existir
        if (hasActivePortal) {
            closePortal();
        }
        
        // Determina a posição do portal (2x3 na frente do Nexus)
        BlockPos portalStart = worldPosition.relative(portalDirection);
        
        // Verifica se há espaço para o portal
        if (!canCreatePortalAt(portalStart)) {
            // TODO: Mensagem de erro para o jogador
            return;
        }
        
        // Cria o portal 2x3
        createPortalStructure(portalStart);
        
        hasActivePortal = true;
        setChanged();
        
        // Efeitos sonoros
        level.playSound(null, worldPosition, SoundEvents.END_PORTAL_SPAWN, 
            SoundSource.BLOCKS, 1.0F, 1.0F);
    }
    
    /**
     * Fecha o portal ativo e remove todos os blocos de portal
     */
    public void closePortal() {
        if (level == null || !hasActivePortal) {
            return;
        }
        
        // Remove todos os blocos de portal
        for (BlockPos portalPos : portalBlocks) {
            if (level.getBlockState(portalPos).getBlock().toString().contains("portal")) {
                level.setBlock(portalPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }
        }
        
        portalBlocks.clear();
        hasActivePortal = false;
        setChanged();
        
        // Efeito sonoro de fechamento
        if (!level.isClientSide) {
            level.playSound(null, worldPosition, SoundEvents.BEACON_DEACTIVATE, 
                SoundSource.BLOCKS, 0.8F, 0.8F);
        }
    }
    
    /**
     * Verifica se é possível criar um portal na posição especificada
     */
    private boolean canCreatePortalAt(BlockPos start) {
        if (level == null) return false;
        
        // Verifica espaço 2x3 (2 largura, 3 altura)
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 3; y++) {
                BlockPos checkPos = start.offset(x, y, 0);
                if (!level.getBlockState(checkPos).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Cria a estrutura física do portal
     */
    private void createPortalStructure(BlockPos start) {
        if (level == null) return;
        
        portalBlocks.clear();
        
        // Determina qual bloco de portal usar baseado na dimensão
        BlockState portalBlockState = getPortalBlockForDimension(currentDimension);
        
        // Cria portal 2x3
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 3; y++) {
                BlockPos portalPos = start.offset(x, y, 0);
                level.setBlock(portalPos, portalBlockState, 3);
                portalBlocks.add(portalPos);
            }
        }
    }
    
    /**
     * Retorna o tipo de bloco de portal apropriado para a dimensão
     */
    private BlockState getPortalBlockForDimension(String dimension) {
        return switch (dimension) {
            case "Forja Morta" -> ModBlocks.FORJA_MORTA_PORTAL.get().defaultBlockState();
            case "Sanctum Selado" -> ModBlocks.SANCTUM_SELADO_PORTAL.get().defaultBlockState();
            case "Nexus do Vazio" -> ModBlocks.VOID_NEXUS_PORTAL.get().defaultBlockState();
            default -> ModBlocks.CRISOL_ARCANO_PORTAL.get().defaultBlockState(); // Fallback
        };
    }
    
    // ================================
    // GETTERS E ESTADO
    // ================================
    
    public String getCurrentDimension() {
        return currentDimension;
    }
    
    public boolean hasActivePortal() {
        return hasActivePortal;
    }
    
    public int getAnimationTicks() {
        return animationTicks;
    }
    
    /**
     * Calcula a intensidade luminosa baseada no estado do Nexus
     */
    public int getLightLevel() {
        if (hasActivePortal) {
            return 15; // Luz máxima quando portal está ativo
        } else if (!currentDimension.equals("none")) {
            return 10; // Luz moderada quando sintonizado
        }
        return 6; // Luz base
    }
    
    // ================================
    // SERIALIZAÇÃO NBT
    // ================================
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putString("CurrentDimension", currentDimension);
        tag.putBoolean("HasActivePortal", hasActivePortal);
        tag.putInt("AnimationTicks", animationTicks);
        tag.putInt("PortalDirection", portalDirection.ordinal());
        
        // Salva posições dos blocos de portal
        if (!portalBlocks.isEmpty()) {
            long[] portalPositions = new long[portalBlocks.size()];
            for (int i = 0; i < portalBlocks.size(); i++) {
                portalPositions[i] = portalBlocks.get(i).asLong();
            }
            tag.putLongArray("PortalBlocks", portalPositions);
        }
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        currentDimension = tag.getString("CurrentDimension");
        hasActivePortal = tag.getBoolean("HasActivePortal");
        animationTicks = tag.getInt("AnimationTicks");
        portalDirection = Direction.values()[tag.getInt("PortalDirection")];
        
        // Carrega posições dos blocos de portal
        portalBlocks.clear();
        if (tag.contains("PortalBlocks")) {
            long[] portalPositions = tag.getLongArray("PortalBlocks");
            for (long pos : portalPositions) {
                portalBlocks.add(BlockPos.of(pos));
            }
        }
    }
    
    // ================================
    // SINCRONIZAÇÃO CLIENTE-SERVIDOR
    // ================================
    
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    // ================================
    // TICKERS - EFEITOS CONTÍNUOS
    // ================================
    
    /**
     * Ticker do lado do servidor - Lógica de jogo
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state, DimensionalNexusBlockEntity blockEntity) {
        blockEntity.animationTicks++;
        
        // Verifica integridade do portal a cada 5 segundos
        if (blockEntity.hasActivePortal && blockEntity.animationTicks % 100 == 0) {
            blockEntity.validatePortalIntegrity();
        }
    }
    
    /**
     * Ticker do lado do cliente - Efeitos visuais
     */
    public static void clientTick(Level level, BlockPos pos, BlockState state, DimensionalNexusBlockEntity blockEntity) {
        blockEntity.animationTicks++;
        
        // Partículas baseadas no estado do Nexus
        if (!blockEntity.currentDimension.equals("none")) {
            // TODO: Adicionar partículas específicas para cada dimensão
            // - Forja Morta: partículas de lava/fogo
            // - Sanctum Selado: partículas arcanas roxas
            // - Nexus do Vazio: partículas caóticas multicolor
        }
    }
    
    /**
     * Valida se todos os blocos do portal ainda existem
     */
    private void validatePortalIntegrity() {
        if (level == null || !hasActivePortal) return;
        
        boolean portalIntact = true;
        for (BlockPos portalPos : portalBlocks) {
            if (!level.getBlockState(portalPos).getBlock().toString().contains("portal")) {
                portalIntact = false;
                break;
            }
        }
        
        if (!portalIntact) {
            // Portal foi danificado, fecha completamente
            closePortal();
        }
    }
}