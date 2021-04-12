package ro.deiutzblaxo.oneblock.island;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.sql.Blob;
import java.util.HashMap;

public class IslandManager {
    OneBlock plugin;

    @Getter
    private final HashMap<String, Island> islands = new HashMap<>();

    public IslandManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    public Island loadIsland(String uuid) {
        if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuid)) {
            Blob blob = plugin.getDbManager().getBlob(TableType.ISLANDS.table, "META", "UUID", uuid);
            IslandMeta meta = IslandMeta.deserialize(blob.getBinaryStream());
            Island island = new Island(plugin, uuid, meta);
            island.setWorld(WorldUtil.loadSlimeWorld(plugin, uuid, island));

            island.setBukkitWorld(Bukkit.getWorld(uuid));//TODO MAYBE A BETTER WAY?
            island.changeBorder();
            ChunkUtils.changeBiome(plugin, island);
            island.save(false);
            islands.put(uuid, island);
            return island;
        }
        Island island = new Island(plugin, uuid, new IslandMeta());
        island.setWorld(plugin.getSlimePlugin().createEmptyWorld(plugin.getLoader(), uuid, false, WorldUtil.getSlimePropertyMap(island)));
        island.loadWorld();
        island.save(false);
        islands.put(uuid, island);

        return island;
    }

    public void deleteIsland(String uuid) throws IslandLoadedException {
        if (islands.containsKey(uuid)) {
            throw new IslandLoadedException("Please unload the island first!");
        }
        plugin.getDbManager().deleteRow(TableType.ISLANDS.table, "UUID", uuid);
        plugin.getDbManager().deleteRow("worlds", "name", uuid);
    }

    public void unloadIsland(Island island, boolean save) throws IslandHasPlayersOnlineException {
        if (!island.getBukkitWorld().getPlayers().isEmpty()) {
            throw new IslandHasPlayersOnlineException("There are players online and can't be unloaded!");
        }
        island.setServer("nothing");
        if (save)
            island.save(false);

        islands.remove(island.getUuidIsland());
        plugin.getIslandLevelManager().cancelCalculation(island);
        island.getAutosave().cancel();
        WorldUtil.unloadSlimeWorld(plugin, island.getWorld());
    }

    public Island getIsland(String island) {
        return islands.get(island);
    }

    @SneakyThrows
    public String getServer(String island) {
        return getIsland(island) == null ? plugin.getDbManager().getString(TableType.ISLANDS.table, "SERVER", "UUID", island) : getIsland(island).getServer();
    }
}
