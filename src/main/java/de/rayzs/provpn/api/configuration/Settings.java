package de.rayzs.provpn.api.configuration;

import de.rayzs.provpn.configuration.*;
import java.util.*;

public class Settings {

    static { reload(); }
    public static void initialize() { }
    public static ConfigurationBuilder CONFIGURATION;

    public static void reload() {
        CONFIGURATION = Configurator.get("settings");

        UPDATE_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.update.enabled", true);
        UPDATE_DELAY = (int) CONFIGURATION.getOrSet("settings.update.delay", 20*60*15);
        API_MODE_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.api-mode", false);
        DEBUG_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.debug", false);
        BLACKLIST_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.local-saves.blacklist", true);
        WHITELIST_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.local-saves.whitelist", true);
        DETECTED_CONSOLE_COMMANDS_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.detected.console.enabled", false);
        DETECTED_CONSOLE_COMMANDS_COMMANDS = (List<String>) CONFIGURATION.getOrSet("settings.detected.console.execute-commands", Arrays.asList("ban-ip %ADDRESS% You are not allowed to use a VPN on our server!", "ban %PLAYER% You are not allowed to use a VPN on our server!"));
        DETECTED_WEBHOOK_ENABLED = (boolean) CONFIGURATION.getOrSet("settings.detected.webhook.enabled", false);
        DETECTED_WEBHOOK_AVATAR = (String) CONFIGURATION.getOrSet("settings.detected.webhook.avatar", "https://www.rayzs.de/provpn/assets/logo.png");
        DETECTED_WEBHOOK_NAME = (String) CONFIGURATION.getOrSet("settings.detected.webhook.name", "ProVPN | Bot");
        DETECTED_WEBHOOK_TITLE = (String) CONFIGURATION.getOrSet("settings.detected.webhook.title", "Detected a VPN user!");
        DETECTED_WEBHOOK_FOOTER = (String) CONFIGURATION.getOrSet("settings.detected.webhook.footer", "detected at %DATE% on %TIME%");
        DETECTED_WEBHOOK_MESSAGE = (List<String>) CONFIGURATION.getOrSet("settings.detected.webhook.fields", Arrays.asList("**PLAYER**:%%%%__%PLAYER%__", "**ADDRESS**:%%%%__%ADDRESS%__"));
        DETECTED_WEBHOOK_URLS = (List<String>) CONFIGURATION.getOrSet("settings.detected.webhook.urls", Arrays.asList("add-first-webhook-url-here", "add-second-webhook-url-here"));
    }

    public static boolean UPDATE_ENABLED, API_MODE_ENABLED, DEBUG_ENABLED, BLACKLIST_ENABLED, WHITELIST_ENABLED, DETECTED_CONSOLE_COMMANDS_ENABLED, DETECTED_WEBHOOK_ENABLED;
    public static int UPDATE_DELAY;
    public static List<String> DETECTED_CONSOLE_COMMANDS_COMMANDS, DETECTED_WEBHOOK_URLS, DETECTED_WEBHOOK_MESSAGE;
    public static String DETECTED_WEBHOOK_AVATAR, DETECTED_WEBHOOK_NAME, DETECTED_WEBHOOK_TITLE, DETECTED_WEBHOOK_FOOTER;
}
