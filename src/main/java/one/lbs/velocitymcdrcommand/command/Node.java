package one.lbs.velocitymcdrcommand.command;

import com.alibaba.fastjson2.JSONObject;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import one.lbs.velocitymcdrcommand.VelocityMCDRCommand;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.Objects;

public class Node {
    private final String name;
    private final String type;
    private final ArrayList<Node> children = new ArrayList<>();

    public Node(JSONObject jsonObject) {
        name = jsonObject.getString("name");
        type = jsonObject.getString("type");
        for (JSONObject child : jsonObject.getJSONArray("children").toArray(JSONObject.class)) {
            children.add(new Node(child));
        }
    }

    public String getName() {
        return name;
    }

    public BrigadierCommand createBrigadierCommand(VelocityMCDRCommand pluginInstance) {
        return new BrigadierCommand(getRootArgumentBuilder(pluginInstance).build());
    }

    public LiteralArgumentBuilder<CommandSource> getRootArgumentBuilder(VelocityMCDRCommand pluginInstance) {
        return (LiteralArgumentBuilder<CommandSource>) getArgumentBuilder(pluginInstance).executes(
                commandContext -> {
                    Player player = (Player) commandContext.getSource();
                    String username = player.getUsername();
                    String message = commandContext.getInput();
                    player.getCurrentServer().ifPresent((serverConnection) -> pluginInstance.logger.info(String.format("[%s] <%s> %s", serverConnection.getServerInfo().getName(), username, message)));
                    return 0;
                }
        );
    }

    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder(VelocityMCDRCommand pluginInstance) {
        /* For Java 14+ :<
        ArgumentBuilder<CommandSource, ?> argumentBuilder = switch (type) {
            case "LITERAL" -> LiteralArgumentBuilder.literal(name);
            case "INTEGER" -> RequiredArgumentBuilder.argument(name, IntegerArgumentType.integer());
            case "FLOAT" -> RequiredArgumentBuilder.argument(name, DoubleArgumentType.doubleArg());
            case "QUOTABLE_TEXT" -> RequiredArgumentBuilder.argument(name, StringArgumentType.string());
            case "GREEDY_TEXT" -> RequiredArgumentBuilder.argument(name, StringArgumentType.greedyString());
            default ->
                // NUMBER, TEXT, BOOLEAN, ENUMERATION, etc...
                    RequiredArgumentBuilder.argument(name, StringArgumentType.word());
        }; */

        // For Java 11 :>
        ArgumentBuilder<CommandSource, ?> argumentBuilder;
        if (Objects.equals(type, "LITERAL")) {
            argumentBuilder = LiteralArgumentBuilder.literal(name);
        } else if (Objects.equals(type, "INTEGER")) {
            argumentBuilder = RequiredArgumentBuilder.argument(name, IntegerArgumentType.integer());
        } else if (Objects.equals(type, "FLOAT")) {
            argumentBuilder = RequiredArgumentBuilder.argument(name, DoubleArgumentType.doubleArg());
        } else if (Objects.equals(type, "QUOTABLE_TEXT")) {
            argumentBuilder = RequiredArgumentBuilder.argument(name, StringArgumentType.string());
        } else if (Objects.equals(type, "GREEDY_TEXT")) {
            argumentBuilder = RequiredArgumentBuilder.argument(name, StringArgumentType.greedyString());
        } else {
            argumentBuilder = RequiredArgumentBuilder.argument(name, StringArgumentType.word());
        }
        argumentBuilder.executes(
                commandContext -> {
                    Player player = (Player) commandContext.getSource();
                    String username = player.getUsername();
                    String message = commandContext.getInput();
                    player.getCurrentServer().ifPresent((serverConnection) -> pluginInstance.logger.info(String.format("[%s] <%s> %s", serverConnection.getServerInfo().getName(), username, message)));
                    return 1;
                }
        );
        for (Node child : children) {
            argumentBuilder.then(child.getArgumentBuilder(pluginInstance));
        }
        return argumentBuilder;
    }
}
