package com.cronicasaetherium.mod.common.capability;

import com.cronicasaetherium.mod.CronicasAetherium;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Registro central e gerenciamento das capacidades personalizadas do mod
 * 
 * Esta classe é responsável por:
 * - Registrar as capacidades customizadas (PlayerProficiency)
 * - Gerenciar a anexação das capacidades às entidades apropriadas
 * - Lidar com eventos de persistência (morte, clone, etc.)
 * - Fornecer métodos de acesso convenientes para outras classes
 * 
 * O sistema de capacidades do NeoForge permite anexar dados customizados
 * a entidades, itens, blocos, etc. de forma que sejam automaticamente
 * sincronizados e persistidos.
 */
@EventBusSubscriber(modid = CronicasAetherium.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapabilities {
    
    /**
     * Capacidade de proficiência do jogador
     * 
     * Esta capacidade é anexada apenas a jogadores (Player entities)
     * e armazena informações sobre suas proficiências em engenharia e arcana.
     * 
     * ResourceLocation: cronicasaetherium:player_proficiency
     */
    public static final EntityCapability<PlayerProficiency, Void> PLAYER_PROFICIENCY = 
        EntityCapability.createVoid(
            ResourceLocation.fromNamespaceAndPath(CronicasAetherium.MODID, "player_proficiency"),
            PlayerProficiency.class
        );
    
    /**
     * Evento de registro de capacidades
     * 
     * Este método é chamado durante a fase de registro do mod e é responsável
     * por registrar todas as capacidades customizadas e definir como elas
     * são anexadas às entidades apropriadas.
     * 
     * @param event Evento de registro de capacidades
     */
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Registra a capacidade de proficiência para jogadores
        event.registerEntity(
            PLAYER_PROFICIENCY,
            Player.class,
            (player, context) -> new PlayerProficiency()
        );
        
        CronicasAetherium.LOGGER.info("Capacidades do mod registradas com sucesso");
    }
    
    /**
     * Método utilitário para obter a capacidade de proficiência de um jogador
     * 
     * Este método fornece uma forma conveniente e segura de acessar a
     * capacidade de proficiência de um jogador, com verificação de nulidade.
     * 
     * @param player Jogador do qual obter a capacidade
     * @return Capacidade de proficiência ou null se não estiver disponível
     */
    @Nullable
    public static PlayerProficiency getPlayerProficiency(Player player) {
        return player.getCapability(PLAYER_PROFICIENCY);
    }
    
    /**
     * Método utilitário para verificar se um jogador tem a capacidade de proficiência
     * 
     * @param player Jogador a verificar
     * @return true se a capacidade estiver disponível, false caso contrário
     */
    public static boolean hasPlayerProficiency(Player player) {
        return getPlayerProficiency(player) != null;
    }
    
    /**
     * Método utilitário para executar uma ação na capacidade de proficiência se disponível
     * 
     * Este método permite executar código que modifica a proficiência do jogador
     * de forma segura, verificando automaticamente se a capacidade está disponível.
     * 
     * @param player Jogador alvo
     * @param action Ação a ser executada na capacidade
     */
    public static void ifPlayerProficiencyPresent(Player player, java.util.function.Consumer<PlayerProficiency> action) {
        PlayerProficiency proficiency = getPlayerProficiency(player);
        if (proficiency != null) {
            action.accept(proficiency);
        }
    }
}

/**
 * Event handler para gerenciar eventos relacionados às capacidades
 * 
 * Esta classe cuida dos eventos que podem afetar as capacidades dos jogadores,
 * garantindo que os dados sejam preservados corretamente em situações como
 * morte, respawn, viagem dimensional, etc.
 */
@EventBusSubscriber(modid = CronicasAetherium.MODID, bus = EventBusSubscriber.Bus.GAME)
class CapabilityEventHandler {
    
    /**
     * Evento de clone do jogador (ao morrer/respawnar)
     * 
     * Este evento é chamado quando um jogador morre e respawna, ou quando
     * viaja entre dimensões. Precisamos copiar os dados de proficiência
     * da entidade antiga para a nova.
     * 
     * @param event Evento de clone do jogador
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // Só processa se o jogador estiver vivo (não em estado fantasma)
        if (event.isWasDeath()) {
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            
            PlayerProficiency oldProficiency = ModCapabilities.getPlayerProficiency(oldPlayer);
            PlayerProficiency newProficiency = ModCapabilities.getPlayerProficiency(newPlayer);
            
            // Copia os dados se ambas as capacidades existirem
            if (oldProficiency != null && newProficiency != null) {
                newProficiency.setEngineeringXp(oldProficiency.getEngineeringXp());
                newProficiency.setArcanaXp(oldProficiency.getArcanaXp());
                
                CronicasAetherium.LOGGER.debug("Dados de proficiência copiados para o jogador {}: {}", 
                    newPlayer.getName().getString(), newProficiency.toString());
            }
        }
    }
    
    /**
     * Evento de entrada do jogador no mundo
     * 
     * Este evento é útil para logging e verificação de que as capacidades
     * foram anexadas corretamente quando o jogador entra no mundo.
     * 
     * @param event Evento de entrada da entidade no mundo
     */
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        
        // Verifica apenas jogadores
        if (entity instanceof Player player && !event.getLevel().isClientSide()) {
            PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
            
            if (proficiency != null) {
                CronicasAetherium.LOGGER.debug("Jogador {} entrou no mundo com proficiências: {}", 
                    player.getName().getString(), proficiency.toString());
            } else {
                CronicasAetherium.LOGGER.warn("Jogador {} entrou no mundo sem capacidade de proficiência!", 
                    player.getName().getString());
            }
        }
    }
}