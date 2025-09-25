package com.cronicasaetherium.mod.common.commands;

import com.cronicasaetherium.mod.common.capability.ModCapabilities;
import com.cronicasaetherium.mod.common.capability.PlayerProficiency;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

/**
 * Comandos de administrador para o sistema de proficiência
 * 
 * Esta classe implementa comandos de console para testar e administrar
 * o sistema de proficiência dos jogadores. Os comandos permitem visualizar
 * e modificar os pontos de experiência de engenharia e arcana.
 * 
 * Comandos disponíveis:
 * - /proficiency get <jogador> <tipo> - Visualiza XP atual
 * - /proficiency add <jogador> <tipo> <quantidade> - Adiciona/remove XP
 * - /proficiency set <jogador> <tipo> <quantidade> - Define XP específico
 * - /proficiency reset <jogador> [tipo] - Reseta proficiências
 * 
 * Tipos válidos: engenharia, arcana
 */
public class ProficiencyCommand {
    
    // Constantes para tipos de proficiência válidos
    private static final String ENGINEERING_TYPE = "engenharia";
    private static final String ARCANA_TYPE = "arcana";
    
    /**
     * Registra todos os comandos de proficiência
     * 
     * Este método deve ser chamado durante o evento de registro de comandos
     * para disponibilizar os comandos no servidor.
     * 
     * @param dispatcher Dispatcher de comandos do servidor
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("proficiency")
                .requires(source -> source.hasPermission(2)) // Requer nível de OP 2
                .then(
                    Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.player())
                            .then(Commands.argument("type", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    builder.suggest(ENGINEERING_TYPE);
                                    builder.suggest(ARCANA_TYPE);
                                    return builder.buildFuture();
                                })
                                .executes(ProficiencyCommand::getProficiency)
                            )
                        )
                )
                .then(
                    Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                            .then(Commands.argument("type", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    builder.suggest(ENGINEERING_TYPE);
                                    builder.suggest(ARCANA_TYPE);
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                    .executes(ProficiencyCommand::addProficiency)
                                )
                            )
                        )
                )
                .then(
                    Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                            .then(Commands.argument("type", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    builder.suggest(ENGINEERING_TYPE);
                                    builder.suggest(ARCANA_TYPE);
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                    .executes(ProficiencyCommand::setProficiency)
                                )
                            )
                        )
                )
                .then(
                    Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(ProficiencyCommand::resetAllProficiency)
                            .then(Commands.argument("type", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    builder.suggest(ENGINEERING_TYPE);
                                    builder.suggest(ARCANA_TYPE);
                                    return builder.buildFuture();
                                })
                                .executes(ProficiencyCommand::resetSpecificProficiency)
                            )
                        )
                )
                .then(
                    Commands.literal("list")
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(ProficiencyCommand::listAllProficiencies)
                        )
                )
        );
    }
    
    /**
     * Comando: /proficiency get <jogador> <tipo>
     * Exibe a quantidade atual de XP de uma proficiência específica
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando (1 = sucesso, 0 = falha)
     */
    private static int getProficiency(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String type = StringArgumentType.getString(context, "type").toLowerCase();
        
        if (!isValidProficiencyType(type)) {
            context.getSource().sendFailure(Component.literal("Tipo de proficiência inválido! Use: " + ENGINEERING_TYPE + " ou " + ARCANA_TYPE));
            return 0;
        }
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        int xp = proficiency.getXpByType(type);
        int level = proficiency.calculateLevel(xp);
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("§6%s§r tem §b%d XP§r de §e%s§r (Nível §a%d§r)", 
                player.getName().getString(), xp, type, level)
        ), true);
        
        return 1;
    }
    
    /**
     * Comando: /proficiency add <jogador> <tipo> <quantidade>
     * Adiciona (ou remove se negativo) XP de uma proficiência
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando
     */
    private static int addProficiency(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String type = StringArgumentType.getString(context, "type").toLowerCase();
        int amount = IntegerArgumentType.getInteger(context, "amount");
        
        if (!isValidProficiencyType(type)) {
            context.getSource().sendFailure(Component.literal("Tipo de proficiência inválido! Use: " + ENGINEERING_TYPE + " ou " + ARCANA_TYPE));
            return 0;
        }
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        int oldXp = proficiency.getXpByType(type);
        boolean success = proficiency.addXpByType(type, amount);
        
        if (!success) {
            context.getSource().sendFailure(Component.literal("Erro ao modificar proficiência"));
            return 0;
        }
        
        int newXp = proficiency.getXpByType(type);
        int newLevel = proficiency.calculateLevel(newXp);
        
        String action = amount >= 0 ? "adicionado" : "removido";
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("§6%s XP§r %s para §e%s§r de §6%s§r. Total: §b%d XP§r (Nível §a%d§r)", 
                Math.abs(amount), action, type, player.getName().getString(), newXp, newLevel)
        ), true);
        
        return 1;
    }
    
    /**
     * Comando: /proficiency set <jogador> <tipo> <quantidade>
     * Define a quantidade exata de XP de uma proficiência
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando
     */
    private static int setProficiency(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String type = StringArgumentType.getString(context, "type").toLowerCase();
        int amount = IntegerArgumentType.getInteger(context, "amount");
        
        if (!isValidProficiencyType(type)) {
            context.getSource().sendFailure(Component.literal("Tipo de proficiência inválido! Use: " + ENGINEERING_TYPE + " ou " + ARCANA_TYPE));
            return 0;
        }
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        if (type.equals(ENGINEERING_TYPE)) {
            proficiency.setEngineeringXp(amount);
        } else {
            proficiency.setArcanaXp(amount);
        }
        
        int level = proficiency.calculateLevel(amount);
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("Proficiência §e%s§r de §6%s§r definida para §b%d XP§r (Nível §a%d§r)", 
                type, player.getName().getString(), amount, level)
        ), true);
        
        return 1;
    }
    
    /**
     * Comando: /proficiency reset <jogador>
     * Reseta todas as proficiências do jogador para 0
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando
     */
    private static int resetAllProficiency(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        proficiency.resetAll();
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("Todas as proficiências de §6%s§r foram resetadas", player.getName().getString())
        ), true);
        
        return 1;
    }
    
    /**
     * Comando: /proficiency reset <jogador> <tipo>
     * Reseta uma proficiência específica do jogador para 0
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando
     */
    private static int resetSpecificProficiency(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String type = StringArgumentType.getString(context, "type").toLowerCase();
        
        if (!isValidProficiencyType(type)) {
            context.getSource().sendFailure(Component.literal("Tipo de proficiência inválido! Use: " + ENGINEERING_TYPE + " ou " + ARCANA_TYPE));
            return 0;
        }
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        if (type.equals(ENGINEERING_TYPE)) {
            proficiency.setEngineeringXp(0);
        } else {
            proficiency.setArcanaXp(0);
        }
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("Proficiência §e%s§r de §6%s§r foi resetada", type, player.getName().getString())
        ), true);
        
        return 1;
    }
    
    /**
     * Comando: /proficiency list <jogador>
     * Lista todas as proficiências do jogador
     * 
     * @param context Contexto do comando
     * @return Código de retorno do comando
     */
    private static int listAllProficiencies(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        
        PlayerProficiency proficiency = ModCapabilities.getPlayerProficiency(player);
        if (proficiency == null) {
            context.getSource().sendFailure(Component.literal("Não foi possível acessar as proficiências do jogador " + player.getName().getString()));
            return 0;
        }
        
        int engXp = proficiency.getEngineeringXp();
        int engLevel = proficiency.getEngineeringLevel();
        int arcXp = proficiency.getArcanaXp();
        int arcLevel = proficiency.getArcanaLevel();
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("§6=== Proficiências de %s ===", player.getName().getString())
        ), false);
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("§e⚙ Engenharia:§r §b%d XP§r (Nível §a%d§r)", engXp, engLevel)
        ), false);
        
        context.getSource().sendSuccess(() -> Component.literal(
            String.format("§e✨ Arcana:§r §b%d XP§r (Nível §a%d§r)", arcXp, arcLevel)
        ), false);
        
        return 1;
    }
    
    /**
     * Verifica se um tipo de proficiência é válido
     * 
     * @param type Tipo a verificar
     * @return true se for válido, false caso contrário
     */
    private static boolean isValidProficiencyType(String type) {
        return ENGINEERING_TYPE.equals(type) || ARCANA_TYPE.equals(type);
    }
}