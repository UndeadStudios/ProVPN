package de.rayzs.provpn.plugin.loader;

import de.rayzs.provpn.plugin.metrics.BukkitMetrics;
import de.rayzs.provpn.utils.builder.ConnectionBuilder;
import de.rayzs.provpn.plugin.listener.BukkitListener;
import de.rayzs.provpn.plugin.command.BukkitCommand;
import de.rayzs.provpn.api.actionbar.Actionbar;
import de.rayzs.provpn.utils.address.AddressUtils;
import de.rayzs.provpn.api.event.ProEventManager;
import de.rayzs.provpn.api.configuration.*;
import de.rayzs.provpn.api.event.ProEventType;
import de.rayzs.provpn.utils.list.PlayerList;
import de.rayzs.provpn.plugin.logger.Logger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.util.*;

public class ProVPNBukkitLoader extends JavaPlugin {

    private static ProVPNBukkitLoader instance;
    private final List<Class<?>> EVENTS = Arrays.asList( BukkitListener.class);
    private int updaterTaskId;

    @Override
    public void onEnable() {
        instance = this;

        PluginLoader.initialize(this, getServer());
        registerEvents();
        registerCommands();

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            List<String> verbosePlayers = PlayerList.getListByType(PlayerList.ListType.VERBOSE),
                    blacklistedPlayers = PlayerList.getListByType(PlayerList.ListType.BLACKLIST);;
            if(blacklistedPlayers != null && verbosePlayers != null && !verbosePlayers.isEmpty()) {
                String actionbarMessage = Messages.ACTIONBAR.replace("%PLAYER%", PluginLoader.LAST_NAME)
                        .replace("%ADDRESS%", PluginLoader.LAST_ADDRESS)
                        .replace("%BLACKLISTED%", String.valueOf(blacklistedPlayers.size()))
                        .replace("%CONNECTIONS%", String.valueOf(PluginLoader.TOTAL_BLOCKED_CONNECTIONS));
                getServer().getOnlinePlayers().stream().filter(player -> verbosePlayers.contains(player.getUniqueId().toString())).forEach(player -> Actionbar.execute(player, actionbarMessage));
            }
        }, 20, 20);

        new BukkitMetrics(this, 18007);
        startUpdaterTask();
        Logger.info("Successfully loaded!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        Logger.info("Saving all files...");
        PlayerList.save();
        Settings.reload();
        Settings.CONFIGURATION.setAndSave("last-informations.name", PluginLoader.LAST_NAME);
        Settings.CONFIGURATION.setAndSave("last-informations.address", PluginLoader.LAST_ADDRESS);
        Settings.CONFIGURATION.setAndSave("last-informations.connections", PluginLoader.TOTAL_BLOCKED_CONNECTIONS);
        Logger.info("Done!");
    }

    private void registerCommands() {
        BukkitCommand command = new BukkitCommand();
        Arrays.asList(new String[] { "provpn", "pvpn", "pv" }).forEach(commandName -> {
            PluginCommand pluginCommand = getCommand(commandName);
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        });
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.EVENTS.forEach(clazz -> {
            try {
                Listener listener = (Listener) clazz.newInstance();
                pluginManager.registerEvents(listener, this);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void startUpdaterTask() {
        if (!Settings.UPDATE_ENABLED) return;
        updaterTaskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            String result = new ConnectionBuilder().setUrl("https://www.rayzs.de/provpn/api/version.php")
                    .setProperties("ProVPN", "1121").connect().getResponse();
            if (!result.equals(getDescription().getVersion())) {
                Bukkit.getScheduler().cancelTask(this.updaterTaskId);
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
        }, 20L, 20L * Settings.UPDATE_DELAY);
    }

    public void proxyRunnableCheck(String name, String address, Object[] objects) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean isProxy = AddressUtils.isVirtualProxy(address);
                Logger.debug("Virtual proxy result of player=" + name + " (" + address + "): " + (isProxy ? "positive" : "negative"));
                ProEventManager.triggerEvents(isProxy ? ProEventType.DETECTED : ProEventType.ALLOWED, objects);
            }
        }.run();
    }

    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
        return names;
    }

    public void executeConsoleCommand(String command) {
        getServer().dispatchCommand(getServer().getConsoleSender(), command);
    }

    public void broadcastMessage(String message) {
        List<String> notifyPlayers = PlayerList.getListByType(PlayerList.ListType.NOTIFY);
        if(notifyPlayers == null || notifyPlayers.isEmpty()) return;
        getServer().getOnlinePlayers().stream().filter(player -> notifyPlayers.contains(player.getUniqueId().toString())).forEach(player -> player.sendMessage(message));
    }

    public static ProVPNBukkitLoader getInstance() {
        return instance;
    }
}
