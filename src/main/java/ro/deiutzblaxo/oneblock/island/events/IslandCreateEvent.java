package ro.deiutzblaxo.oneblock.island.events;

import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import org.bukkit.block.Biome;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandExistsExceptions;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;
import ro.nexs.db.manager.exception.DifferentArgLengthException;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class IslandCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Island island;
    @Getter
    private String islandUUID;
    @Getter
    private IslandType type;
    @Getter
    private UUID owner;

    public IslandCreateEvent(OneBlock plugin,String uuid, IslandType types, UUID owner) throws IslandExistsExceptions {
        this.type = types;
        this.owner=owner;
        this.islandUUID = uuid;

        if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuid)) {
            throw new IslandExistsExceptions();
        }
        HashMap<UUID, RANK> a = new HashMap<>();
        a.put(owner, RANK.OWNER);
        try {
            plugin.getLogger().log(Level.INFO, "Inserting island in database ", uuid);

            plugin.getDbManager().insert(TableType.ISLANDS.table, new String[]{"UUID", "MEMBERS", "TYPE", "SPAWNX", "SPAWNY", "SPAWNZ", "SERVER","PHASE_LOCK"}
                    , new Object[]{uuid, UTILS.fromHashMapToString(a), types.name(), 0, 81, 0, plugin.getConfig().getString("server-name"),false});

        } catch (DifferentArgLengthException e) {
            e.printStackTrace();
        }
        SlimeWorld world = null;
        try {
            plugin.getLogger().log(Level.INFO, "creating empty world ", uuid);
            world = OneBlock.getInstance().getSlimePlugin().createEmptyWorld(OneBlock.getInstance().getLoader(), uuid, false, WorldUtil.getSlimePropertyMap(type));
        } catch (WorldAlreadyExistsException | IOException e) {
            e.printStackTrace();
        }
        plugin.getLogger().log(Level.INFO, "Creating class Island");
        Island island = new Island(a, uuid, types,0);
        island.setWorld(world);
        plugin.getIslandManager().getIslands().put(uuid, island);
        this.island = island;

    }

    public Island getIsland() {
        return island;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
