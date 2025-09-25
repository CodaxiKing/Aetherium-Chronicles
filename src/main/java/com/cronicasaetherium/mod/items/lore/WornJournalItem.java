package com.cronicasaetherium.mod.items.lore;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Diário Desgastado - Item de lore introdutório
 * 
 * Este item contém fragmentos da história e lore do mod Crônicas de Aetherium.
 * Quando usado, exibe textos introdutórios que explicam o mundo, a história
 * e os sistemas do mod de forma imersiva.
 * 
 * Funcionalidades:
 * - Interface de lore com múltiplas páginas
 * - Textos introdutórios sobre o mundo de Aetherium
 * - Orientações básicas sobre os sistemas do mod
 * - Design imersivo que complementa a experiência
 */
public class WornJournalItem extends Item {
    
    // Páginas do diário com conteúdo de lore
    private static final String[] LORE_PAGES = {
        // Página 1 - Introdução
        "§6=== Crônicas de Aetherium ===§r\n\n" +
        "§7Fragmento 1:§r\n\n" +
        "...o mundo rachou, e de suas veias cristalinas, uma nova energia emergiu. " +
        "O Aetherium, como veio a ser chamado, não era apenas um minério comum - " +
        "era a materialização da própria força vital do universo.\n\n" +
        "§oAs antigas civilizações descobriram que esta energia podia ser " +
        "moldada tanto pela engenhosidade quanto pela magia...§r",
        
        // Página 2 - Os Dois Caminhos
        "§6=== Os Dois Caminhos ===§r\n\n" +
        "§7Fragmento 2:§r\n\n" +
        "Dois caminhos emergiram entre os estudiosos do Aetherium:\n\n" +
        "§e⚙ A Engenharia§r - Aqueles que buscavam dominar a energia através " +
        "de máquinas precisas, engrenagens e automação. Eles viam no Aetherium " +
        "uma fonte de poder tecnológico.\n\n" +
        "§d✨ A Arcana§r - Aqueles que compreendiam a natureza mística da energia, " +
        "canalizando-a através de rituais, plantas e conhecimento ancestral.",
        
        // Página 3 - A Sinergia
        "§6=== A Grande Descoberta ===§r\n\n" +
        "§7Fragmento 3:§r\n\n" +
        "§5Mas os mais sábios descobriram que os dois caminhos não eram opostos - " +
        "eram complementares.§r\n\n" +
        "Máquinas imbuídas com essência mágica operavam com eficiência impossível. " +
        "Rituais alimentados por energia tecnológica alcançavam poder inimaginável.\n\n" +
        "§oA verdadeira maestria do Aetherium vem da harmonia entre " +
        "tecnologia e magia...§r",
        
        // Página 4 - O Crisol Arcano
        "§6=== O Crisol Arcano ===§r\n\n" +
        "§7Fragmento 4:§r\n\n" +
        "Além do véu da realidade existe um lugar onde o Aetherium cresce " +
        "em cristalizações puras - o §bCrisol Arcano§r.\n\n" +
        "Esta dimensão instável é tanto um laboratório quanto um campo de provas. " +
        "Apenas aqueles com coragem e conhecimento suficiente podem ativar " +
        "os antigos portais que levam a este reino.\n\n" +
        "§cCuidado - o Crisol não perdoa os despreparados...§r",
        
        // Página 5 - Orientações Práticas
        "§6=== Primeiros Passos ===§r\n\n" +
        "§7Para o Explorador Iniciante:§r\n\n" +
        "§e1.§r Mine §bAetherium§r nas profundezas e estruturas antigas\n" +
        "§e2.§r Escolha seu caminho: §6Engenharia§r ou §dArcana§r\n" +
        "§e3.§r Construa suas primeiras máquinas ou altares\n" +
        "§e4.§r Ganhe experiência em sua proficiência escolhida\n" +
        "§e5.§r Descubra a sinergia entre os sistemas\n\n" +
        "§oLembre-se: a jornada mais gratificante combina ambos os caminhos.§r"
    };
    
    /**
     * Construtor do Diário Desgastado
     * 
     * @param properties Propriedades do item
     */
    public WornJournalItem(Properties properties) {
        super(properties);
    }
    
    /**
     * Manipula o uso do diário
     * 
     * Quando o jogador usa o item (botão direito), abre a interface
     * de lore com os textos introdutórios do mod.
     * 
     * @param level Nível/mundo
     * @param player Jogador que usou o item
     * @param usedHand Mão usada
     * @return Resultado da interação
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        
        if (!level.isClientSide()) {
            openJournalInterface(player);
        }
        
        return InteractionResultHolder.success(stack);
    }
    
    /**
     * Abre a interface de lore do diário
     * 
     * Por ora, exibe as páginas como mensagens no chat.
     * Em uma implementação futura, seria uma GUI dedicada.
     * 
     * @param player Jogador que verá o conteúdo
     */
    private void openJournalInterface(Player player) {
        // TODO: Implementar GUI dedicada para o diário
        // Por ora, exibe o conteúdo no chat
        
        player.sendSystemMessage(Component.literal("§6📖 Abrindo o Diário Desgastado...§r"));
        
        // Determina qual página mostrar baseado em um estado persistente
        // Por simplicidade, mostra uma página aleatória ou sequencial
        int pageIndex = (int) (player.level().getGameTime() / 100) % LORE_PAGES.length;
        String currentPage = LORE_PAGES[pageIndex];
        
        // Divide o conteúdo em linhas para melhor formatação no chat
        String[] lines = currentPage.split("\n");
        
        // Exibe o cabeçalho
        player.sendSystemMessage(Component.literal("§8" + "=".repeat(40) + "§r"));
        
        // Exibe o conteúdo da página
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                player.sendSystemMessage(Component.literal(line));
            } else {
                player.sendSystemMessage(Component.literal("")); // Linha vazia
            }
        }
        
        // Exibe informações de navegação
        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal(
            String.format("§8Página %d de %d - Use novamente para a próxima página§r", 
                pageIndex + 1, LORE_PAGES.length)));
        player.sendSystemMessage(Component.literal("§8" + "=".repeat(40) + "§r"));
        
        // Som de virar página
        player.level().playSound(null, player.blockPosition(), 
            net.minecraft.sounds.SoundEvents.ITEM_BOOK_PAGE_TURN, 
            net.minecraft.sounds.SoundSource.PLAYERS, 0.7f, 1.0f);
    }
}