package one.lbs.velocitymcdrcommand.command;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import one.lbs.velocitymcdrcommand.VelocityMCDRCommand;

import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;

@Singleton
public class MCDReforgedCommand implements SuggestionProvider<CommandSource>, com.mojang.brigadier.Command<CommandSource>{
    @Inject
    VelocityMCDRCommand pluginInstance;

    private final ArrayList<CommandMeta> registeredCommands = new ArrayList<>();

    public BrigadierCommand createBrigadierCommand() {
        LiteralCommandNode<CommandSource> llsMCDReforgedRegisterCommand = createSubCommand().build();
        return new BrigadierCommand(llsMCDReforgedRegisterCommand);
    }

    public LiteralArgumentBuilder<CommandSource> createSubCommand() {
        return LiteralArgumentBuilder
                .<CommandSource>literal("mcdr").requires(
                        commandSource -> commandSource instanceof ConsoleCommandSource
                ).then(LiteralArgumentBuilder.<CommandSource>literal("register").then(
                        RequiredArgumentBuilder.<CommandSource, String>argument("data", StringArgumentType.greedyString())
                                .executes(this))
                );
    }

    @Override
    public int run(CommandContext<CommandSource> commandContext) {
        CommandManager commandManager = pluginInstance.server.getCommandManager();

        try {
            for (CommandMeta commandMeta: registeredCommands) {
                commandManager.unregister(commandMeta);
            }
            registeredCommands.clear();
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(StringArgumentType.getString(commandContext, "data"));
            JSONObject[] var14 = (JSONObject[])jsonObject.getJSONArray("data").toArray(JSONObject.class, new JSONReader.Feature[0]);

            for (JSONObject nodeJsonObject : var14) {
                Node node = new Node(nodeJsonObject);
                BrigadierCommand brigadierCommand = node.createBrigadierCommand(pluginInstance);
                CommandMeta commandMeta = commandManager.metaBuilder(brigadierCommand).build();
                this.registeredCommands.add(commandMeta);
                commandManager.register(commandMeta, brigadierCommand);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            return 0;
        }
        pluginInstance.logger.info("Updated MCDReforged command tree suggestion successfully");
        return 1;
    }

    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<CommandSource> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
        return builder.buildFuture();
    }
}
