package de.rayzs.provpn.plugin.process;

import de.rayzs.provpn.utils.reflection.Reflection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.*;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ProcessExecutor {

    private final Object executor;

    public ProcessExecutor(Object executor) {
        this.executor = executor;
    }

    public boolean isConsole() {
        return !isPlayer();
    }

    public boolean isPlayer() {
        return Reflection.isBungeecordServer() ? executor instanceof ProxiedPlayer : executor instanceof Player;
    }

    public UUID getUniqueId() {
        if(isConsole()) return null;
        return Reflection.isBungeecordServer() && executor instanceof ProxiedPlayer ? ((ProxiedPlayer) executor).getUniqueId()
                : executor instanceof Player ? ((Player) executor).getUniqueId() : null;
    }

    public boolean hasPermission(String permission) {
        if(isConsole()) return true;
        return Reflection.isBungeecordServer()
                    && executor instanceof ProxiedPlayer
                    ? ((ProxiedPlayer) executor).hasPermission(permission)
                : executor instanceof Player
                    && (((Player) executor).hasPermission(permission)
                    || ((Player) executor).isOp());
    }

    public boolean isOnline() {
        if(isConsole()) return true;
        return Reflection.isBungeecordServer()
                && executor instanceof ProxiedPlayer
                ? ((ProxiedPlayer) executor).isConnected()
                : Reflection.isBungeecordServer()
                && executor instanceof PendingConnection
                ? ((PendingConnection) executor).isConnected()
                : executor instanceof Player
                && (((Player) executor).isOnline());
    }

    public void disconnect(String disconnectMessage) {
        if(Reflection.isBungeecordServer() && executor instanceof ProxiedPlayer) ((ProxiedPlayer) executor).disconnect(disconnectMessage);
        else if(Reflection.isBungeecordServer() && executor instanceof PendingConnection) ((PendingConnection) executor).disconnect(TextComponent.fromLegacyText(disconnectMessage));
        else if(executor instanceof Player) ((Player) executor).kickPlayer(disconnectMessage);
    }

    public void sendMessage(String text) {
        if(Reflection.isBungeecordServer()) ((net.md_5.bungee.api.CommandSender) executor).sendMessage(text);
        else ((org.bukkit.command.CommandSender) executor).sendMessage(text);
    }
}
