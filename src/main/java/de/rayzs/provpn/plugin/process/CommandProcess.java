package de.rayzs.provpn.plugin.process;

import de.rayzs.provpn.api.discord.Discord;
import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.plugin.loader.PluginLoader;
import de.rayzs.provpn.utils.address.AddressUtils;
import de.rayzs.provpn.api.configuration.*;
import de.rayzs.provpn.utils.list.PlayerList;
import java.util.*;

public class CommandProcess {

    public static void handleCommand(Object executorObject, String[] args, String commandName) {
        ProcessExecutor executor = new ProcessExecutor(executorObject);
        if(!checkPermission(executor, "use", true)) return;

        String blacklistUsage = Messages.HELP_USAGE_BLACKLIST.replace("%COMMAND%", commandName),
                whitelistUsage =  Messages.HELP_USAGE_WHITELIST.replace("%COMMAND%", commandName),
                checkUsage = Messages.HELP_USAGE_CHECK.replace("%COMMAND%", commandName),
                onlyPlayers = Messages.ONLY_PLAYERS;

        if(args.length == 3) {
            if(args[0].equals("whitelist") || args[0].equals("wl")) {
                if(!checkPermission(executor, "whitelist.change", true)) return;
                String target = args[2];
                switch (args[1].toLowerCase()) {
                    case "add":
                        executor.sendMessage(PlayerList.add(PlayerList.ListType.WHITELIST, target) ?
                                Messages.WHITELIST_ADDED.replace("%TARGET%", target)
                                : Messages.WHITELIST_ADD_NO.replace("%TARGET%", target));
                        break;
                    case "remove":
                        executor.sendMessage(PlayerList.remove(PlayerList.ListType.WHITELIST, target) ?
                                Messages.WHITELIST_REMOVED.replace("%TARGET%", target)
                                : Messages.WHITELIST_REMOVE_NO.replace("%TARGET%", target));
                        break;
                    default:
                        executor.sendMessage(whitelistUsage);
                        break;
                } return;

            } else if(args[0].equals("blacklist") || args[0].equals("bl")) {
                if(!checkPermission(executor, "blacklist.change", true)) return;
                String target = args[2];
                switch (args[1].toLowerCase()) {
                    case "add":
                        executor.sendMessage(PlayerList.add(PlayerList.ListType.BLACKLIST, target) ?
                                Messages.BLACKLIST_ADDED.replace("%TARGET%", target)
                                : Messages.BLACKLIST_ADD_NO.replace("%TARGET%", target));
                        break;
                    case "remove":
                        executor.sendMessage(PlayerList.remove(PlayerList.ListType.BLACKLIST, target) ?
                                Messages.BLACKLIST_REMOVED.replace("%TARGET%", target)
                                : Messages.BLACKLIST_REMOVE_NO.replace("%TARGET%", target));
                        break;
                    default:
                        executor.sendMessage(blacklistUsage);
                        break;
                } return;
            }
        } else if(args.length == 2) {

            switch (args[0].toLowerCase()) {
                case "whitelist": case "wl":
                    if(args[1].equals("list") || args[1].equals("ls")) {
                        if (!checkPermission(executor, "whitelist.list", true)) return;
                        List<String> whitelistedPlayers = PlayerList.getListByType(PlayerList.ListType.WHITELIST);
                        executor.sendMessage(whitelistedPlayers.isEmpty() ? Messages.WHITELIST_LIST_EMPTY : Messages.WHITELIST_LIST.replace("%LIST%",  Arrays.toString(whitelistedPlayers.toArray()).replace("[", "").replace("]", "").replace(", ", ", ")));
                        return;

                    } else if(args[1].equals("clear") || args[1].equals("clr")) {
                        if (!checkPermission(executor, "whitelist.clear", true)) return;
                        if(PlayerList.getListByType(PlayerList.ListType.WHITELIST).isEmpty()) {
                            executor.sendMessage(Messages.WHITELIST_CLEAR_FAILED);
                            return;
                        }
                        PlayerList.getListByType(PlayerList.ListType.WHITELIST).clear();
                        executor.sendMessage(Messages.WHITELIST_CLEAR);
                        return;
                    }
                    executor.sendMessage(whitelistUsage);
                    return;
                case "blacklist": case "bl":
                    if(args[1].equals("list") || args[1].equals("ls")) {
                        if (!checkPermission(executor, "blacklist.list", true)) return;
                        List<String> blacklistedPlayers = PlayerList.getListByType(PlayerList.ListType.BLACKLIST);
                        executor.sendMessage(blacklistedPlayers.isEmpty() ? Messages.BLACKLIST_LIST_EMPTY : Messages.BLACKLIST_LIST.replace("%LIST%",  Arrays.toString(blacklistedPlayers.toArray()).replace("[", "").replace("]", "").replace(", ", ", ")));
                        return;

                    } else if(args[1].equals("clear") || args[1].equals("clr")) {
                        if (!checkPermission(executor, "blacklist.clear", true)) return;
                        if(PlayerList.getListByType(PlayerList.ListType.BLACKLIST).isEmpty()) {
                            executor.sendMessage(Messages.BLACKLIST_CLEAR_FAILED);
                            return;
                        }
                        PlayerList.getListByType(PlayerList.ListType.BLACKLIST).clear();
                        executor.sendMessage(Messages.BLACKLIST_CLEAR);
                        return;
                    }
                    executor.sendMessage(blacklistUsage);
                    return;
                case "check":
                    if(!checkPermission(executor, "check", true)) return;
                    String target = args[1];
                    if (!AddressUtils.isValidAddress(target)) {
                        executor.sendMessage(Messages.CHECK_NOT_VALID.replace("%IP%", target));
                        return;
                    }

                    boolean whitelisted = PlayerList.getListByType(PlayerList.ListType.WHITELIST).contains(target),
                            blacklisted = PlayerList.getListByType(PlayerList.ListType.BLACKLIST).contains(target);
                    executor.sendMessage(Messages.CHECK_LOADING.replace("%IP%", target));
                    new Thread(() -> {
                        boolean virtualProxy = AddressUtils.isVirtualProxy(target);
                        Messages.CHECK_MESSAGE.forEach(message -> {
                            executor.sendMessage(message
                                    .replace("&", "§")
                                    .replace("%IP%", target)
                                    .replace("%VPN%", virtualProxy ? Messages.CHECK_STATE_POSITIVE : Messages.CHECK_STATE_NEGATIVE)
                                    .replace("%WHITELISTED%", whitelisted ? Messages.CHECK_STATE_POSITIVE : Messages.CHECK_STATE_NEGATIVE)
                                    .replace("%BLACKLISTED%", blacklisted ? Messages.CHECK_STATE_POSITIVE : Messages.CHECK_STATE_NEGATIVE)
                            );
                        });
                    }).start();
                    return;
            }
        } else if(args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "whitelist": case "wl":
                    executor.sendMessage(whitelistUsage);
                    return;
                case "blacklist": case "bl":
                    executor.sendMessage(blacklistUsage);
                    return;
                case "check":
                    executor.sendMessage(checkUsage);
                    return;
                case "reload": case "rl":
                    if(!checkPermission(executor, "reload", true)) return;
                    Messages.reload();
                    Settings.reload();
                    PlayerList.reload();
                    Discord.initialize();
                    executor.sendMessage(Messages.RELOAD);
                    return;
                case "actionbar": case "stats":
                    if(executor.isConsole()) {
                        executor.sendMessage(onlyPlayers);
                        return;
                    }
                    if(!checkPermission(executor, "stats", true)) return;
                    boolean added = false;
                    if(PlayerList.add(PlayerList.ListType.VERBOSE, executor.getUniqueId().toString())) added = true;
                    else PlayerList.remove(PlayerList.ListType.VERBOSE, executor.getUniqueId().toString());
                    executor.sendMessage(added ? Messages.STATS_ACTIVATED : Messages.STATS_DISABLED);
                    return;
                case "notify":
                    if(executor.isConsole()) {
                        executor.sendMessage(onlyPlayers);
                        return;
                    }
                    if(!checkPermission(executor, "notify", true)) return;
                    boolean removed = false;
                    if(PlayerList.add(PlayerList.ListType.NOTIFY, executor.getUniqueId().toString())) removed = true;
                    else PlayerList.remove(PlayerList.ListType.NOTIFY, executor.getUniqueId().toString());
                    executor.sendMessage(removed ? Messages.NOTIFICATIONS_ACTIVATED : Messages.NOTIFICATIONS_DISABLED);
                    return;
            }
        }

        Messages.HELP_ALL_MESSAGE.forEach(message -> executor.sendMessage(message.replace("&", "§").replace("%COMMAND%", commandName)));
    }

    public static List<String> handleTabComplete(Object executorObject, String[] args) {
        ProcessExecutor executor = new ProcessExecutor(executorObject);
        final List<String> suggestions = new ArrayList<>(), result = new ArrayList<>();

        switch (args.length) {
            case 1:
                if (checkPermission(executor, "notify", false)) suggestions.add("notify");
                if (checkPermission(executor, "stats", false)) suggestions.addAll(Arrays.asList("actionbar", "stats"));
                if (checkPermission(executor, "check", false)) suggestions.add("check");
                if (checkPermission(executor, "reload", false)) {
                    suggestions.add("rl");
                    suggestions.add("reload");
                }

                if(checkPermission(executor, "whitelist.list", false)
                        || checkPermission(executor, "whitelist.clear", false)
                        || checkPermission(executor, "whitelist.change", false)) suggestions.addAll(Arrays.asList("whitelist", "wl"));

                if(checkPermission(executor, "blacklist.list", false)
                        || checkPermission(executor, "blacklist.clear", false)
                        || checkPermission(executor, "blacklist.change", false)) suggestions.addAll(Arrays.asList("blacklist", "bl"));
                break;
            case 2:
                if (checkPermission(executor, "whitelist.change", false))
                switch (args[0].toLowerCase()) {
                    case "whitelist": case "blacklist": case "wl": case "bl":
                        suggestions.addAll(Arrays.asList("add", "remove", "list", "ls", "clear", "clr"));
                        break;
                }
                break;
            case 3:
                if (checkPermission(executor, (args[0].equals("whitelist") || args[0].equals("wl") ? "whitelist" : "blacklist") + ".change", false))
                switch (args[1].toLowerCase()) {
                    case "remove":
                        suggestions.addAll(PlayerList.getListByType(args[0].equals("whitelist") || args[0].equals("wl") ? PlayerList.ListType.WHITELIST : PlayerList.ListType.BLACKLIST));
                        break;
                    case "add":
                        if(Reflection.isBungeecordServer()) PluginLoader.getBungeeLoader().getPlayerNames().stream().filter(name -> !PlayerList.getListByType(args[0].equals("whitelist") || args[0].equals("wl") ? PlayerList.ListType.WHITELIST : PlayerList.ListType.BLACKLIST).contains(name)).forEach(suggestions::add);
                        else PluginLoader.getBukkitLoader().getPlayerNames().stream().filter(name -> !PlayerList.getListByType(args[0].equals("whitelist") || args[0].equals("wl") ? PlayerList.ListType.WHITELIST : PlayerList.ListType.BLACKLIST).contains(name)).forEach(suggestions::add);
                        break;
                }
                break;
        }

        suggestions.stream().filter(suggestion -> suggestion.startsWith(args[args.length-1].toLowerCase())).forEach(result::add);
        return result;
    }

    private static boolean checkPermission(ProcessExecutor executor, String permission, boolean sendMessage) {
        if(!executor.hasPermission("provpn.") && !executor.hasPermission("provpn.*")) {
            if(sendMessage) executor.sendMessage(Messages.NO_PERMS.replace("%MISSING%", "provpn." + permission));
            return false;
        } return true;
    }
}
