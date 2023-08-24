package de.rayzs.provpn.plugin.loader;

import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.api.event.ProEventManager;
import de.rayzs.provpn.api.configuration.*;
import de.rayzs.provpn.api.discord.Discord;
import de.rayzs.provpn.utils.list.PlayerList;
import de.rayzs.provpn.plugin.proevents.*;

public class PluginLoader {

    private static ProVPNBungeeLoader bungeeLoader;
    private static ProVPNBukkitLoader bukkitLoader;

    public static String LAST_NAME = "-", LAST_ADDRESS = "-";
    public static int TOTAL_BLOCKED_CONNECTIONS = 0;
    public static boolean OUTDATED_VERSION = true;

    public static void initialize(Object clazzObj, Object serverObject) {
        Reflection.initialize(serverObject);
        PlayerList.initialize();

        if(Reflection.isBungeecordServer()) bungeeLoader = (ProVPNBungeeLoader) clazzObj;
        else bukkitLoader = (ProVPNBukkitLoader) clazzObj;

        Messages.initialize();
        Settings.initialize();
        Discord.initialize();

        if(Settings.API_MODE_ENABLED) return;
        ProEventManager.addEvent(new CheckEvent());
        ProEventManager.addEvent(new DetectedEvent());
        LAST_NAME = (String) Settings.CONFIGURATION.getOrSet("last-informations.name", "-");
        LAST_ADDRESS = (String) Settings.CONFIGURATION.getOrSet("last-informations.address", "-");
        TOTAL_BLOCKED_CONNECTIONS = (int) Settings.CONFIGURATION.getOrSet("last-informations.connections",  0);
    }

    public static void setLastDetectedPlayer(String name, String address) {
        LAST_NAME = name;
        LAST_ADDRESS = address;
    }

    public static ProVPNBukkitLoader getBukkitLoader() {
        return bukkitLoader;
    }
    public static ProVPNBungeeLoader getBungeeLoader() {
        return bungeeLoader;
    }
}
