package ro.deiutzblaxo.oneblock.island.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.TableType;

public class IslandDeleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Island island;
    @Getter
    PlayerOB player;

    public IslandDeleteEvent(OneBlock plugin, PlayerOB player, Island island) {
        this.island = island;
        this.player = player;

        Bukkit.getPluginManager().callEvent(new IslandUnLoadEvent(plugin, island));
        plugin.getDbManager().deleteRow(TableType.ISLANDS.table,"UUID",island.getUuidIsland());
        WorldUtil.deleteSlimeWorld(island.getWorld());
        switch (island.getType()) {
            case WORLD:
                player.setOverworld(null);
                break;
            case NETHER:
                player.setNether(null);
                break;
            case END:
                player.setThe_end(null);
                break;
        }


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
