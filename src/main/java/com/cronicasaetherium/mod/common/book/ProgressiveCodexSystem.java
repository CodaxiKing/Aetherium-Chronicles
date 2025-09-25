package com.cronicasaetherium.mod.common.book;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Sistema de Desbloqueio Progressivo do Códice Escondido
 * 
 * Este sistema implementa a descoberta orgânica de conhecimento conforme
 * o jogador progride através dos pilares de Tecnologia e Magia.
 * 
 * Funcionamento:
 * - O Códice inicia com informações mínimas
 * - Gatilhos são ativados quando o jogador crafta itens-chave
 * - Novos capítulos são desbloqueados progressivamente
 * - Mensagens sutis informam sobre novo conhecimento
 * 
 * Gatilhos Implementados:
 * - Faca de Sacrifício → Capítulo "Colheita de Espíritos"
 * - Motor a Vapor → Capítulo "A Revolução a Vapor"
 * - Primeiro Aço Reforçado → Capítulo "A Era do Aço"
 * - Tijolo Infundido → Capítulo "Sinergia dos Pilares"
 * - Armadura especializada → Capítulos de especialização
 * 
 * O sistema persiste o progresso do jogador e sincroniza entre sessões.
 */
@EventBusSubscriber(modid = CronicasAetherium.MODID)
public class ProgressiveCodexSystem {
    
    // Armazenamento do progresso por jogador (UUID -> capítulos desbloqueados)
    private static final Map<UUID, Set<String>> playerProgress = new HashMap<>();
    
    // Mapeamento de itens-gatilho para capítulos
    private static final Map<ResourceLocation, CodexChapter> triggerItems = new HashMap<>();
    
    // Inicialização dos gatilhos
    static {
        initializeTriggers();
    }
    
    /**
     * Inicializa todos os gatilhos de desbloqueio
     */
    private static void initializeTriggers() {
        // TODO: Registrar gatilhos quando os itens estiverem disponíveis
        // registerTrigger("cronicasaetherium:sacrifice_knife", 
        //     new CodexChapter("spirit_harvest", "Colheita de Espíritos", "Aprenda a extrair essências espirituais"));
        
        // registerTrigger("cronicasaetherium:steam_engine", 
        //     new CodexChapter("steam_revolution", "A Revolução a Vapor", "Domine a primeira fonte de energia"));
        
        // registerTrigger("cronicasaetherium:reinforced_steel_ingot", 
        //     new CodexChapter("steel_age", "A Era do Aço", "Tecnologia avançada ao seu alcance"));
        
        // registerTrigger("cronicasaetherium:soul_infused_brick", 
        //     new CodexChapter("pillar_synergy", "Sinergia dos Pilares", "Quando magia e tecnologia se unem"));
        
        // registerTrigger("cronicasaetherium:bronze_helmet", 
        //     new CodexChapter("tech_specialization", "Especialização Tecnológica", "O caminho da resistência"));
        
        // registerTrigger("cronicasaetherium:twisted_willow_helmet", 
        //     new CodexChapter("magic_specialization", "Especialização Mágica", "O caminho da essência"));
    }
    
    /**
     * Registra um item como gatilho para desbloqueio de capítulo
     * 
     * @param itemId ID do item (ex: "cronicasaetherium:steam_engine")
     * @param chapter Capítulo a desbloquear
     */
    private static void registerTrigger(String itemId, CodexChapter chapter) {
        triggerItems.put(ResourceLocation.parse(itemId), chapter);
    }
    
    /**
     * Event handler para quando um jogador crafta um item
     * Verifica se é um gatilho e desbloqueia conteúdo se necessário
     */
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack craftedStack = event.getCrafting();
            
            if (!craftedStack.isEmpty()) {
                checkAndUnlockChapter(player, craftedStack);
            }
        }
    }
    
    /**
     * Verifica se o item craftado é um gatilho e desbloqueia capítulo
     * 
     * @param player Jogador que craftou
     * @param craftedStack Item craftado
     */
    private static void checkAndUnlockChapter(ServerPlayer player, ItemStack craftedStack) {
        Item item = craftedStack.getItem();
        ResourceLocation itemId = getItemId(item);
        
        if (itemId != null && triggerItems.containsKey(itemId)) {
            CodexChapter chapter = triggerItems.get(itemId);
            
            if (unlockChapterForPlayer(player, chapter)) {
                // Notifica o jogador sobre o novo conhecimento
                sendUnlockNotification(player, chapter);
                
                CronicasAetherium.LOGGER.info("Capítulo '{}' desbloqueado para jogador {}",
                    chapter.title, player.getName().getString());
            }
        }
    }
    
    /**
     * Obtém o ResourceLocation de um item
     */
    private static ResourceLocation getItemId(Item item) {
        // TODO: Implementar quando o sistema de registro estiver completo
        // return BuiltInRegistries.ITEM.getKey(item);
        return null;
    }
    
    /**
     * Desbloqueia um capítulo para um jogador específico
     * 
     * @param player Jogador alvo
     * @param chapter Capítulo a desbloquear
     * @return true se foi desbloqueado (não estava já desbloqueado)
     */
    public static boolean unlockChapterForPlayer(ServerPlayer player, CodexChapter chapter) {
        UUID playerId = player.getUUID();
        
        // Obtém ou cria progresso do jogador
        Set<String> unlockedChapters = playerProgress.computeIfAbsent(playerId, k -> new HashSet<>());
        
        // Verifica se já estava desbloqueado
        if (unlockedChapters.contains(chapter.id)) {
            return false; // Já desbloqueado
        }
        
        // Desbloqueia o capítulo
        unlockedChapters.add(chapter.id);
        
        // TODO: Persistir progresso em arquivo ou NBT do jogador
        savePlayerProgress(player, unlockedChapters);
        
        return true;
    }
    
    /**
     * Verifica se um jogador tem um capítulo desbloqueado
     * 
     * @param player Jogador a verificar
     * @param chapterId ID do capítulo
     * @return true se desbloqueado
     */
    public static boolean isChapterUnlocked(Player player, String chapterId) {
        UUID playerId = player.getUUID();
        Set<String> unlockedChapters = playerProgress.get(playerId);
        
        return unlockedChapters != null && unlockedChapters.contains(chapterId);
    }
    
    /**
     * Obtém todos os capítulos desbloqueados de um jogador
     * 
     * @param player Jogador a verificar
     * @return Set com IDs dos capítulos desbloqueados
     */
    public static Set<String> getUnlockedChapters(Player player) {
        UUID playerId = player.getUUID();
        return playerProgress.getOrDefault(playerId, new HashSet<>());
    }
    
    /**
     * Envia notificação sutil sobre novo conhecimento desbloqueado
     * 
     * @param player Jogador a notificar
     * @param chapter Capítulo desbloqueado
     */
    private static void sendUnlockNotification(ServerPlayer player, CodexChapter chapter) {
        // Mensagem principal de desbloqueio
        Component mainMessage = Component.translatable("codex.cronicasaetherium.chapter_unlocked")
            .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE, net.minecraft.ChatFormatting.ITALIC);
        
        // Nome do capítulo desbloqueado
        Component chapterMessage = Component.literal("\"" + chapter.title + "\"")
            .withStyle(net.minecraft.ChatFormatting.GOLD);
        
        // Mensagem completa
        Component fullMessage = Component.translatable("codex.cronicasaetherium.new_knowledge", 
            mainMessage, chapterMessage);
        
        // Envia mensagem no chat
        player.sendSystemMessage(fullMessage);
        
        // TODO: Adicionar efeito visual/sonoro quando possível
        // player.level().playSound(null, player.blockPosition(), 
        //     ModSounds.CODEX_UNLOCK.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
    }
    
    /**
     * Salva o progresso do jogador (placeholder)
     * 
     * @param player Jogador
     * @param chapters Capítulos desbloqueados
     */
    private static void savePlayerProgress(ServerPlayer player, Set<String> chapters) {
        // TODO: Implementar persistência real
        // Opções: NBT do jogador, arquivo JSON, banco de dados
        CronicasAetherium.LOGGER.debug("Salvando progresso do Códice para {}: {} capítulos",
            player.getName().getString(), chapters.size());
    }
    
    /**
     * Carrega o progresso do jogador quando conecta
     * 
     * @param player Jogador que conectou
     */
    public static void loadPlayerProgress(ServerPlayer player) {
        // TODO: Implementar carregamento real da persistência
        UUID playerId = player.getUUID();
        
        // Por enquanto, apenas inicializa com capítulos básicos
        Set<String> basicChapters = new HashSet<>();
        basicChapters.add("introduction"); // Capítulo introdutório sempre disponível
        
        playerProgress.put(playerId, basicChapters);
        
        CronicasAetherium.LOGGER.debug("Carregado progresso do Códice para {}", 
            player.getName().getString());
    }
    
    /**
     * Force unlock para debug/admin
     * 
     * @param player Jogador alvo
     * @param chapterId ID do capítulo
     */
    public static void forceUnlockChapter(ServerPlayer player, String chapterId) {
        // Cria capítulo temporário para forceUnlock
        CodexChapter debugChapter = new CodexChapter(chapterId, "Debug Chapter", "Forcefully unlocked");
        unlockChapterForPlayer(player, debugChapter);
    }
    
    /**
     * Classe para representar um capítulo do Códice
     */
    public static class CodexChapter {
        public final String id;
        public final String title;
        public final String description;
        
        public CodexChapter(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CodexChapter that = (CodexChapter) obj;
            return id.equals(that.id);
        }
        
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}