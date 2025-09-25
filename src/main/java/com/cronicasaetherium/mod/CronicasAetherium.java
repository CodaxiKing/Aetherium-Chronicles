package com.cronicasaetherium.mod;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import com.cronicasaetherium.mod.registry.ModItems;
import com.cronicasaetherium.mod.registry.ModBlocks;
import com.cronicasaetherium.mod.registry.ModEntities;
import com.cronicasaetherium.mod.registry.ModCreativeTabs;
import com.cronicasaetherium.mod.registry.ModBlockEntities;
import com.cronicasaetherium.mod.common.commands.ProficiencyCommand;
import com.cronicasaetherium.mod.config.ModConfig;

/**
 * Classe principal do mod Crônicas de Aetherium
 * 
 * Este mod adiciona três sistemas principais ao Minecraft:
 * 1. Sistema de Criaturas e Exploração - Mobs únicos, chefes desafiadores e estruturas
 * 2. Sistema de Tecnologia e Automação - Máquinas, energia e automação
 * 3. Sistema de Magia e Misticismo - Feitiços, mana e artefatos mágicos
 * 
 * Desenvolvido para Minecraft 1.21.1 usando NeoForge
 */
@Mod(CronicasAetherium.MODID)
public class CronicasAetherium {
    
    // ID único do mod - deve corresponder ao valor em gradle.properties
    public static final String MODID = "cronicasaetherium";
    
    // Logger para depuração e informações do mod
    private static final Logger LOGGER = LogUtils.getLogger();
    
    /**
     * Construtor principal do mod
     * Inicializa todos os sistemas e registra os event buses necessários
     * 
     * @param modEventBus Bus de eventos do mod para registro de itens, blocos, etc.
     * @param modContainer Container do mod para configurações
     */
    public CronicasAetherium(IEventBus modEventBus, ModContainer modContainer) {
        // Registra os event handlers para inicialização comum
        modEventBus.addListener(this::commonSetup);
        
        // Registra todos os sistemas do mod
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        
        // Registra o event bus principal do NeoForge
        NeoForge.EVENT_BUS.register(this);
        
        // Registra o evento de comandos
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        
        // Registra configurações do mod
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ModConfig.SPEC);
        
        LOGGER.info("Crônicas de Aetherium inicializado com sucesso!");
    }
    
    /**
     * Configuração comum executada tanto no cliente quanto no servidor
     * Aqui são inicializados elementos que funcionam em ambos os lados
     * 
     * @param event Evento de configuração comum
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Executando configuração comum do Crônicas de Aetherium");
        
        // Configurações que devem ser executadas após o registro de todos os elementos
        event.enqueueWork(() -> {
            // TODO: Inicializar sistemas de spawn de mobs
            // TODO: Registrar receitas dinâmicas
            // TODO: Configurar sistemas de energia
            
            LOGGER.info("Configuração comum concluída");
        });
    }
    
    /**
     * Event handler para quando o servidor está iniciando
     * Usado para configurações específicas do servidor
     * 
     * @param event Evento de inicialização do servidor
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Crônicas de Aetherium: Servidor iniciando");
        
        // TODO: Configurar geração de estruturas
        // TODO: Inicializar sistemas de mundo
    }
    
    /**
     * Event handlers específicos do cliente
     * Só são executados no lado do cliente (não no servidor dedicado)
     */
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        
        /**
         * Configuração específica do cliente
         * Aqui são registrados renderers, telas, keybindings, etc.
         * 
         * @param event Evento de configuração do cliente
         */
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Configurando lado cliente do Crônicas de Aetherium");
            
            // TODO: Registrar renderers de entidades
            // TODO: Registrar telas de GUI
            // TODO: Configurar keybindings para magia
            
            LOGGER.info("Configuração do cliente concluída");
        }
    }
    
    /**
     * Evento de registro de comandos
     * Registra todos os comandos customizados do mod
     * 
     * @param event Evento de registro de comandos
     */
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ProficiencyCommand.register(event.getDispatcher());
        LOGGER.info("Comandos do Crônicas de Aetherium registrados");
    }
}