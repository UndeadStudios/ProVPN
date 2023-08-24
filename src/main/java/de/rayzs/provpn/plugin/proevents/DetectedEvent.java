package de.rayzs.provpn.plugin.proevents;

import de.rayzs.provpn.plugin.process.ProcessExecutor;
import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.plugin.loader.PluginLoader;
import org.bukkit.event.player.PlayerLoginEvent;
import de.rayzs.provpn.api.configuration.*;
import de.rayzs.provpn.api.discord.Discord;
import de.rayzs.provpn.utils.list.PlayerList;
import de.rayzs.provpn.plugin.logger.Logger;
import net.md_5.bungee.api.event.LoginEvent;
import de.rayzs.provpn.api.event.*;
import java.util.*;

public class DetectedEvent implements ProEvent {

    @Override
    public ProEventType type() { return ProEventType.DETECTED; }

    @Override
    public void execute(Object... objects) {
        PluginLoader.TOTAL_BLOCKED_CONNECTIONS++;
        StringBuilder kickMessageBuilder = new StringBuilder();
        List<String> kickMessageLines = null;
        String name = (String) objects[1],
                address = (String) objects[2],
                kickMessage,
                kickNotification = Messages.DETECTED_KICK_NOTIFICATION.replace("%PLAYER%", name).replace("%ADDRESS%", address);

        PluginLoader.setLastDetectedPlayer(name, address);

        if(Settings.BLACKLIST_ENABLED) {
            if(PlayerList.contains(PlayerList.ListType.BLACKLIST, address))
                kickMessageLines = Messages.DETECTED_BLACKLISTED_ADDRESS_KICK_MESSAGE;
            else if(PlayerList.contains(PlayerList.ListType.BLACKLIST, name))
                kickMessageLines = Messages.DETECTED_BLACKLISTED_NAME_KICK_MESSAGE;
            else {
                if(Settings.DETECTED_WEBHOOK_ENABLED) Discord.sendEmbed(name, address);
                PlayerList.add(PlayerList.ListType.BLACKLIST, address);
            }
        }

        if(kickMessageLines == null) kickMessageLines = Messages.DETECTED_KICK_MESSAGE;

        if(kickMessageLines == Messages.DETECTED_KICK_MESSAGE) {
            Logger.info(kickNotification);
            if (Reflection.isBungeecordServer()) PluginLoader.getBungeeLoader().broadcastMessage(kickNotification);
            else PluginLoader.getBukkitLoader().broadcastMessage(kickNotification);

            if(Settings.DETECTED_CONSOLE_COMMANDS_ENABLED) {
                for (String command : Settings.DETECTED_CONSOLE_COMMANDS_COMMANDS) {
                    command = command.replace("%PLAYER%", name).replace("%ADDRESS%", address);
                    if (Reflection.isBungeecordServer()) PluginLoader.getBungeeLoader().executeConsoleCommand(command);
                    else PluginLoader.getBukkitLoader().executeConsoleCommand(command);
                }
            }
        }

        for (int i = 0; i < kickMessageLines.size(); i++) {
            String kickMessageLine = kickMessageLines.get(i).replace("%PLAYER%", name).replace("%ADDRESS%", address).replace("&", "§");
            kickMessageBuilder.append(kickMessageLine);
            if(i < kickMessageLines.size()-1) kickMessageBuilder.append("\n");
        } kickMessage = kickMessageBuilder.toString();

        if(Reflection.isBungeecordServer()) {
            LoginEvent event = (LoginEvent) objects[0];
            ProcessExecutor player = new ProcessExecutor(event.getConnection());
            Logger.debug("Trying to cancel proxy-players connection with cancelling " + event.getClass().getSimpleName() + " event... (player=" + name + ", address=" + address + ")");
            event.setCancelled(true);
            event.setCancelReason(kickMessage);
            if(!player.isOnline()) {
                Logger.debug("Successfully cancelled proxy-players connection! (player=" + name + ", address=" + address + ")");
                return;
            }
            Logger.debug("Failed to cancel proxy-players connection! Kicking him manually. (player=" + name + ", address=" + address + ")");
            player.disconnect(kickMessage);
        } else {
            PlayerLoginEvent event = (PlayerLoginEvent) objects[0];
            ProcessExecutor player = new ProcessExecutor(event.getPlayer());
            Logger.debug("Trying to cancel players connection with cancelling " + event.getClass().getSimpleName() + " event... (player=" + name + ", address=" + address + ")");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(kickMessage);
            if(!player.isOnline()) {
                Logger.debug("Successfully cancelled players connection! (player=" + name + ", address=" + address + ")");
                return;
            }
            Logger.debug("Failed to cancel players connection! Kicking him manually. (player=" + name + ", address=" + address + ")");
            player.disconnect(kickMessage);
        }
    }
}
