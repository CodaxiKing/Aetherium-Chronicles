package com.cronicasaetherium.mod.common.gui;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.common.gui.menu.SteamEngineMenu;
import com.cronicasaetherium.mod.common.gui.menu.MechanicalCrusherMenu;
import com.cronicasaetherium.mod.common.gui.menu.ManaInfuserMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro central para todos os tipos de GUI/Menu do mod Crônicas de Aetherium
 * 
 * Esta classe gerencia o registro de todos os MenuTypes customizados do mod,
 * que são necessários para criar interfaces gráficas para as máquinas.
 * 
 * MenuTypes registrados:
 * - Steam Engine: Interface do Motor a Vapor
 * - Mechanical Crusher: Interface do Triturador Mecânico  
 * - Mana Infuser: Interface da Infusora de Mana
 * - Outras máquinas conforme implementadas
 * 
 * Cada MenuType conecta o servidor (lógica) com o cliente (interface visual),
 * permitindo que os jogadores interajam com as máquinas e vejam seu estado.
 */
public class ModMenuTypes {
    
    // DeferredRegister para registro eficiente de MenuTypes
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = 
        DeferredRegister.create(Registries.MENU, CronicasAetherium.MODID);
    
    /**
     * MenuType para o Motor a Vapor
     * Permite visualizar energia, combustível, progresso de aquecimento
     */
    public static final Supplier<MenuType<SteamEngineMenu>> STEAM_ENGINE = 
        MENU_TYPES.register("steam_engine", () -> 
            IMenuTypeExtension.create(SteamEngineMenu::new));
    
    /**
     * MenuType para o Triturador Mecânico
     * Permite visualizar energia, progresso de processamento, inventário
     */
    public static final Supplier<MenuType<MechanicalCrusherMenu>> MECHANICAL_CRUSHER = 
        MENU_TYPES.register("mechanical_crusher", () -> 
            IMenuTypeExtension.create(MechanicalCrusherMenu::new));
    
    /**
     * MenuType para a Infusora de Mana
     * Permite visualizar energia, mana, progresso de conversão
     */
    public static final Supplier<MenuType<ManaInfuserMenu>> MANA_INFUSER = 
        MENU_TYPES.register("mana_infuser", () -> 
            IMenuTypeExtension.create(ManaInfuserMenu::new));
    
    /**
     * Método de registro que deve ser chamado na inicialização do mod
     * Registra o DeferredRegister no event bus do mod
     * 
     * @param modEventBus Event bus do mod para registro
     */
    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}