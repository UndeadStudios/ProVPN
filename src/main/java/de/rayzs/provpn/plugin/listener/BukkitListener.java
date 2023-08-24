package de.rayzs.provpn.plugin.listener;

import de.rayzs.provpn.plugin.loader.PluginLoader;
import de.rayzs.provpn.api.event.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.*;

public class BukkitListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String name = player.getName(), address = event.getAddress().getHostAddress();
        if(!event.getResult().name().contains("ALLOW")
                || Bukkit.getBanList(BanList.Type.IP).isBanned(address)
                || Bukkit.getBanList(BanList.Type.NAME).isBanned(name)) return;
        ProEventManager.triggerEvents(ProEventType.CHECKING, event, name, address);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!PluginLoader.OUTDATED_VERSION && (player.hasPermission("provpn.use") || player.hasPermission("provpn.*") || player.isOp())) {
            Bukkit.getScheduler().runTaskLater(PluginLoader.getBukkitLoader(), () -> {
                if(player.isOnline()) {
                    player.sendMessage("§8[§4ProVPN§8] §cYou're using an outdated version of this plugin!");
                    player.sendMessage("§8[§4ProVPN§8] §cPlease update it on: https://www.rayzs.de/products/provpn/page");
                }
            }, 20);
        }
    }
}
