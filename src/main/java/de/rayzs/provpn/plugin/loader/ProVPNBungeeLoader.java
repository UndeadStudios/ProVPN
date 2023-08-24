package de.rayzs.provpn.plugin.loader;

import de.rayzs.provpn.plugin.metrics.BungeeMetrics;
import de.rayzs.provpn.utils.builder.ConnectionBuilder;
import de.rayzs.provpn.plugin.listener.BungeeListener;
import de.rayzs.provpn.api.configuration.Settings;
import de.rayzs.provpn.api.configuration.Messages;
import de.rayzs.provpn.plugin.command.BungeeCommand;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import de.rayzs.provpn.api.actionbar.Actionbar;
import de.rayzs.provpn.utils.address.AddressUtils;
import de.rayzs.provpn.utils.list.PlayerList;
import de.rayzs.provpn.plugin.logger.Logger;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.*;
import de.rayzs.provpn.api.event.*;
import java.util.*;

public class ProVPNBungeeLoader extends Plugin {

    public static ProVPNBungeeLoader instance;
    private final List<Class<?>> EVENTS = Arrays.asList( BungeeListener.class);
    private ScheduledTask scheduledTask;

    @Override
    public void onEnable() {
        instance = this;

        PluginLoader.initialize(this, getProxy());
        registerEvents();
        registerCommands();

        getProxy().getScheduler().schedule(this, () -> {
            List<String> verbosePlayers = PlayerList.getListByType(PlayerList.ListType.VERBOSE),
                    blacklistedPlayers = PlayerList.getListByType(PlayerList.ListType.BLACKLIST);;
            if(blacklistedPlayers != null && verbosePlayers != null && !verbosePlayers.isEmpty()) {
                String actionbarMessage = Messages.ACTIONBAR.replace("%PLAYER%", PluginLoader.LAST_NAME)
                        .replace("%ADDRESS%", PluginLoader.LAST_ADDRESS)
                        .replace("%BLACKLISTED%", String.valueOf(blacklistedPlayers.size()))
                        .replace("%CONNECTIONS%", String.valueOf(PluginLoader.TOTAL_BLOCKED_CONNECTIONS));
                getProxy().getPlayers().stream().filter(player -> verbosePlayers.contains(player.getUniqueId().toString())).forEach(player -> Actionbar.execute(player, actionbarMessage));
            }
        }, 1, 1, TimeUnit.SECONDS);

        new BungeeMetrics(this, 18008);
        startUpdaterTask();
        Logger.info("Successfully loaded!");
    }

    @Override
    public void onDisable() {
        getProxy().getScheduler().cancel(this);
        Logger.info("Saving all files...");
        PlayerList.save();
        Settings.reload();
        Settings.CONFIGURATION.setAndSave("last-informations.name", PluginLoader.LAST_NAME);
        Settings.CONFIGURATION.setAndSave("last-informations.address", PluginLoader.LAST_ADDRESS);
        Settings.CONFIGURATION.setAndSave("last-informations.connections", PluginLoader.TOTAL_BLOCKED_CONNECTIONS);
        Logger.info("Done!");
    }

    private void registerCommands() {
        Arrays.asList(new String[] { "provpn", "pvpn", "pv" }).forEach(commandName -> {
            BungeeCommand command = new BungeeCommand(commandName);
            getProxy().getPluginManager().registerCommand(this, command);
        });
    }

    private void registerEvents() {
        PluginManager pluginManager = getProxy().getPluginManager();
        this.EVENTS.forEach(clazz -> {
            try {
                Listener listener = (Listener) clazz.newInstance();
                pluginManager.registerListener(this, listener);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void startUpdaterTask() {
        if (!Settings.UPDATE_ENABLED) return;
        scheduledTask = getProxy().getScheduler().schedule(this, () -> {
            String result = new ConnectionBuilder().setUrl("https://www.rayzs.de/provpn/api/version.php")
                    .setProperties("ProVPN", "1121").connect().getResponse();
            if (!result.equals(getDescription().getVersion())) {
                getProxy().getScheduler().cancel(scheduledTask);
                if (result.equals("unknown")) {
                    Logger.warning("Failed reaching web host! (firewall enabled? website down?)");
                } else if (result.equals("exception")) {
                    Logger.warning("Failed creating web instance! (outdated java version?)");
                } else {
                    PluginLoader.OUTDATED_VERSION = false;
                    Logger.warning("You're using an outdated version of this plugin!");
                    Logger.warning("Please update it on: https://www.rayzs.de/products/provpn/page");
                }
            }
        }, 20L, Settings.UPDATE_DELAY, TimeUnit.SECONDS);
    }

    public void proxyRunnableCheck(String name, String address, Object[] objects) {
        getProxy().getScheduler().runAsync(this, () -> {
            boolean isProxy = AddressUtils.isVirtualProxy(address);
            Logger.debug("Virtual proxy result of player=" + name + " (" + address + "): " + (isProxy ? "positive" : "negative"));
            ProEventManager.triggerEvents(isProxy ? ProEventType.DETECTED : ProEventType.ALLOWED, objects);
        });
    }

    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        getProxy().getPlayers().forEach(player -> names.add(player.getName()));
        return names;
    }

    public void executeConsoleCommand(String command) {
        getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), command);
    }

    public void broadcastMessage(String message) {
        List<String> notifyPlayers = PlayerList.getListByType(PlayerList.ListType.NOTIFY);
        if(notifyPlayers == null || notifyPlayers.isEmpty()) return;
        getProxy().getPlayers().stream().filter(player -> notifyPlayers.contains(player.getUniqueId().toString())).forEach(player -> player.sendMessage(message));
    }

    public static ProVPNBungeeLoader getInstance() {
        return instance;
    }
}
