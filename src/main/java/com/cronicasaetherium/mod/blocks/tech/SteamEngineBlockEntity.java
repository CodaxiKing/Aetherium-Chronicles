package com.cronicasaetherium.mod.blocks.tech;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * BlockEntity do Motor a Vapor - Coração do sistema tecnológico Tier 1
 * 
 * O Motor a Vapor é a primeira fonte de energia do mod, convertendo combustível
 * sólido (carvão, madeira) + água em Forge Energy (FE).
 * 
 * Sistemas integrados:
 * - Armazenamento de combustível (slot de entrada)
 * - Tanque de água (sistema de fluidos futuro)
 * - Geração contínua de FE enquanto há combustível e água
 * - Mecânica de aquecimento (tempo para aquecer/esfriar)
 * - Persistência de dados via NBT
 * - Sincronização client-server para efeitos visuais
 * 
 * Esta BlockEntity serve como fundação para todo o sistema tecnológico,
 * fornecendo energia para Crushers, Furnaces e outras máquinas Tier 1.
 */
public class SteamEngineBlockEntity extends BlockEntity {
    
    // Constantes de configuração da máquina
    private static final int MAX_ENERGY = 50000; // Capacidade máxima de energia
    private static final int ENERGY_GENERATION = 20; // FE gerado por tick
    private static final int FUEL_BURN_TIME = 1600; // Ticks de queima por carvão (80 segundos)
    private static final int WARMUP_TIME = 200; // Ticks para aquecer (10 segundos)
    private static final int COOLDOWN_TIME = 400; // Ticks para esfriar (20 segundos)
    
    // Estado interno da máquina
    private int energyStored = 0; // Energia atualmente armazenada
    private int fuelBurnTime = 0; // Tempo restante do combustível atual
    private int maxBurnTime = 0; // Tempo total de queima do combustível atual
    private int warmupProgress = 0; // Progresso de aquecimento (0-200)
    private int waterLevel = 0; // Nível de água (0-1000, futuro sistema de fluidos)
    private boolean isActive = false; // Se o motor está funcionando
    private boolean isWarmedUp = false; // Se o motor está aquecido
    
    // Inventário para combustível
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // Marca a BlockEntity como modificada
            syncToClient(); // Sincroniza com o cliente
        }
        
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) { // Slot de combustível
                return isFuel(stack);
            } else if (slot == 1) { // Slot de água (bucket de água)
                return stack.getItem() == Items.WATER_BUCKET;
            }
            return false;
        }
    };
    
    /**
     * Construtor da BlockEntity
     */
    public SteamEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Construtor simplificado usando o tipo registrado
     */
    public SteamEngineBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.STEAM_ENGINE.get(), pos, state);
    }
    
    /**
     * Método principal de processamento - executado a cada tick
     * 
     * Gerencia toda a lógica do motor:
     * - Aquecimento/resfriamento
     * - Queima de combustível
     * - Geração de energia
     * - Consumo de água
     */
    public void tick() {
        if (level == null || level.isClientSide()) {
            return; // Processa apenas no servidor
        }
        
        boolean wasActive = isActive;
        
        // Verifica se pode operar (tem combustível e água)
        if (canOperate()) {
            // Inicia queima se não há combustível ativo
            if (fuelBurnTime <= 0) {
                startBurning();
            }
            
            // Processo de aquecimento
            if (!isWarmedUp) {
                warmUp();
            } else {
                // Gera energia se aquecido
                generateEnergy();
            }
            
            // Consome combustível
            if (fuelBurnTime > 0) {
                fuelBurnTime--;
                isActive = true;
            }
            
        } else {
            // Para de funcionar e resfria
            isActive = false;
            coolDown();
        }
        
        // Sincroniza com cliente se mudou estado
        if (wasActive != isActive) {
            setChanged();
            syncToClient();
        }
    }
    
    /**
     * Verifica se o motor pode operar
     * 
     * @return true se pode operar
     */
    private boolean canOperate() {
        // Precisa ter combustível ou estar queimando
        boolean hasFuel = fuelBurnTime > 0 || !inventory.getStackInSlot(0).isEmpty();
        
        // Precisa ter água (simplificado por enquanto)
        boolean hasWater = waterLevel > 0 || !inventory.getStackInSlot(1).isEmpty();
        
        // Precisa ter espaço para energia
        boolean hasEnergySpace = energyStored < MAX_ENERGY;
        
        return hasFuel && hasWater && hasEnergySpace;
    }
    
    /**
     * Inicia a queima de um novo combustível
     */
    private void startBurning() {
        ItemStack fuelStack = inventory.getStackInSlot(0);
        if (!fuelStack.isEmpty() && isFuel(fuelStack)) {
            // Consome um item de combustível
            int burnTime = getBurnTime(fuelStack);
            fuelStack.shrink(1);
            inventory.setStackInSlot(0, fuelStack);
            
            // Inicia a queima
            fuelBurnTime = burnTime;
            maxBurnTime = burnTime;
            
            // Consome água (simplificado)
            ItemStack waterStack = inventory.getStackInSlot(1);
            if (waterStack.getItem() == Items.WATER_BUCKET) {
                inventory.setStackInSlot(1, new ItemStack(Items.BUCKET));
                waterLevel = 1000; // Adiciona água
            }
            
            CronicasAetherium.LOGGER.info("Motor a Vapor iniciou queima na posição {}", getBlockPos());
        }
    }
    
    /**
     * Processo de aquecimento do motor
     */
    private void warmUp() {
        if (fuelBurnTime > 0) {
            warmupProgress++;
            if (warmupProgress >= WARMUP_TIME) {
                isWarmedUp = true;
                CronicasAetherium.LOGGER.info("Motor a Vapor aqueceu na posição {}", getBlockPos());
            }
        }
    }
    
    /**
     * Processo de resfriamento do motor
     */
    private void coolDown() {
        if (warmupProgress > 0) {
            warmupProgress = Math.max(0, warmupProgress - 2); // Resfria mais devagar
            if (warmupProgress == 0) {
                isWarmedUp = false;
            }
        }
    }
    
    /**
     * Gera energia do motor
     */
    private void generateEnergy() {
        if (isWarmedUp && fuelBurnTime > 0 && waterLevel > 0) {
            int generated = Math.min(ENERGY_GENERATION, MAX_ENERGY - energyStored);
            energyStored += generated;
            
            // Consome água gradualmente
            if (level.getGameTime() % 20 == 0) { // A cada segundo
                waterLevel = Math.max(0, waterLevel - 1);
            }
        }
    }
    
    /**
     * Verifica se um item é combustível válido
     * 
     * @param stack ItemStack a verificar
     * @return true se é combustível
     */
    private boolean isFuel(ItemStack stack) {
        return stack.getItem() == Items.COAL || 
               stack.getItem() == Items.CHARCOAL ||
               stack.getItem() == Items.OAK_PLANKS ||
               stack.getItem() == Items.STICK;
    }
    
    /**
     * Obtém o tempo de queima de um combustível
     * 
     * @param stack ItemStack do combustível
     * @return Tempo de queima em ticks
     */
    private int getBurnTime(ItemStack stack) {
        if (stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL) {
            return FUEL_BURN_TIME; // 80 segundos
        } else if (stack.getItem() == Items.OAK_PLANKS) {
            return FUEL_BURN_TIME / 4; // 20 segundos
        } else if (stack.getItem() == Items.STICK) {
            return FUEL_BURN_TIME / 8; // 10 segundos
        }
        return 0;
    }
    
    /**
     * Extrai energia do motor
     * 
     * @param maxExtract Energia máxima a extrair
     * @return Energia efetivamente extraída
     */
    public int extractEnergy(int maxExtract) {
        int extracted = Math.min(maxExtract, energyStored);
        energyStored -= extracted;
        if (extracted > 0) {
            setChanged();
        }
        return extracted;
    }
    
    // ================================
    // GETTERS PARA GUI E SINCRONIZAÇÃO
    // ================================
    
    public int getEnergyStored() { return energyStored; }
    public int getMaxEnergy() { return MAX_ENERGY; }
    public int getFuelBurnTime() { return fuelBurnTime; }
    public int getMaxBurnTime() { return maxBurnTime; }
    public int getWarmupProgress() { return (warmupProgress * 100) / WARMUP_TIME; }
    public int getWaterLevel() { return waterLevel; }
    public boolean isActive() { return isActive; }
    public boolean isWarmedUp() { return isWarmedUp; }
    public ItemStackHandler getInventory() { return inventory; }
    
    /**
     * Sincroniza dados com o cliente
     */
    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            // TODO: Implementar sincronização quando necessário
            // level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
    
    // ================================
    // PERSISTÊNCIA DE DADOS (NBT)
    // ================================
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putInt("energy", energyStored);
        tag.putInt("fuel_burn_time", fuelBurnTime);
        tag.putInt("max_burn_time", maxBurnTime);
        tag.putInt("warmup_progress", warmupProgress);
        tag.putInt("water_level", waterLevel);
        tag.putBoolean("is_active", isActive);
        tag.putBoolean("is_warmed_up", isWarmedUp);
        tag.put("inventory", inventory.serializeNBT(registries));
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        energyStored = tag.getInt("energy");
        fuelBurnTime = tag.getInt("fuel_burn_time");
        maxBurnTime = tag.getInt("max_burn_time");
        warmupProgress = tag.getInt("warmup_progress");
        waterLevel = tag.getInt("water_level");
        isActive = tag.getBoolean("is_active");
        isWarmedUp = tag.getBoolean("is_warmed_up");
        
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }
}