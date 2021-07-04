package ro.deiutzblaxo.redissocial.configmanagers.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.deiutzblaxo.redissocial.RedisSocial;

import java.io.File;

public class ConfigManager {
    private File file;
    @Getter
    private FileConfiguration fileConfiguration;
    private final RedisSocial plugin;

    public ConfigManager(RedisSocial plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
        file = new File(plugin.getDataFolder(), "config.yml");
        System.out.println(file.getPath());
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }


}
