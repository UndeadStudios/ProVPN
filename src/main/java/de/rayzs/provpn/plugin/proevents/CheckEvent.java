package de.rayzs.provpn.plugin.proevents;

import de.rayzs.provpn.api.configuration.Settings;
import de.rayzs.provpn.plugin.loader.PluginLoader;
import de.rayzs.provpn.utils.list.PlayerList;
import de.rayzs.provpn.plugin.logger.Logger;
import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.api.event.*;


public class CheckEvent implements ProEvent {

    @Override
    public ProEventType type() { return ProEventType.CHECKING; }

    @Override
    public void execute(Object... objects) {
        String name = (String) objects[1], address = (String) objects[2];
        Logger.debug("Checking player=" + name + " (" + address + ") for using a virtual proxy...");

        if(Settings.WHITELIST_ENABLED && (PlayerList.contains(PlayerList.ListType.WHITELIST, name) || PlayerList.contains(PlayerList.ListType.WHITELIST, address))) {
            Logger.debug("Virtual proxy result of player=" + name + " (" + address + "): Whitelisted!");
            ProEventManager.triggerEvents(ProEventType.ALLOWED, objects);
            return;
        }

        if(PlayerList.contains(PlayerList.ListType.BLACKLIST, name) || PlayerList.contains(PlayerList.ListType.BLACKLIST, address)) {
            Logger.debug("Virtual proxy result of player=" + name + " (" + address + "): Blacklisted!");
            ProEventManager.triggerEvents(ProEventType.DETECTED, objects);
            return;
        }

        Logger.debug("Virtual proxy result of player=" + name + " (" + address + ") is loading...");

       if(Reflection.isBungeecordServer()) PluginLoader.getBungeeLoader().proxyRunnableCheck(name, address, objects);
       else PluginLoader.getBukkitLoader().proxyRunnableCheck(name, address, objects);
    }
}
