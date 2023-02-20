package ro.deiutzblaxo.oneblock.island.manage.callbacks;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.interfaces.onFinish;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.io.IOException;
import java.util.concurrent.Callable;

public class LoadCallBack implements Callable<Island> {

    OneBlock plugin;
    String islandUUID;
    onFinish onFinish;

    public LoadCallBack(OneBlock plugin, String islandUUID, onFinish onFinish) {
        this.plugin = plugin;
        this.islandUUID = islandUUID;
        this.onFinish = onFinish;
    }


    @Override
    public Island call() throws IOException, NoFoundException {

        String data = plugin.getDbManager().getString(TableType.ISLANDS.table, "META", "UUID", islandUUID);
        IslandMeta meta = IslandMeta.deserialize(data);
        Island island = new Island(plugin, islandUUID, meta);
        if (plugin.getDbManager().exists(TableType.LEVEL.table, "UUID", islandUUID)) {
            island.setLevel(plugin.getDbManager().get(TableType.LEVEL.table, "LEVEL", "UUID", islandUUID, Integer.class));
        } else island.setLevel(0);

        island.getBukkitWorld().setAutoSave(false);
        island.getBukkitWorld().setGameRule(GameRule.KEEP_INVENTORY, plugin.getConfig().getBoolean("keep-inventory"));
        island.setWorld(WorldUtil.loadSlimeWorld(plugin, islandUUID, island));
        island.setBukkitWorld(Bukkit.getWorld(islandUUID));//TODO MAYBE A BETTER WAY?
        if (Bukkit.getPlayer(island.getOwner()) != null)
            island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
        island.changeBorder();
        ChunkUtils.changeBiome(plugin, island);
        island.getBukkitWorld().setTime(island.getMeta().getTime());
        island.save(false);
        return island;
    }
}