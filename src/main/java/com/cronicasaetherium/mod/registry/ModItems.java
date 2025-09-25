package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todos os itens do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todos os itens customizados do mod,
 * incluindo materiais básicos, ferramentas, armaduras e itens mágicos.
 * 
 * Organização dos itens:
 * - Materiais básicos (Aetherium, cristais, etc.)
 * - Ferramentas e armas
 * - Componentes de máquinas
 * - Itens mágicos e relíquias
 * - Itens de invocação de chefes
 */
public class ModItems {
    
    // DeferredRegister para registro eficiente de itens
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(Registries.ITEM, CronicasAetherium.MODID);
    
    // ================================
    // MATERIAIS BÁSICOS E RECURSOS
    // ================================
    
    /**
     * Cristal de Aetherium - Material base do mod
     * Obtido através de mineração em estruturas especiais
     * Usado como componente principal em receitas avançadas
     */
    public static final Supplier<Item> AETHERIUM_CRYSTAL = ITEMS.register("aetherium_crystal",
        () -> new Item(new Item.Properties()));
    
    /**
     * Fragmento de Aetherium - Versão refinada do cristal
     * Obtido através de processamento tecnológico
     * Necessário para itens de alto nível
     */
    public static final Supplier<Item> AETHERIUM_FRAGMENT = ITEMS.register("aetherium_fragment",
        () -> new Item(new Item.Properties()));
    
    /**
     * Essência Mágica - Recurso base para o sistema de magia
     * Obtida através de rituais e processamento de plantas mágicas
     * Consumida na criação de feitiços e itens mágicos
     */
    public static final Supplier<Item> MAGIC_ESSENCE = ITEMS.register("magic_essence",
        () -> new Item(new Item.Properties()));
    
    // ================================
    // COMPONENTES TECNOLÓGICOS
    // ================================
    
    /**
     * Engrenagem de Aetherium - Componente básico para máquinas
     * Crafted combinando Aetherium com ferro
     * Usado em todas as máquinas rotacionais
     */
    public static final Supplier<Item> AETHERIUM_GEAR = ITEMS.register("aetherium_gear",
        () -> new Item(new Item.Properties()));
    
    /**
     * Circuito Mágico - Componente avançado que une tecnologia e magia
     * Necessário para máquinas que processam energia mágica
     * Requer tanto materiais tecnológicos quanto mágicos
     */
    public static final Supplier<Item> MAGIC_CIRCUIT = ITEMS.register("magic_circuit",
        () -> new Item(new Item.Properties()));
    
    // ================================
    // ITENS DE INVOCAÇÃO DE CHEFES
    // ================================
    
    /**
     * Amuleto do Dragão Ancestral - Invoca o primeiro chefe
     * Craftado com materiais raros encontrados em estruturas antigas
     * Uso único, consome o item ao invocar o chefe
     */
    public static final Supplier<Item> ANCIENT_DRAGON_AMULET = ITEMS.register("ancient_dragon_amulet",
        () -> new Item(new Item.Properties().stacksTo(1)));
    
    /**
     * Núcleo Corrompido - Invoca o chefe tecnológico
     * Obtido através de processamento avançado de materiais corrompidos
     * Deve ser usado em uma estrutura específica
     */
    public static final Supplier<Item> CORRUPTED_CORE = ITEMS.register("corrupted_core",
        () -> new Item(new Item.Properties().stacksTo(1)));
    
    /**
     * Orbe do Vazio - Invoca o chefe mágico final
     * Item mais raro do mod, requer materiais de todos os sistemas
     * Só pode ser usado após derrotar os outros dois chefes
     */
    public static final Supplier<Item> VOID_ORB = ITEMS.register("void_orb",
        () -> new Item(new Item.Properties().stacksTo(1)));
    
    // ================================
    // LIVROS E GUIAS
    // ================================
    
    /**
     * Tomo das Crônicas - Livro guia principal do mod
     * Contém todas as informações necessárias para progressão
     * Recebido automaticamente quando o jogador primeiro encontra um material do mod
     */
    public static final Supplier<Item> CHRONICLES_TOME = ITEMS.register("chronicles_tome",
        () -> new Item(new Item.Properties().stacksTo(1)));
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}