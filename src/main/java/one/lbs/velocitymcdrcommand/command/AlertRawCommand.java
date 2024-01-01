package one.lbs.velocitymcdrcommand.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import one.lbs.velocitymcdrcommand.VelocityMCDRCommand;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import javax.annotation.Nullable;
import java.util.Optional;


public class AlertRawCommand {
    public static @Nullable CommandMeta registeredMeta = null;

    public static void register(VelocityMCDRCommand pluginInst) {
        CommandManager commandManager = pluginInst.server.getCommandManager();
        BrigadierCommand command = createBrigadierCommand(pluginInst);
        registeredMeta = commandManager.metaBuilder(command).build();
        commandManager.register(registeredMeta, command);
        pluginInst.logger.info("Enabled alert raw command");
    }

    public static void unregister(VelocityMCDRCommand pluginInst) {
        if (registeredMeta != null) {
            pluginInst.server.getCommandManager().unregister(registeredMeta);
        }
        pluginInst.logger.info("Disabled alert raw command");
    }


    private static BrigadierCommand createBrigadierCommand(VelocityMCDRCommand pluginInst) {
        LiteralCommandNode<CommandSource> alertRawNode = LiteralArgumentBuilder.<CommandSource>literal("alertraw")
                .requires(commandSource -> commandSource.hasPermission("velocitymcdrcommand.command.alertraw"))
                .then(LiteralArgumentBuilder.<CommandSource>literal("@a")
                        .then(getSendComponentBuilder(pluginInst).executes(
                                context -> {
                                    Component textComponent = GsonComponentSerializer.gson().deserialize(context.getArgument("component", String.class));
                                    for (Player player : pluginInst.server.getAllPlayers()) {
                                        player.sendMessage(textComponent);
                                    }
                                    return 0;
                                }
                        )))
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                        .then(getSendComponentBuilder(pluginInst).executes(
                                context -> {
                                    Component textComponent = GsonComponentSerializer.gson().deserialize(context.getArgument("component", String.class));
                                    String playerName = context.getArgument("target", String.class);
                                    Optional<Player> player = pluginInst.server.getPlayer(playerName);
                                    if (player.isPresent()) {
                                        player.get().sendMessage(textComponent);
                                        return 0;
                                    } else {
                                        return 1;
                                    }
                                }
                        ))
                ).build();
        return new BrigadierCommand(alertRawNode);
    }

    private static RequiredArgumentBuilder<CommandSource, String> getSendComponentBuilder(VelocityMCDRCommand pluginInst) {
        return RequiredArgumentBuilder.argument("component", StringArgumentType.greedyString());
    }
}
