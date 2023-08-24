package de.rayzs.provpn.plugin.logger;

import de.rayzs.provpn.api.configuration.Settings;
import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.plugin.loader.PluginLoader;
import net.md_5.bungee.api.ProxyServer;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class Logger {

    private final static java.util.logging.Logger LOGGER =
            Reflection.isBungeecordServer()
            ? PluginLoader.getBungeeLoader().getLogger()
            : PluginLoader.getBukkitLoader().getLogger();

    public static void info(String text) { send(Priority.INFO, text); }
    public static void warning(String text) { send(Priority.WARNING, text); }
    public static void debug(String text) {
        if(!Settings.DEBUG_ENABLED) return;
        send(Priority.INFO, text);
    }

    protected static void send(Priority priority, String text) {
        boolean hasColors = text.contains("§");
        if(hasColors) {
            if (Reflection.isBungeecordServer()) ProxyServer.getInstance().getConsole().sendMessage(text);
            else Bukkit.getServer().getConsoleSender().sendMessage(text);
            return;
        }

        Level level = priority == Priority.WARNING ? Level.WARNING : Level.INFO;
        LOGGER.log(level, text);
    }

    protected enum Priority { INFO, WARNING }
}
