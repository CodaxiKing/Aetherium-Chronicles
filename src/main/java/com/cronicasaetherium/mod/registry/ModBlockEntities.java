package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.blocks.synergy.ManaInfuserBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todas as BlockEntities do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todas as BlockEntities customizadas do mod,
 * incluindo máquinas, sistemas mágicos e estruturas especiais.
 * 
 * BlockEntities são responsáveis por armazenar dados de blocos que precisam
 * de lógica complexa, inventários, energia ou estados persistentes.
 */
public class ModBlockEntities {
    
    // DeferredRegister para registro eficiente de BlockEntities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CronicasAetherium.MODID);
    
    /**
     * BlockEntity da Infusora de Mana
     * Responsável pela lógica de conversão de energia em mana
     * Gerencia inventário, energia e processamento da máquina
     */
    public static final Supplier<BlockEntityType<ManaInfuserBlockEntity>> MANA_INFUSER = 
        BLOCK_ENTITIES.register("mana_infuser", () -> 
            BlockEntityType.Builder.of(ManaInfuserBlockEntity::new, ModBlocks.MANA_INFUSER.get())
                .build(null));
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}