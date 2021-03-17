package ro.deiutzblaxo.oneblock.island;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.island.events.IslandCreateEvent;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandExistsExceptions;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.util.HashMap;
import java.util.UUID;

public class IslandManager {
    OneBlock plugin;

    @Getter
    private final HashMap<String, Island> islands = new HashMap<>();

    public IslandManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    public Island createIsland(String uuid, IslandType types, UUID owner) throws IslandExistsExceptions {
        IslandCreateEvent event = new IslandCreateEvent(plugin,uuid,types,owner);
        Bukkit.getPluginManager().callEvent(event);
        return event.getIsland();
    }
    public Island getIsland(String uuid){
        return islands.get(uuid);
    }

    public Island getIsland(UUID player , String uuid){
        return getIsland(player,uuid,IslandType.valueOf(uuid.split("_")[0]));

    }
    public Island getIsland(UUID player, String uuid, IslandType type) {
        if (islands.containsKey(uuid))
            return islands.get(uuid);
        if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuid)) {
            try {
                Island island = new Island(UTILS.fromStringToHashMap(plugin.getDbManager().getString(TableType.ISLANDS.table, "MEMBERS", "UUID", uuid)), uuid, type,
                        plugin.getDbManager().existString(TableType.ISLANDS.table,"UUID",uuid) ? plugin.getDbManager().getInt(TableType.ISLANDS.table, "COUNT","UUID",uuid) : 0);
                plugin.getDbManager().set(TableType.ISLANDS.table, "SERVER", "UUID", plugin.getConfig().getString("server-name"), island.getUuidIsland());
                island.setWorld(WorldUtil.loadSlimeWorld(uuid,type));
                islands.put(uuid, island);
                return island;
            } catch (NoDataFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Island isl = createIsland(uuid, type, player);
                islands.put(uuid, isl);
                return isl;
            } catch (IslandExistsExceptions islandExistsExceptions) {
                islandExistsExceptions.printStackTrace();
            }
        }
        return null;

    }

    public void teleportToIsland(Player player, Island island){
        island.teleportHere(player);
    }

}
