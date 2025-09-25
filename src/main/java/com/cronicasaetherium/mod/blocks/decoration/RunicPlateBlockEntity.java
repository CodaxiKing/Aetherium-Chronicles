package com.cronicasaetherium.mod.blocks.decoration;

import com.cronicasaetherium.mod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block Entity da Placa Rúnica - Gerencia armazenamento e renderização de itens
 * 
 * Esta classe gerencia o estado interno da Placa Rúnica, incluindo:
 * - Armazenamento persistente do item colocado na placa
 * - Sincronização cliente-servidor para renderização 3D do item
 * - Efeitos visuais baseados no tipo de item armazenado
 * - Lógica de partículas e iluminação dinâmica
 * 
 * Funcionalidades técnicas:
 * - Serialização NBT para persistência mundial
 * - Packet de sincronização para cliente
 * - Ticker para efeitos contínuos
 * - Cálculo de intensidade luminosa baseada no item
 */
public class RunicPlateBlockEntity extends BlockEntity {
    
    private ItemStack storedItem = ItemStack.EMPTY;
    private int animationTicks = 0;
    
    public RunicPlateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RUNIC_PLATE.get(), pos, blockState);
    }
    
    /**
     * Obtém o item atualmente armazenado na placa
     */
    public ItemStack getStoredItem() {
        return storedItem;
    }
    
    /**
     * Define o item a ser armazenado na placa
     * Automaticamente marca para sincronização e save
     */
    public void setStoredItem(ItemStack item) {
        this.storedItem = item.copy();
        setChanged();
        
        // Sincroniza com cliente para renderização
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
    
    /**
     * Verifica se a placa contém um item específico
     * Usado pela lógica de ativação de portais
     */
    public boolean hasItem(ItemStack requiredItem) {
        return ItemStack.isSameItemSameTags(storedItem, requiredItem);
    }
    
    /**
     * Verifica se a placa está vazia
     */
    public boolean isEmpty() {
        return storedItem.isEmpty();
    }
    
    /**
     * Calcula a intensidade luminosa baseada no item armazenado
     * Itens mais raros emitem mais luz
     */
    public int getLightLevel() {
        if (storedItem.isEmpty()) {
            return 4; // Luz base da placa
        }
        
        // Itens raros emitem mais luz
        if (storedItem.isEnchanted()) {
            return 12;
        }
        if (storedItem.getRarity().name().equals("EPIC")) {
            return 10;
        }
        if (storedItem.getRarity().name().equals("RARE")) {
            return 8;
        }
        
        return 6; // Luz padrão com item comum
    }
    
    // ================================
    // SERIALIZAÇÃO NBT
    // ================================
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        if (!storedItem.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            storedItem.save(registries, itemTag);
            tag.put("StoredItem", itemTag);
        }
        
        tag.putInt("AnimationTicks", animationTicks);
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        if (tag.contains("StoredItem")) {
            storedItem = ItemStack.parse(registries, tag.getCompound("StoredItem")).orElse(ItemStack.EMPTY);
        } else {
            storedItem = ItemStack.EMPTY;
        }
        
        animationTicks = tag.getInt("AnimationTicks");
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
    public static void serverTick(Level level, BlockPos pos, BlockState state, RunicPlateBlockEntity blockEntity) {
        blockEntity.animationTicks++;
        
        // A cada 20 ticks (1 segundo), verifica se precisa sincronizar
        if (blockEntity.animationTicks % 20 == 0) {
            blockEntity.setChanged();
        }
    }
    
    /**
     * Ticker do lado do cliente - Efeitos visuais
     */
    public static void clientTick(Level level, BlockPos pos, BlockState state, RunicPlateBlockEntity blockEntity) {
        blockEntity.animationTicks++;
        
        // Só cria partículas se tem item armazenado
        if (!blockEntity.storedItem.isEmpty()) {
            // TODO: Adicionar partículas mágicas flutuando sobre o item
            // Frequência baseada na raridade do item
            
            if (blockEntity.animationTicks % 10 == 0 && level.random.nextFloat() < 0.3f) {
                // Partículas douradas para itens encantados
                if (blockEntity.storedItem.isEnchanted()) {
                    // Código para partículas douradas
                }
                
                // Partículas roxas para itens épicos
                if (blockEntity.storedItem.getRarity().name().equals("EPIC")) {
                    // Código para partículas roxas
                }
            }
        }
    }
    
    /**
     * Obtém ticks de animação para renderização rotativa do item
     */
    public int getAnimationTicks() {
        return animationTicks;
    }
}