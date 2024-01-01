package one.lbs.velocitymcdrcommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import one.lbs.velocitymcdrcommand.command.AlertRawCommand;
import one.lbs.velocitymcdrcommand.command.MCDReforgedCommand;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Optional;

@Plugin(
        id = "velocitymcdrcommand",
        name = "VelocityMCDRCommand",
        version = "1.0-SNAPSHOT",
        url = "http://github.com/Lazy-Bing-Server/MCDRCommand-Velocity",
        description = "Make velocity more compatible with MCDReforged",
        authors = {"Ra1ny_Yuki"}
)
public class VelocityMCDRCommand {
    @Inject
    public Logger logger;
    @Inject
    public ProxyServer server;
    @Inject
    public Injector injector;
    @Inject
    @DataDirectory
    public Path dataFolderPath;
    public Config config;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (!Files.exists(dataFolderPath)) {
            try {
                Files.createDirectories(dataFolderPath);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("VelocityProxyWhitelist load fail! createDirectories {} fail!!", dataFolderPath);
            }
        }
        loadConfig();
    }

    private void loadConfig() {
        config = new Config(this, dataFolderPath.resolve(Paths.get("config.json")));
        if (!config.load()) {
            logger.error("VelocityMCDRCommand load config fail!");
            config.save();
            // throw new IllegalStateException("VelocityMCDRCommand init fail");
        }

        if (config.getData().enableMCDRCommandSuggestion) {
            injector.getInstance(MCDReforgedCommand.class).registerSelf();
        } else {
            injector.getInstance(MCDReforgedCommand.class).unregisterSelf();
        }
        if (config.getData().enableAlertRaw) {
            AlertRawCommand.register(this);
        } else {
            AlertRawCommand.unregister(this);
        }
        logger.info("Config loaded!");
    }

    @Subscribe
    public void proxyReloadEventHandler(ProxyReloadEvent event) {
        loadConfig();
    }

    @Subscribe
    public void onPlayerChatEvent(PlayerChatEvent event) {
        if (!config.getData().enablePrintPlayerChat) {
            return;
        }
        Player player = event.getPlayer();
        String message = event.getMessage();
        String username = player.getUsername();

        player.getCurrentServer().ifPresent(
                (serverConnection -> {
                    Object[] objects = new Object[]{serverConnection.getServerInfo().getName(), username, message};
                    logger.info(MessageFormat.format(config.getData().playerChatLogFormat, objects));
                })
        );
    }
}
