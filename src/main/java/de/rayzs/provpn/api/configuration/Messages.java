package de.rayzs.provpn.api.configuration;

import de.rayzs.provpn.configuration.*;
import java.util.*;

public class Messages {

    static { reload(); }
    public static void initialize() { }
    public static ConfigurationBuilder CONFIGURATION;

    public static void reload() {
        CONFIGURATION = Configurator.get("messages");
        RELOAD = (String) CONFIGURATION.getOrSet("messages.reload", "&aReloaded all configuration files!");
        NO_PERMS = (String) CONFIGURATION.getOrSet("messages.no-perms", "&cNo permissions! Missing permission: &4%MISSING%");
        DETECTED_KICK_NOTIFICATION = (String) CONFIGURATION.getOrSet("messages.detected.notification", "&4%PLAYER% &8(&4&o%ADDRESS%&8) &ctried to join through a VPN!");
        ACTIONBAR = (String) CONFIGURATION.getOrSet("messages.detected.actionbar", "&cLast detected player: &4%PLAYER% &8(&4&o%ADDRESS%&8) &8- &4%BLACKLISTED% &cblacklisted &8- &4%CONNECTIONS% &ctotal blocked connections!");
        HELP_USAGE_BLACKLIST = (String) CONFIGURATION.getOrSet("messages.blacklist.usage", "&7Usage: &f/%COMMAND% blacklist add&7/&fremove&7/&fclear address&7/&fname");
        HELP_USAGE_WHITELIST = (String) CONFIGURATION.getOrSet("messages.whitelist.usage", "&7Usage: &f/%COMMAND% whitelist add&7/&fremove&7/&fclear address&7/&fname");
        HELP_USAGE_CHECK = (String) CONFIGURATION.getOrSet("messages.check.usage", "&7Usage: &f/%COMMAND% check address");
        ONLY_PLAYERS = (String) CONFIGURATION.getOrSet("messages.only-players", "&cYou need to be an online player to execute this command!");
        BLACKLIST_ADDED = (String) CONFIGURATION.getOrSet("messages.blacklist.added", "&aAdded &2%TARGET% &ato the blacklist!");
        WHITELIST_ADDED = (String) CONFIGURATION.getOrSet("messages.whitelist.added", "&aAdded &2%TARGET% &ato the whitelist!");
        BLACKLIST_REMOVED = (String) CONFIGURATION.getOrSet("messages.blacklist.removed", "&aRemoved &2%TARGET% &afrom the blacklist!");
        WHITELIST_REMOVED = (String) CONFIGURATION.getOrSet("messages.whitelist.removed", "&aRemoved &2%TARGET% &afrom the whitelist!");
        BLACKLIST_ADD_NO = (String) CONFIGURATION.getOrSet("messages.blacklist.already-in-list", "&4%TARGET% &cis already on the blacklist!");
        WHITELIST_ADD_NO = (String) CONFIGURATION.getOrSet("messages.whitelist.already-in-list", "&4%TARGET% &cis already on the whitelist!");
        BLACKLIST_REMOVE_NO = (String) CONFIGURATION.getOrSet("messages.blacklist.not-in-list", "&4%TARGET% &cis not on the blacklist!");
        WHITELIST_REMOVE_NO = (String) CONFIGURATION.getOrSet("messages.whitelist.not-in-list", "&4%TARGET% &cis not on the whitelist!");
        BLACKLIST_LIST = (String) CONFIGURATION.getOrSet("messages.blacklist.list", "&7List: &e%LIST%");
        WHITELIST_LIST = (String) CONFIGURATION.getOrSet("messages.whitelist.list", "&7List: &e%LIST%");
        BLACKLIST_LIST_EMPTY = (String) CONFIGURATION.getOrSet("messages.blacklist.empty-list", "&cList is empty!");
        WHITELIST_LIST_EMPTY = (String) CONFIGURATION.getOrSet("messages.whitelist.empty-list", "&cList is empty!");
        BLACKLIST_CLEAR = (String) CONFIGURATION.getOrSet("messages.blacklist.clear", "&aBlacklist has been cleared completely!");
        WHITELIST_CLEAR = (String) CONFIGURATION.getOrSet("messages.whitelist.clear", "&aWhitelist has been cleared completely!");
        BLACKLIST_CLEAR_FAILED = (String) CONFIGURATION.getOrSet("messages.blacklist.clear-failed", "&cBlacklist is already empty!");
        WHITELIST_CLEAR_FAILED = (String) CONFIGURATION.getOrSet("messages.whitelist.clear-failed", "&cWhitelist is already empty!");
        STATS_ACTIVATED = (String) CONFIGURATION.getOrSet("messages.stats.activated", "&aActivated actionbar statistics!");
        STATS_DISABLED = (String) CONFIGURATION.getOrSet("messages.stats.disabled", "&aDisabled actionbar statistics!");
        NOTIFICATIONS_ACTIVATED = (String) CONFIGURATION.getOrSet("messages.notifications.activated", "&aActivated chat notifications!");
        CHECK_STATE_POSITIVE = (String) CONFIGURATION.getOrSet("messages.check.states.positive", "&aYes!");
        CHECK_STATE_NEGATIVE = (String) CONFIGURATION.getOrSet("messages.check.states.negative", "&cNo!");
        CHECK_NOT_VALID = (String) CONFIGURATION.getOrSet("messages.check.invalid", "&4%IP% &cis not a valid ip address!");
        CHECK_LOADING = (String) CONFIGURATION.getOrSet("messages.check.loading", "&eLoading data...");
        NOTIFICATIONS_DISABLED = (String) CONFIGURATION.getOrSet("messages.notifications.disabled", "&aDisabled chat notifications!");
        DETECTED_KICK_MESSAGE = (List<String>) CONFIGURATION.getOrSet("messages.detected.detected-kick-message", Arrays.asList("&cYou got detected using a VPN!", "&4Please deactivate your VPN to join the server."));
        DETECTED_BLACKLISTED_ADDRESS_KICK_MESSAGE = (List<String>) CONFIGURATION.getOrSet("messages.detected.blocked-address-kick-message", Arrays.asList("&cYour ip address is blacklisted!", "&4That means that you won't be able to join the server ever again!"));
        DETECTED_BLACKLISTED_NAME_KICK_MESSAGE = (List<String>) CONFIGURATION.getOrSet("messages.detected.blocked-name-kick-message", Arrays.asList("&cYour name is blacklisted!", "&4That means that you won't be able to join the server ever again!"));
        HELP_ALL_MESSAGE = (List<String>) CONFIGURATION.getOrSet("messages.help", Arrays.asList("&7Usage: &f/%COMMAND%",
                "&8- &freload&7/&frl &8- &7Reload the plugin files",
                "&8- &fnotify &8- &7Activate chat notifications",
                "&8- &fstats &8- &7Activate actionbar statistics",
                "&8- &fcheck address &8- &7Check an address manually",
                "&8- &fwhitelist &flist&7/&fclear &7or &fadd&7/&fremove address&7/&fname",
                "&8- &fblacklist &flist&7/&fclear &7or &fadd&7/&fremove address&7/&fname"));
        CHECK_MESSAGE = (List<String>) CONFIGURATION.getOrSet("messages.check.result", Arrays.asList("&7Informations:",
                "&7IP: &f%IP%",
                "&7Proxy/VPN: %VPN%",
                "&7Whitelisted: %WHITELISTED%",
                "&7Blacklisted: %BLACKLISTED%"));
    }

    public static String RELOAD, NO_PERMS, DETECTED_KICK_NOTIFICATION, ACTIONBAR, HELP_USAGE_BLACKLIST, HELP_USAGE_WHITELIST, HELP_USAGE_CHECK, ONLY_PLAYERS, BLACKLIST_ADDED,
            WHITELIST_ADDED, BLACKLIST_REMOVED, WHITELIST_REMOVED, BLACKLIST_ADD_NO, WHITELIST_ADD_NO, BLACKLIST_REMOVE_NO, WHITELIST_REMOVE_NO,
            BLACKLIST_CLEAR_FAILED, WHITELIST_CLEAR_FAILED, BLACKLIST_LIST, WHITELIST_LIST, BLACKLIST_LIST_EMPTY, WHITELIST_LIST_EMPTY, BLACKLIST_CLEAR, WHITELIST_CLEAR,
            STATS_ACTIVATED, STATS_DISABLED, NOTIFICATIONS_ACTIVATED, CHECK_STATE_POSITIVE, CHECK_STATE_NEGATIVE, CHECK_NOT_VALID, CHECK_LOADING, NOTIFICATIONS_DISABLED;

    public static List<String> DETECTED_KICK_MESSAGE, DETECTED_BLACKLISTED_ADDRESS_KICK_MESSAGE, DETECTED_BLACKLISTED_NAME_KICK_MESSAGE, HELP_ALL_MESSAGE, CHECK_MESSAGE;
}
