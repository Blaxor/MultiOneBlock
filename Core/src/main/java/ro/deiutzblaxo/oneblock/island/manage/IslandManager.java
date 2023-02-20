package ro.deiutzblaxo.oneblock.island.manage;

import lombok.Getter;
import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.CreateCallBack;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.DeleteCallBack;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.LoadCallBack;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.unLoadCallBack;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class IslandManager {

    private OneBlock plugin;
    @Getter
    private final HashMap<String, Island> islands = new HashMap<>();

    public IslandManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    public Island createIsland(PlayerOB owner) {

        return plugin.generalPool.submit(new CreateCallBack(plugin, owner, island -> {
            islands.put(island.getUuidIsland(), island);
        })).get();
    }

    public Island loadIsland(String islandUUID) {
        try {
            return plugin.generalPool.submit(new LoadCallBack(plugin, islandUUID, island -> {
                islands.put(island.getUuidIsland(), island);
            })).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void unloadIsland(Island island) throws IslandHasPlayersOnlineException {
        if (!island.getBukkitWorld().getPlayers().isEmpty()) {
            throw new IslandHasPlayersOnlineException("There are players online and can't be unloaded!");
        }
        try {
            plugin.generalPool.submit(new unLoadCallBack(plugin, island, island1 -> {
                islands.remove(island1.getUuidIsland());

            })).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIsland(String islandUUID) throws IslandLoadedException {
        if (islands.containsKey(islandUUID)) {
            throw new IslandLoadedException("Please unload the island first!");
        }
        try {
            plugin.generalPool.submit(new DeleteCallBack(plugin, islandUUID, island -> {
            })).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }


    public static String generateWorldUUID() {
        return UUID.randomUUID().toString();
    }


    public Island getIsland(String uuid) {
        return islands.get(uuid);

    }

    public String getServer(String island) {
        try {
            return getIsland(island) == null ? plugin.getDbManager().getString(TableType.ISLANDS.table, "SERVER", "UUID", island) : getIsland(island).getServer();
        } catch (NoFoundException e) {
            return null;
        }
    }
}
