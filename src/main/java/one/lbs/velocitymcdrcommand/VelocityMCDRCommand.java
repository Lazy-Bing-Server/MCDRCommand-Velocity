package one.lbs.velocitymcdrcommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import one.lbs.velocitymcdrcommand.command.AlertRawCommand;
import one.lbs.velocitymcdrcommand.command.MCDReforgedCommand;
import org.slf4j.Logger;

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

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register(injector.getInstance(MCDReforgedCommand.class).createBrigadierCommand());
        AlertRawCommand.register(this);
    }

    @Subscribe
    public void onPlayerChatEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String username = player.getUsername();

        player.getCurrentServer().ifPresent((serverConnection) -> logger.info(String.format("[%s] <%s> %s", serverConnection.getServerInfo().getName(), username, message)));
    }
}
