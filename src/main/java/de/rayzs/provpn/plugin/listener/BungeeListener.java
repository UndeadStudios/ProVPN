package de.rayzs.provpn.plugin.listener;

import de.rayzs.provpn.plugin.loader.PluginLoader;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.*;
import de.rayzs.provpn.api.event.*;
import net.md_5.bungee.event.*;

import java.util.concurrent.TimeUnit;

public class BungeeListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();
        String name = connection.getName(), address = connection.getAddress().getHostString();
        ProEventManager.triggerEvents(ProEventType.CHECKING, event, name, address);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!PluginLoader.OUTDATED_VERSION && (player.hasPermission("provpn.use") || player.hasPermission("provpn.*"))) {
            ProxyServer.getInstance().getScheduler().schedule(PluginLoader.getBungeeLoader(), () -> {
                if(player.isConnected()) {
                    player.sendMessage("§8[§4ProVPN§8] §cYou're using an outdated version of this plugin!");
                    player.sendMessage("§8[§4ProVPN§8] §cPlease update it on: https://www.rayzs.de/products/provpn/page");
                }
            }, 1, TimeUnit.SECONDS);
        }
    }
}
