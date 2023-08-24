package de.rayzs.provpn.utils.list;

import de.rayzs.provpn.configuration.*;
import java.util.*;

public class PlayerList {

    private static final ConfigurationBuilder configuration = Configurator.get("lists");
    private static List<String> blacklisted, whitelisted, notify_players, verbose_players;

    static { reload(); }
    public static void initialize() { }

    public static void reload() {
        whitelisted = (List<String>) configuration.getOrSet("lists.whitelisted", new ArrayList<>());
        blacklisted = (List<String>) configuration.getOrSet("lists.blacklisted", new ArrayList<>());
        notify_players = new ArrayList<>();
        verbose_players = new ArrayList<>();
    }

    public static void save() {
        configuration.setAndSave("lists.whitelisted", whitelisted);
        configuration.setAndSave("lists.blacklisted", blacklisted);
    }

    public static boolean add(ListType type, Object object) {
        List<String> list = getListByType(type);
        return add(list, object);
    }

    private static boolean add(List<String> list, Object object) {
        if(list == null || contains(list, object)) return false;
        list.add(String.valueOf(object).toLowerCase());
        return true;
    }

    public static boolean remove(ListType type, Object object) {
        List<String> list = getListByType(type);
        return remove(list, object);
    }

    private static boolean remove(List<String> list, Object object) {
        if(list == null || !contains(list, object)) return false;
        list.remove(String.valueOf(object).toLowerCase());
        return true;
    }

    public static boolean contains(ListType type, Object object) {
        List<String> list = getListByType(type);
        if(list == null) return false;
        return contains(list, object);
    }

    public static boolean contains(List<?> list, Object object) {
        return list.contains(String.valueOf(object).toLowerCase());
    }

    public static List<String> getListByType(ListType type) {
        return type == ListType.BLACKLIST ? blacklisted
                : type == ListType.WHITELIST ? whitelisted
                : type == ListType.NOTIFY ? notify_players
                : type == ListType.VERBOSE ? verbose_players
                : new ArrayList<>();
    }

    public enum ListType { BLACKLIST, WHITELIST, NOTIFY, VERBOSE }
}
