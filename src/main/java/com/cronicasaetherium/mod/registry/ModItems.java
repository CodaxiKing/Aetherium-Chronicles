package com.cronicasaetherium.mod.registry;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.cronicasaetherium.mod.items.synergy.RuneOfEfficiencyItem;
import com.cronicasaetherium.mod.items.lore.WornJournalItem;
import com.cronicasaetherium.mod.items.dimension.UnstableHeartItem;

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
    
    // ========= MATERIAIS TIER 1: TECNOLOGIA ==========
    
    /**
     * Minério de Cobre Cru - Material básico de tecnologia Tier 1
     * Encontrado nas camadas médias, base do sistema tecnológico inicial
     * Usado para criar bronze e componentes básicos
     */
    public static final Supplier<Item> RAW_COPPER = ITEMS.register("raw_copper",
        () -> new Item(new Item.Properties()));
    
    /**
     * Lingote de Cobre - Versão fundida do cobre cru
     * Primeiro material processado para tecnologia
     * Componente essencial em todas as receitas de bronze
     */
    public static final Supplier<Item> COPPER_INGOT = ITEMS.register("copper_ingot",
        () -> new Item(new Item.Properties()));
    
    /**
     * Minério de Estanho Cru - Segundo material básico de tecnologia Tier 1  
     * Mais raro que cobre, essencial para ligas
     * Encontrado em veios menores nas profundezas médias
     */
    public static final Supplier<Item> RAW_TIN = ITEMS.register("raw_tin",
        () -> new Item(new Item.Properties()));
    
    /**
     * Lingote de Estanho - Versão fundida do estanho cru
     * Material de liga necessário para criar bronze
     * Proporção menor nas receitas de bronze
     */
    public static final Supplier<Item> TIN_INGOT = ITEMS.register("tin_ingot",
        () -> new Item(new Item.Properties()));
    
    /**
     * Lingote de Bronze - Primeira liga do mod (Tier 1)
     * Criado combinando cobre e estanho na proporção correta
     * Material base para todas as máquinas e ferramentas básicas
     */
    public static final Supplier<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot",
        () -> new Item(new Item.Properties()));
    
    /**
     * Chave de Calibração - Ferramenta essencial de tecnologia
     * Primeira ferramenta especial que os jogadores devem criar
     * Usada para configurar e rotacionar máquinas
     */
    public static final Supplier<Item> CALIBRATION_WRENCH = ITEMS.register("calibration_wrench",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .durability(250)
            .rarity(Rarity.UNCOMMON)));
    
    // ========= MATERIAIS TIER 1: MAGIA ==========
    
    /**
     * Fragmento de Alma - Material mágico básico Tier 1
     * Encontrado no subsolo, pulsa com luz fraca
     * Base para toda a progressão mágica inicial
     */
    public static final Supplier<Item> SOUL_FRAGMENT = ITEMS.register("soul_fragment",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Essência Espiritual - Combustível mágico básico
     * Dropado por mobs mortos com Faca de Sacrifício
     * Consumido em todas as transmutações mágicas básicas
     */
    public static final Supplier<Item> SPIRIT_ESSENCE = ITEMS.register("spirit_essence",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.COMMON)));
    
    /**
     * Faca de Sacrifício - Ferramenta mágica para colheita de essências
     * Primeira ferramenta mágica especial
     * Mata mobs e tem chance de dropar Essência Espiritual
     */
    public static final Supplier<Item> SACRIFICE_KNIFE = ITEMS.register("sacrifice_knife",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .durability(150)
            .rarity(Rarity.UNCOMMON)));
    
    // ========= MATERIAIS TIER 2: TECNOLOGIA ==========
    
    /**
     * Minério de Cobalto Cru - Material avançado Tier 2
     * Encontrado em veios raros no subsolo profundo
     * Necessário para criar Aço Reforçado
     */
    public static final Supplier<Item> RAW_COBALT = ITEMS.register("raw_cobalt",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
    /**
     * Lingote de Cobalto - Versão fundida do cobalto cru  
     * Material raro para ligas avançadas
     * Componente do Aço Reforçado junto com ferro
     */
    public static final Supplier<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
    /**
     * Lingote de Aço Reforçado - Liga avançada Tier 2
     * Criado combinando Ferro + Cobalto + FE na Fundidora de Ligas
     * Material principal para todas as máquinas Tier 2
     */
    public static final Supplier<Item> REINFORCED_STEEL_INGOT = ITEMS.register("reinforced_steel_ingot",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
    // ========= MATERIAIS DE MINI-BOSS ==========
    
    /**
     * Coração de Adamantina - Drop do Colosso de Rocha (mini-boss)
     * Item raro necessário para rituais avançados e máquinas Tier 2+
     * Só pode ser obtido derrotando o primeiro mini-boss
     */
    public static final Supplier<Item> ADAMANTINE_HEART = ITEMS.register("adamantine_heart",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.EPIC)));
    
    // ========= MATERIAIS TIER 2: MAGIA ==========
    
    /**
     * Espírito Maligno - Essência de mobs hostis
     * Dropado por mobs agressivos mortos com Faca de Sacrifício
     * Usado em rituais de combate e encantamentos ofensivos
     */
    public static final Supplier<Item> MALIGNANT_SPIRIT = ITEMS.register("malignant_spirit",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Espírito Puro - Essência de mobs pacíficos
     * Dropado por mobs passivos mortos com Faca de Sacrifício
     * Usado em rituais de proteção e encantamentos defensivos
     */
    public static final Supplier<Item> PURE_SPIRIT = ITEMS.register("pure_spirit",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Espírito Arcano - Essência de mobs mágicos
     * Dropado por criaturas mágicas mortas com Faca de Sacrifício
     * Material mais raro, usado em rituais avançados
     */
    public static final Supplier<Item> ARCANE_SPIRIT = ITEMS.register("arcane_spirit",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
    /**
     * Essência Mágica Concentrada - Versão refinada da essência básica
     * Criada através de rituais no Altar de Infusão
     * Necessária para encantamentos permanentes e artefatos
     */
    public static final Supplier<Item> CONCENTRATED_MAGIC_ESSENCE = ITEMS.register("concentrated_magic_essence",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
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
    // AMULETOS E ITENS FUNCIONAIS
    // ================================
    
    // ========= AMULETOS TIER 1: MAGIA ==========
    
    /**
     * Amuleto de Penumbra - Primeiro amuleto funcional Tier 1
     * Reduz o alcance de detecção de monstros na escuridão
     * Facilita exploração noturna e em cavernas escuras
     */
    public static final Supplier<Item> PENUMBRA_AMULET = ITEMS.register("penumbra_amulet",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Anel de Caçador - Segundo item funcional Tier 1
     * Faz inimigos atingidos brilharem através das paredes
     * Útil para rastrear mobs após combate inicial
     */
    public static final Supplier<Item> HUNTER_RING = ITEMS.register("hunter_ring",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Códice Escondido - Guia mágico Tier 1
     * Criado com livro + Fragmento de Alma
     * Revela novos conhecimentos mágicos conforme progressão
     */
    public static final Supplier<Item> HIDDEN_CODEX = ITEMS.register("hidden_codex",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)));
    
    // ========= FERRAMENTAS MÁGICAS TIER 1 ==========
    
    /**
     * Varinha da Floresta - Ferramenta de ativação mágica
     * Usada para ativar rituais no Altar Arcano
     * Ferramenta essencial para progressão mágica Tier 2
     */
    public static final Supplier<Item> FOREST_WAND = ITEMS.register("forest_wand",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .durability(100)
            .rarity(Rarity.UNCOMMON)));
    
    // ========= ARTEFATOS TIER 2: SINERGIA ==========
    
    /**
     * Anel de Regeneração - Item funcional mágico Tier 2
     * Consome mana do inventário para regenerar vida lentamente
     * Exemplo de magia funcional para sobrevivência
     */
    public static final Supplier<Item> REGENERATION_RING = ITEMS.register("regeneration_ring",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)));
    
    /**
     * Núcleo Anímico - Item Tier 3 para criação de golens
     * Criado no Altar de Infusão com materiais raros
     * Permite criar golems mágicos assistentes
     */
    public static final Supplier<Item> ANIMIC_CORE = ITEMS.register("animic_core",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.EPIC)));
    
    // ========= BOLSAS E ARMAZENAMENTO ==========
    
    /**
     * Bolsa de Espíritos - Armazenamento especializado Tier 2
     * Armazena e separa diferentes tipos de espíritos colhidos
     * Essencial para magia avançada organizada
     */
    public static final Supplier<Item> SPIRIT_POUCH = ITEMS.register("spirit_pouch",
        () -> new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)));
    
    // ========= COMPONENTES TIER 1 TECNOLOGIA ==========
    
    /**
     * Engrenagem de Bronze - Componente básico Tier 1
     * Criada na Prensa de Engrenagens com lingotes de bronze
     * Usada em todas as máquinas Tier 1
     */
    public static final Supplier<Item> BRONZE_GEAR = ITEMS.register("bronze_gear",
        () -> new Item(new Item.Properties()));
    
    /**
     * Placa de Bronze - Componente estrutural Tier 1
     * Criada na Prensa de Engrenagens 
     * Material de construção para máquinas
     */
    public static final Supplier<Item> BRONZE_PLATE = ITEMS.register("bronze_plate",
        () -> new Item(new Item.Properties()));
    
    /**
     * Pó de Cobre - Material processado
     * Resultado do Triturador Mecânico processando minério de cobre
     * Dobra o rendimento de cobre quando fundido
     */
    public static final Supplier<Item> COPPER_DUST = ITEMS.register("copper_dust",
        () -> new Item(new Item.Properties()));
    
    /**
     * Pó de Estanho - Material processado
     * Resultado do Triturador Mecânico processando minério de estanho
     * Dobra o rendimento de estanho quando fundido  
     */
    public static final Supplier<Item> TIN_DUST = ITEMS.register("tin_dust",
        () -> new Item(new Item.Properties()));
    
    // ========= COMPONENTES TIER 2 TECNOLOGIA ==========
    
    /**
     * Engrenagem de Aço Reforçado - Componente avançado Tier 2
     * Criada com Aço Reforçado para máquinas de alta performance
     * Essencial para automação avançada
     */
    public static final Supplier<Item> REINFORCED_STEEL_GEAR = ITEMS.register("reinforced_steel_gear",
        () -> new Item(new Item.Properties()
            .rarity(Rarity.RARE)));
    
    /**
     * Circuito de Aprimoramento: Velocidade - Módulo Tier 2
     * Pode ser instalado em máquinas para aumentar velocidade
     * Sistema modular de otimização de máquinas
     */
    public static final Supplier<Item> SPEED_UPGRADE_CIRCUIT = ITEMS.register("speed_upgrade_circuit",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Circuito de Aprimoramento: Eficiência - Módulo Tier 2
     * Reduz consumo de energia das máquinas
     * Otimização de energia para produção sustentável
     */
    public static final Supplier<Item> EFFICIENCY_UPGRADE_CIRCUIT = ITEMS.register("efficiency_upgrade_circuit",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)));
    
    /**
     * Circuito de Aprimoramento: Sorte - Módulo Tier 2
     * Adiciona efeito Fortune às máquinas de processamento
     * Aumenta rendimento de materiais processados
     */
    public static final Supplier<Item> FORTUNE_UPGRADE_CIRCUIT = ITEMS.register("fortune_upgrade_circuit",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.RARE)));

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
    // ITENS DA DIMENSÃO CRISOL ARCANO
    // ================================
    
    /**
     * Coração Instável - Ativador do portal para a dimensão Crisol Arcano
     * Item raro usado para ativar a estrutura do portal arcano
     * Consumido durante a ativação do portal
     */
    public static final Supplier<Item> UNSTABLE_HEART = ITEMS.register("unstable_heart",
        () -> new UnstableHeartItem(new Item.Properties().stacksTo(1)));
    
    // ================================
    // ITENS DE SINERGIA (TECNOLOGIA + MAGIA)
    // ================================
    
    /**
     * Runa de Eficiência - Item de sinergia Magia → Tecnologia
     * Aplicada em máquinas para conceder bônus de eficiência
     * Consome o item ao usar, efeito permanente na máquina
     */
    public static final Supplier<Item> RUNE_OF_EFFICIENCY = ITEMS.register("rune_of_efficiency",
        () -> new RuneOfEfficiencyItem(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)));
    
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
     * Diário Desgastado - Item de lore introdutório
     * Contém fragmentos da história e lore do mod
     * Abre interface com textos introdutórios quando usado
     */
    public static final Supplier<Item> WORN_JOURNAL = ITEMS.register("worn_journal",
        () -> new WornJournalItem(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)));
    
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