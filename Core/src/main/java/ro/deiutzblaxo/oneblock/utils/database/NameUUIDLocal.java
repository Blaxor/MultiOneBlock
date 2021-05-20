package ro.deiutzblaxo.oneblock.utils.database;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

@Data
public class NameUUIDLocal {
    private final OneBlock plugin;
    private TreeMap<String, String> cache = new TreeMap<>();
    private final File file;
    private FileConfiguration fileConfiguration;

    public NameUUIDLocal(OneBlock plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder().getPath(), "cachePlayer.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadSave();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            updateSave();

        }, 5, 20 * 60 * 15);
    }

    public void updateSave() {
        cache.forEach((s, s2) -> fileConfiguration.set(s, s2));
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSave() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.getKeys(false).forEach(s -> {
            cache.put(s, fileConfiguration.getString(s));
        });
    }

}
