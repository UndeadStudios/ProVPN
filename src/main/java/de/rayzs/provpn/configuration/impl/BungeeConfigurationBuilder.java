package de.rayzs.provpn.configuration.impl;

import de.rayzs.provpn.configuration.ConfigurationBuilder;
import de.rayzs.provpn.plugin.logger.Logger;
import net.md_5.bungee.config.*;
import java.io.File;

public class BungeeConfigurationBuilder implements ConfigurationBuilder {

    private String fileName, customFilePath;
    private File file;
    private boolean loadDefault;
    private Configuration configuration;

    public BungeeConfigurationBuilder(String fileName) {
        init(fileName);
    }

    public BungeeConfigurationBuilder(String fileName, String customFilePath) {
        this.customFilePath = customFilePath;
        init(fileName);
    }

    protected void init(String fileName) {
        customFilePath = customFilePath == null ? "./plugins/ProVPN" : customFilePath;
        File directory = new File(customFilePath);

        this.fileName = fileName;

        try {
            if(!directory.isDirectory()) directory.mkdirs();
            file = new File(customFilePath, fileName + ".yml");
            loadDefault = !file.exists();
            if(!file.exists()) file.createNewFile();
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (Exception exception) { exception.printStackTrace(); }
    }

    @Override
    public void reload() {
        init(this.fileName);
    }

    @Override
    public void save() {
        try { ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (Exception exception) {
            Logger.warning("Could not save configuration file! [file=" + fileName + ", message=" + exception.getMessage() + "]");
        }
    }

    @Override
    public ConfigurationBuilder set(String path, String target, Object object) {
        this.configuration.set(((path != null) ? (path + ".") : "") + target, object);
        return this;
    }

    @Override
    public ConfigurationBuilder set(String target, Object object) {
        set(null, target, object);
        return this;
    }

    @Override
    public ConfigurationBuilder setAndSave(String path, String target, Object object) {
        set(path, target, object);
        save();
        return this;
    }

    @Override
    public ConfigurationBuilder setAndSave(String target, Object object) {
        set(target, object);
        save();
        return this;
    }

    @Override
    public Object getOrSet(String path, String target, Object object) {
        Object result = get(path, target);
        if (result != null)
            return result;
        set(path, target, object);
        save();
        return get(path, target);
    }

    @Override
    public Object getOrSet(String target, Object object) {
        Object result = get(target);
        if (result != null)
            return result;
        set(target, object);
        save();
        return get(target);
    }

    @Override
    public Object get(String target) {
        return get(null, target);
    }

    @Override
    public Object get(String path, String target) {
        Object object = this.configuration.get(((path != null) ? (path + ".") : "") + target);
        if (object instanceof String) {
            String objString = (String) object;
            return objString.replace("&", "§");
        }
        return object;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public boolean loadDefault() {
        return loadDefault;
    }
}
