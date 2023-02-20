package ro.deiutzblaxo.oneblock.island.manage.callbacks;

import com.infernalsuite.aswm.api.exceptions.WorldAlreadyExistsException;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.interfaces.onFinish;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.Rank;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import static ro.deiutzblaxo.oneblock.island.manage.IslandManager.generateWorldUUID;

public class CreateCallBack implements Callable<Island> {

    OneBlock plugin;
    PlayerOB owner;
    onFinish onFinish;

    public CreateCallBack(OneBlock plugin, PlayerOB owner, onFinish onFinish) {
        this.plugin = plugin;
        this.owner = owner;
        this.onFinish = onFinish;
    }


    @Override
    public Island call() throws IOException, WorldAlreadyExistsException {
        if (owner.hasIsland()) {
            return owner.getIsland(true);
        }

        Island island = new Island(plugin, generateWorldUUID(), new IslandMeta("Default Island"));//TODO CHANGE EASIER SETUP
        island.setLevel(0);

        island.getMeta().setMembers(new HashMap<>() {{
            put(owner.getPlayer(), Rank.RankEnum.OWNER);
        }});
        island.setWorld(plugin.getSlimePlugin().createEmptyWorld(plugin.getLoader(), island.getUuidIsland(), false, WorldUtil.getSlimePropertyMap(island)));
        island.loadWorld();
        island.save(false);
        if (Bukkit.getPlayer(island.getOwner()) != null)
            island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
        island.changeBorder();
        island.getBukkitWorld().setAutoSave(false);
        island.getBukkitWorld().setGameRule(GameRule.KEEP_INVENTORY, plugin.getConfig().getBoolean("keep-inventory"));

        onFinish.run(island);

        return island;
    }
}