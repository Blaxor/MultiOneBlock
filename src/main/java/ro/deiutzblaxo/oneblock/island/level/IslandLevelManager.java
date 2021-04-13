package ro.deiutzblaxo.oneblock.island.level;

import lombok.Getter;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.level.calculate.IslandLevelCalculateManager;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.Triplet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class IslandLevelManager {
    @Getter
    private ArrayList<Triplet<String, Integer, IslandMeta>> topIslands = new ArrayList<>();
    @Getter
    private IslandLevelCalculateManager islandLevelCalculateManager;
    private OneBlock plugin;

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
    }

    private void updateTOP() throws Exception {
        ResultSet set;
        PreparedStatement statement = plugin.getDbManager().getPreparedStatement("SELECT UUID,LEVEL FROM LEVEL ORDER BY LEVEL DESC LIMIT 10");
        set = plugin.getDbManager().executeQuery(statement);
        topIslands.clear();
        while (set.next()) {
            String uuid = set.getString("UUID");
            int level = set.getInt("LEVEL");
            IslandMeta meta = IslandMeta.deserialize(plugin.getDbManager().getBlob(TableType.ISLANDS.table, "META", "UUID", uuid).getBinaryStream());
            topIslands.add(new Triplet(uuid, level, meta));
        }
        set.close();
    }


}
