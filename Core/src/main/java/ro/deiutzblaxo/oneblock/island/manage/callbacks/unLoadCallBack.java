package ro.deiutzblaxo.oneblock.island.manage.callbacks;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.interfaces.onFinish;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.io.IOException;
import java.util.concurrent.Callable;

public class unLoadCallBack implements Callable<Boolean> {

    OneBlock plugin;
    Island island;
    onFinish onFinish;

    public unLoadCallBack(OneBlock plugin, Island island, onFinish onFinish) {
        this.plugin = plugin;
        this.island = island;
        this.onFinish = onFinish;
    }


    @Override
    public Boolean call() {

        try {

            island.setServer(null);
            island.save(false);


            plugin.getIslandLevelManager().getIslandLevelCalculateManager().cancelCalculation(island);
            island.getAutosave().cancel();
            WorldUtil.unloadSlimeWorld(plugin, island.getBukkitWorld());
            onFinish.run(island);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}