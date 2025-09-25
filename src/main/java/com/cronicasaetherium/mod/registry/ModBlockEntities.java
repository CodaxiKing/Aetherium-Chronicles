package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.blocks.synergy.ManaInfuserBlockEntity;
import com.cronicasaetherium.mod.blocks.tech.SteamEngineBlockEntity;
import com.cronicasaetherium.mod.blocks.tech.MechanicalCrusherBlockEntity;
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
     * BlockEntity do Motor a Vapor
     * Coração do sistema tecnológico Tier 1
     * Converte combustível + água em Forge Energy
     */
    public static final Supplier<BlockEntityType<SteamEngineBlockEntity>> STEAM_ENGINE = 
        BLOCK_ENTITIES.register("steam_engine", () -> 
            BlockEntityType.Builder.of(SteamEngineBlockEntity::new, ModBlocks.STEAM_ENGINE.get())
                .build(null));
    
    /**
     * BlockEntity do Triturador Mecânico
     * Máquina de processamento Tier 1
     * Dobra rendimento de minérios com chance de subprodutos
     */
    public static final Supplier<BlockEntityType<MechanicalCrusherBlockEntity>> MECHANICAL_CRUSHER = 
        BLOCK_ENTITIES.register("mechanical_crusher", () -> 
            BlockEntityType.Builder.of(MechanicalCrusherBlockEntity::new, ModBlocks.MECHANICAL_CRUSHER.get())
                .build(null));
    
    /**
     * BlockEntity da Centrífuga Espiritual
     * Máquina de sinergia entre Tecnologia e Magia
     * Processa bolsas de espírito usando energia tecnológica
     */
    public static final Supplier<BlockEntityType<com.cronicasaetherium.mod.blocks.synergy.SpiritCentrifugeBlockEntity>> SPIRIT_CENTRIFUGE = 
        BLOCK_ENTITIES.register("spirit_centrifuge", () -> 
            BlockEntityType.Builder.of(com.cronicasaetherium.mod.blocks.synergy.SpiritCentrifugeBlockEntity::new, ModBlocks.SPIRIT_CENTRIFUGE.get())
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