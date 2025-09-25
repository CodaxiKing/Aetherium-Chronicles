package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todas as entidades do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todas as entidades customizadas do mod,
 * incluindo mobs hostis, neutros, passivos e chefes épicos.
 * 
 * Organização das entidades:
 * - Mobs passivos (herbívoros, criaturas mansas)
 * - Mobs neutros (defensivos, territoriais)
 * - Mobs hostis (agressivos, predadores)
 * - Chefes épicos (criaturas lendárias)
 * - Entidades de projétil e utilidade
 * 
 * Cada categoria tem comportamentos e sistemas de spawn específicos
 * para incentivar diferentes tipos de exploração e estratégias de combate.
 */
public class ModEntities {
    
    // DeferredRegister para registro eficiente de entidades
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(Registries.ENTITY_TYPE, CronicasAetherium.MODID);
    
    // ================================
    // MOBS PASSIVOS - EXPLORAÇÃO PACÍFICA
    // ================================
    
    // TODO: Entidades comentadas temporariamente até implementação das classes
    /*
     * Gigante das Planícies - Herbívoro majestoso e gentil
     * 
     * Características:
     * - Vagueia pelas planícies em pequenos grupos
     * - Não ataca mesmo quando provocado
     * - Dropa couro especial e materiais raros
     * - Pode ser alimentado com grama especial para obter recursos
     * 
     * Comportamento:
     * - Migra entre diferentes áreas de pasto
     * - Protege filhotes de outros mobs
     * - Emite sons calmantes que afastam mobs hostis
     */
    /*
    public static final Supplier<EntityType<?>> PLAINS_GIANT = ENTITY_TYPES.register("plains_giant",
        () -> EntityType.Builder.of(null, MobCategory.CREATURE) // TODO: Implementar classe da entidade
            .sized(3.0F, 4.0F) // Criatura grande
            .clientTrackingRange(10)
            .updateInterval(3)
            .build("plains_giant"));
    */
    
    /*
     * Cristal Flutuante - Criatura mágica etérea (TODO: Implementar)
     */
    /*
    public static final Supplier<EntityType<?>> FLOATING_CRYSTAL = ENTITY_TYPES.register("floating_crystal",
        () -> EntityType.Builder.of(null, MobCategory.AMBIENT)
            .sized(1.0F, 1.0F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .fireImmune()
            .build("floating_crystal"));
    */
    
    // ================================
    // MOBS NEUTROS - EXPLORAÇÃO CAUTELOSA (TODO: Implementar)
    // ================================
    
    /*
     * Guardião de Ruínas - Defensor ancestral de estruturas (TODO: Implementar)
     */
    /*
    public static final Supplier<EntityType<?>> RUIN_GUARDIAN = ENTITY_TYPES.register("ruin_guardian",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(1.5F, 2.5F)
            .clientTrackingRange(10)
            .updateInterval(3)
            .fireImmune()
            .build("ruin_guardian"));
    */
    
    // ================================
    // MOBS HOSTIS - COMBATE E DESAFIO (TODO: Implementar)
    // ================================
    
    /*
     * TODO: Todas as entidades hostis serão implementadas nas próximas fases
     * Por enquanto estão comentadas para evitar erros de build
     */
    
    /*
    public static final Supplier<EntityType<?>> INSECTOID_SWARM = ENTITY_TYPES.register("insectoid_swarm",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(0.8F, 0.6F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("insectoid_swarm"));
    
    public static final Supplier<EntityType<?>> SHADOW_STALKER = ENTITY_TYPES.register("shadow_stalker",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(1.2F, 2.0F)
            .clientTrackingRange(12)
            .updateInterval(3)
            .build("shadow_stalker"));
    */
    
    // ================================
    // CHEFES ÉPICOS - BATALHAS MEMORÁVEIS (TODO: Implementar)
    // ================================
    
    /*
     * TODO: Todos os chefes e projéteis serão implementados nas próximas fases
     * Por enquanto estão comentados para evitar erros de build
     */
    
    /*
    public static final Supplier<EntityType<?>> ANCIENT_DRAGON = ENTITY_TYPES.register("ancient_dragon",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(5.0F, 3.0F)
            .clientTrackingRange(16)
            .updateInterval(1)
            .fireImmune()
            .build("ancient_dragon"));
    
    public static final Supplier<EntityType<?>> TECH_COLOSSUS = ENTITY_TYPES.register("tech_colossus",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(6.0F, 8.0F)
            .clientTrackingRange(20)
            .updateInterval(1)
            .fireImmune()
            .build("tech_colossus"));
    
    public static final Supplier<EntityType<?>> VOID_LORD = ENTITY_TYPES.register("void_lord",
        () -> EntityType.Builder.of(null, MobCategory.MONSTER)
            .sized(4.0F, 6.0F)
            .clientTrackingRange(24)
            .updateInterval(1)
            .fireImmune()
            .build("void_lord"));
    
    public static final Supplier<EntityType<?>> MAGIC_PROJECTILE = ENTITY_TYPES.register("magic_projectile",
        () -> EntityType.Builder.of(null, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build("magic_projectile"));
    */
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}