package com.cronicasaetherium.mod.items.dimensional;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Semente da Incerteza - Artefato caótico para acesso ao Vazio Intersticial
 * 
 * Este artefato instável não cria um portal tradicional, mas sim despedaça a
 * realidade ao redor do jogador, transportando-o diretamente para o Vazio Intersticial.
 * É uma viagem de mão única - a única saída é a vitória ou a morte.
 * 
 * Restrições de uso:
 * - Só funciona nos limites do mundo (Y >= 319 ou Y <= -63)
 * - Consumido no uso (viagem de mão única)
 * - Não há portal de retorno na dimensão de destino
 * - Apenas morte ou vitória contra o chefe permitem saída
 * 
 * Efeitos de ativação:
 * - Tela treme violentamente
 * - Shaders de glitch distorcem a visão
 * - Sons de estática e vidro quebrando
 * - Realidade parece ser "sugada" para dentro do jogador
 * - Teleportação abrupta após 3-4 segundos de caos
 * 
 * Crafting sugerido:
 * - Fragmento do Vazio (drop ultra-raro do Crisol Arcano)
 * - Cristal Instável (cristalizações de energia caótica)
 * - Essência da Incerteza (extraída de eventos dimensionais)
 * - Núcleo de Realidade Fragmentada (componente final boss)
 * 
 * Lore: "Um artefato que não deveria existir, forjado no caos entre dimensões.
 *        Pulsa com a incerteza fundamental da realidade."
 */
public class SeedOfUncertaintyItem extends Item {
    
    public SeedOfUncertaintyItem() {
        super(new Item.Properties()
            .stacksTo(1) // Único por natureza
            .rarity(Rarity.EPIC) // Raridade épica devido à natureza perigosa
            .fireResistant() // Indestrutível pelos elementos
        );
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack seed = player.getItemInHand(hand);
        
        if (level.isClientSide) {
            // Lado cliente: apenas confirma uso
            return InteractionResultHolder.success(seed);
        }
        
        // Verifica se o jogador está na altura correta
        BlockPos playerPos = player.blockPosition();
        double playerY = player.getY();
        
        if (playerY < 319 && playerY > -63) {
            // Não está nos limites do mundo
            player.sendSystemMessage(Component.literal(
                "§cA realidade está muito estável aqui. Vá para os limites extremos do mundo."));
            player.sendSystemMessage(Component.literal(
                "§7(Y >= 319 ou Y <= -63)"));
            return InteractionResultHolder.fail(seed);
        }
        
        // Está na altura correta! Inicia o caos dimensional
        if (player instanceof ServerPlayer serverPlayer) {
            initiateVoidTransition(serverPlayer, level, playerPos);
            
            // Consome a semente
            seed.shrink(1);
            
            return InteractionResultHolder.success(seed);
        }
        
        return InteractionResultHolder.pass(seed);
    }
    
    /**
     * Inicia a sequência de transição caótica para o Vazio
     */
    private void initiateVoidTransition(ServerPlayer player, Level level, BlockPos pos) {
        // Mensagem dramática
        player.sendSystemMessage(Component.literal("§4§l⚠ REALIDADE FRAGMENTANDO ⚠"));
        player.sendSystemMessage(Component.literal("§c§oA Semente da Incerteza rasga o tecido do espaço..."));
        
        // Efeitos sonoros caóticos
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 
            2.0F, 0.5F + level.random.nextFloat() * 0.5F);
        
        level.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 
            1.5F, 0.1F);
        
        // TODO: Adicionar efeitos visuais de caos
        // - Partículas de glitch
        // - Tremor de tela (através de packets customizados)
        // - Distorção visual
        
        // Agenda a teleportação após delay dramático
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().execute(() -> {
                try {
                    Thread.sleep(4000); // 4 segundos de caos
                    
                    // Efeito final antes da teleportação
                    serverLevel.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, 
                        SoundSource.PLAYERS, 3.0F, 0.1F);
                    
                    // TODO: Teleportar para dimensão do Vazio Intersticial
                    // player.changeDimension(VoidIntersticialLevel);
                    
                    // Por ora, apenas mensagem
                    player.sendSystemMessage(Component.literal("§4§lREALIDADE FRAGMENTADA!"));
                    player.sendSystemMessage(Component.literal("§7(Teleportação para Vazio não implementada ainda)"));
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        // Warning principal
        tooltipComponents.add(Component.literal("§4§l⚠ EXTREMAMENTE PERIGOSO ⚠"));
        tooltipComponents.add(Component.literal("§8"));
        
        // Instruções de uso
        tooltipComponents.add(Component.literal("§cUse apenas nos limites extremos do mundo"));
        tooltipComponents.add(Component.literal("§c(Y >= 319 ou Y <= -63)"));
        tooltipComponents.add(Component.literal("§8"));
        
        // Avisos
        tooltipComponents.add(Component.literal("§4⚠ Viagem de mão única"));
        tooltipComponents.add(Component.literal("§4⚠ Sem portal de retorno"));
        tooltipComponents.add(Component.literal("§4⚠ Vitória ou morte"));
        tooltipComponents.add(Component.literal("§8"));
        
        // Efeitos
        tooltipComponents.add(Component.literal("§7Clique direito para fragmentar a realidade"));
        tooltipComponents.add(Component.literal("§7e ser transportado para o §0Vazio Intersticial§7."));
        
        // Lore
        tooltipComponents.add(Component.literal("§8"));
        tooltipComponents.add(Component.literal("§o§8\"Um artefato que não deveria existir,"));
        tooltipComponents.add(Component.literal("§o§8forjado no caos entre dimensões."));
        tooltipComponents.add(Component.literal("§o§8Pulsa com a incerteza fundamental"));
        tooltipComponents.add(Component.literal("§o§8da realidade.\""));
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Sempre brilhante devido à natureza caótica
    }
    
    /**
     * A semente pulsa com energia caótica - animação especial no inventário
     */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false; // Não pode ser encantada devido à instabilidade
    }
    
    /**
     * Retorna uma cor caótica que muda constantemente
     */
    public int getChaosColor(long worldTime) {
        // Cor que muda baseada no tempo do mundo para efeito caótico
        double cycle = Math.sin(worldTime * 0.1) * 0.5 + 0.5;
        int red = (int)(255 * cycle);
        int green = (int)(128 * (1 - cycle));
        int blue = (int)(255 * Math.cos(worldTime * 0.05) * 0.5 + 0.5);
        
        return (red << 16) | (green << 8) | blue;
    }
}