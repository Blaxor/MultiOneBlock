package ro.deiutzblaxo.oneblock.island;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.Pair;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;


public class IslandManager {
    private OneBlock plugin;

    @Getter
    private final HashMap<String, Island> islands = new HashMap<>();

    public IslandManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    public Island loadIsland(String uuid) {

        Pair<Island, Boolean> pair = plugin.getGeneralPool().submit(new loadIslandCallback(uuid, plugin)).get();
        Island island = pair.getFirst();
        if (!pair.getLast()) {

            island.setWorld(WorldUtil.loadSlimeWorld(plugin, uuid, island));
            island.setBukkitWorld(Bukkit.getWorld(uuid));//TODO MAYBE A BETTER WAY?
            if (Bukkit.getPlayer(island.getOwner()) != null)
                island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
            island.changeBorder();
            ChunkUtils.changeBiome(plugin, island);
            island.getBukkitWorld().setTime(island.getMeta().getTime());
            island.save(false);
            islands.put(uuid, island);

        } else {
            island.setWorld(plugin.getSlimePlugin().createEmptyWorld(plugin.getLoader(), uuid, false, WorldUtil.getSlimePropertyMap(island)));
            island.loadWorld();
            island.save(false);
            islands.put(uuid, island);
            if (Bukkit.getPlayer(island.getOwner()) != null)
                island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
            island.changeBorder();

        }
        return island;
    }

    public void deleteIsland(String uuid) throws IslandLoadedException {

        if (islands.containsKey(uuid)) {
            throw new IslandLoadedException("Please unload the island first!");
        }
        plugin.getDbManager().deleteRow(TableType.ISLANDS.table, "UUID", uuid);
        plugin.getDbManager().deleteRow("worlds", "name", uuid);
        plugin.getDbManager().deleteRow(TableType.LEVEL.table, "UUID", uuid);
    }

    public void unloadIsland(Island island, boolean save) throws IslandHasPlayersOnlineException {

        if (!island.getBukkitWorld().getPlayers().isEmpty()) {
            throw new IslandHasPlayersOnlineException("There are players online and can't be unloaded!");
        }
        island.setServer(null);
        if (save)
            island.save(false);

        islands.remove(island.getUuidIsland());
        plugin.getIslandLevelManager().getIslandLevelCalculateManager().cancelCalculation(island);
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


    private class loadIslandCallback implements Callable<Pair<Island, Boolean>> {
        @Getter
        private String uuid;

        private OneBlock plugin;

        loadIslandCallback(String uuid, OneBlock plugin) {
            this.uuid = uuid;
            this.plugin = plugin;
        }

        @SneakyThrows
        @Override
        public Pair<Island, Boolean> call() {

            if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuid)) {
                String data = plugin.getDbManager().getString(TableType.ISLANDS.table, "META", "UUID", uuid);
                IslandMeta meta = IslandMeta.deserialize(data);
                Island island = new Island(plugin, uuid, meta);
                if (plugin.getDbManager().existString(TableType.LEVEL.table, "UUID", uuid)) {
                    island.setLevel(plugin.getDbManager().getInt(TableType.LEVEL.table, "LEVEL", "UUID", uuid));
                } else
                    island.setLevel(0);

                return new Pair<>(island, false);
            }

            Island island = new Island(plugin, uuid, new IslandMeta(plugin.getPlayerManager().getNameByUUID(UUID.fromString(uuid.split("_")[1]))));
            if (plugin.getDbManager().existString(TableType.LEVEL.table, "UUID", uuid)) {
                island.setLevel(plugin.getDbManager().getInt(TableType.LEVEL.table, "LEVEL", "UUID", uuid));
            } else
                island.setLevel(0);

            return new Pair<>(island, true);
        }
    }
}


