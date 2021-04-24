package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;

public class PlayerResetIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    @Getter
    private PlayerOB playerOB;
    @Getter
    private Island island;
    @Getter
    private Player player;

    public PlayerResetIslandEvent(OneBlock plugin, PlayerOB playerOB, Island island) {
        this.playerOB = playerOB;
        this.player = Bukkit.getPlayer(playerOB.getPlayer());
        this.island = island;
        if (isCancelled())
            return;

        //TODO REMOVE FROM DB v
        player.getInventory().clear();
        player.closeInventory();
        player.getEnderChest().clear();
        player.setLevel(0);
        player.setExp(0);

        island.getMeta().getMembers().forEach((uuid, rank) -> {
            plugin.getDbManager().setNull(TableType.PLAYERS.table, "UUID", String.valueOf(uuid), "ISLAND");
        });

        try {
            plugin.getIslandManager().unloadIsland(island, false);
        } catch (IslandHasPlayersOnlineException e) {
            e.printStackTrace();
        }
        try {
            plugin.getIslandManager().deleteIsland(island.getUuidIsland());
        } catch (IslandLoadedException e) {
            e.printStackTrace();
        }
    }


    public PlayerResetIslandEvent(OneBlock plugin, PlayerOB playerOB, Location teleport) {
        new PlayerResetIslandEvent(plugin, playerOB, playerOB.getIsland(false), teleport);
    }

    public PlayerResetIslandEvent(OneBlock plugin, PlayerOB playerOB) {
        new PlayerResetIslandEvent(plugin, playerOB, playerOB.getIsland(false));
    }

    public PlayerResetIslandEvent(OneBlock plugin, PlayerOB playerOB, Island island, Location teleport) {
        this.playerOB = playerOB;
        this.player = Bukkit.getPlayer(playerOB.getPlayer());
        this.island = island;
        if (isCancelled())
            return;

        //TODO REMOVE FROM DB v
        player.getInventory().clear();
        player.closeInventory();
        player.getEnderChest().clear();
        player.setLevel(0);
        player.setExp(0);
        island.getMeta().getMembers().remove(playerOB.getIsland());
        playerOB.setIsland(null);
        player.teleport(teleport);
        if (island.getMeta().getMembers().size() <= 1) {
            try {
                plugin.getIslandManager().unloadIsland(island, false);
            } catch (IslandHasPlayersOnlineException e) {
                e.printStackTrace();
            }
            try {
                plugin.getIslandManager().deleteIsland(island.getUuidIsland());
            } catch (IslandLoadedException e) {
                e.printStackTrace();
            }
        } else {
            if (island.getBukkitWorld().getPlayers().isEmpty()) {
                try {
                    plugin.getIslandManager().unloadIsland(island, true);
                } catch (IslandHasPlayersOnlineException e) {
                    e.printStackTrace();
                }
            } else {
                island.save(false);
            }
        }
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
