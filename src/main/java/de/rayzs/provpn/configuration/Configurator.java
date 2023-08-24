package de.rayzs.provpn.configuration;

import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.configuration.impl.*;

public class Configurator {

    private static ConfigurationBuilder configurationBuilder;

    public static ConfigurationBuilder get(String fileName, String filePath) {
        if(Reflection.isBungeecordServer()) configurationBuilder = new BungeeConfigurationBuilder(fileName, filePath);
        else configurationBuilder = new BukkitConfigurationBuilder(fileName, filePath);
        return configurationBuilder;
    }

    public static ConfigurationBuilder get(String fileName) {
        if(Reflection.isBungeecordServer()) configurationBuilder = new BungeeConfigurationBuilder(fileName);
        else configurationBuilder = new BukkitConfigurationBuilder(fileName);
        return configurationBuilder;
    }
}
