package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.util.UUID;

public class PlayerLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private PlayerOB player;

    public PlayerLoadEvent(OneBlock plugin, UUID player_) {
        player = plugin.getPlayerManager().getPlayer(player_);
        if (!plugin.getDbManager().exists(TableType.PLAYERS.table, "UUID", player_.toString())){

            plugin.getPlayerManager().getPlayers().put(player_, player);
            return;
        }


            String islandUUID = IslandType.WORLD.name() + "_" + player_.toString();
            try {
                islandUUID = plugin.getDbManager().getString(TableType.PLAYERS.table, IslandType.WORLD.name(), "UUID", player.toString());
            } catch (NoDataFoundException e) {
            } finally {
                player.setOverworld(plugin.getIslandManager().getIsland(player_, islandUUID, IslandType.WORLD));
            }
            plugin.getPlayerManager().getPlayers().put(player_, player);



    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
