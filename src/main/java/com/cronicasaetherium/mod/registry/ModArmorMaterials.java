package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registro de materiais de armadura do mod Crônicas de Aetherium
 * 
 * Define as características dos diferentes tipos de armadura disponíveis no mod,
 * incluindo durabilidade, proteção, encantabilidade e sons.
 * 
 * Materiais implementados:
 * - BRONZE: Armadura tecnológica com alta proteção física
 * - TWISTED_WILLOW: Armadura mágica com baixa proteção mas bônus especiais
 */
public class ModArmorMaterials {
    
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = 
        DeferredRegister.create(Registries.ARMOR_MATERIAL, CronicasAetherium.MODID);
    
    /**
     * Material de Armadura de Bronze - Caminho Tecnológico
     * 
     * Características:
     * - Proteção alta (equivalente ao ferro)
     * - Durabilidade elevada
     * - Som metálico
     * - Reparo com lingotes de bronze
     */
    public static final Supplier<ArmorMaterial> BRONZE = ARMOR_MATERIALS.register("bronze", 
        () -> new ArmorMaterial(
            // Mapa de proteção por slot (pés, pernas, peito, cabeça)
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
            }),
            // Encantabilidade
            9,
            // Som quando equipado
            SoundEvents.ARMOR_EQUIP_IRON,
            // Material de reparo
            () -> Ingredient.of(ModItems.BRONZE_INGOT.get()),
            // Layers (recursos de textura)
            List.of(
                new ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "bronze")
                )
            ),
            // Resistência
            0.0F, // Sem resistência a knockback adicional (será aplicada pela classe do item)
            // Resistência a knockback
            0.0F
        ));
    
    /**
     * Material de Armadura de Salgueiro Torcido - Caminho Mágico
     * 
     * Características:
     * - Proteção baixa (equivalente ao couro)
     * - Durabilidade média
     * - Som orgânico
     * - Reparo com madeira de salgueiro torcido
     */
    public static final Supplier<ArmorMaterial> TWISTED_WILLOW = ARMOR_MATERIALS.register("twisted_willow", 
        () -> new ArmorMaterial(
            // Mapa de proteção por slot (pés, pernas, peito, cabeça)
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 2);
                map.put(ArmorItem.Type.CHESTPLATE, 3);
                map.put(ArmorItem.Type.HELMET, 1);
            }),
            // Encantabilidade (alta para armadura mágica)
            15,
            // Som quando equipado (madeira)
            SoundEvents.ARMOR_EQUIP_LEATHER,
            // Material de reparo
            () -> Ingredient.of(ModBlocks.TWISTED_WILLOW_LOG.get()),
            // Layers (recursos de textura)
            List.of(
                new ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "twisted_willow")
                )
            ),
            // Resistência
            0.0F,
            // Resistência a knockback
            0.0F
        ));
    
    /**
     * Registra os materiais de armadura no event bus
     */
    public static void register(IEventBus modEventBus) {
        ARMOR_MATERIALS.register(modEventBus);
    }
}