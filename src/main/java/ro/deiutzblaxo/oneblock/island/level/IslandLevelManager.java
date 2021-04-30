package ro.deiutzblaxo.oneblock.island.level;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.level.calculate.IslandLevelCalculateManager;
import ro.deiutzblaxo.oneblock.island.level.calculate.IslandLevelCalculator;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.Triplet;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class IslandLevelManager {
    @Getter
    private final ArrayList<Triplet<String, Integer, IslandMeta>> topIslands = new ArrayList<>();
    @Getter
    private final IslandLevelCalculateManager islandLevelCalculateManager;
    private final OneBlock plugin;

    public IslandLevelManager(OneBlock plugin) {
        this.plugin = plugin;
        islandLevelCalculateManager = new IslandLevelCalculateManager(plugin);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                updateTOP();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 15, 20 * 5 * 60);
        updateBlockValue();
    }

    private void updateTOP() throws Exception {
        ResultSet set;
        PreparedStatement statement = plugin.getDbManager().getPreparedStatement("SELECT UUID,LEVEL FROM LEVEL ORDER BY LEVEL DESC LIMIT 10");
        set = plugin.getDbManager().executeQuery(statement);
        topIslands.clear();
        while (set.next()) {
            String uuid = set.getString("UUID");
            int level = set.getInt("LEVEL");
            IslandMeta meta = IslandMeta.deserialize(plugin.getDbManager().getString(TableType.ISLANDS.table, "META", "UUID", uuid));
            topIslands.add(new Triplet(uuid, level, meta));
        }
        set.close();
    }

    private void updateBlockValue() {
        File file = new File(plugin.getDataFolder(), "blockconfig.yml");
        if (!file.exists()) {
            plugin.saveResource("blockconfig.yml", true);
            updateBlockValue();
            return;

        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("blocks");
        section.getKeys(true).forEach(s -> {
            IslandLevelCalculator.blockValue.put(Material.matchMaterial(s), section.getInt(s));
        });
    }


}
