package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import me.stefan923.playerdatastorage.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.UUID;

public class PlayerLeaveIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    @Getter
    private PlayerOB playerOB;
    @Getter
    private UUID playerOBUUID;
    @Getter
    private Island island;
    @Getter
    private Player player;

    public PlayerLeaveIslandEvent(OneBlock plugin, UUID playerOB, Island island) {
        playerOBUUID = playerOB;
        this.playerOB = plugin.getPlayerManager().getPlayer(playerOB);
        this.player = Bukkit.getPlayer(playerOB);
        this.island = island;
        if (isCancelled())
            return;
        if (player != null) {
            player.getInventory().clear();
            player.closeInventory();
            player.getEnderChest().clear();
            player.setLevel(0);
            player.setExp(0);
        }
        island.getMeta().getMembers().remove(playerOB);
        if (this.playerOB == null) {
            plugin.getDbManager().setNull(TableType.PLAYERS.table, "UUID", playerOBUUID.toString(), "ISLAND");

            PlayerData data = new PlayerData(/*new ItemStack[0],*/ new ItemStack[0], new PotionEffect[0], 0);
            data.setUuid(playerOBUUID);
            plugin.getPlayerSaveStorage().savePlayerData(data);
        } else {
            this.playerOB.setIsland(null);
            this.player.teleport(plugin.getSpawnLocation());
        }
        if (island.getMeta().getMembers().size() <= 0) {
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


    public PlayerLeaveIslandEvent(OneBlock plugin, PlayerOB playerOB, Island island) {
        this.playerOB = playerOB;
        this.playerOBUUID = playerOB.getPlayer();
        this.player = Bukkit.getPlayer(playerOBUUID);
        this.island = island;
        if (isCancelled())
            return;

        player.getInventory().clear();
        player.closeInventory();
        player.getEnderChest().clear();
        player.setLevel(0);
        player.setExp(0);
        island.getMeta().getMembers().remove(playerOBUUID);
        playerOB.setIsland(null);
        this.player.teleport(plugin.getSpawnLocation());
        if (island.getMeta().getMembers().size() <= 0) {
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

    public PlayerLeaveIslandEvent(OneBlock plugin, PlayerOB playerOB, Location teleport) {
        new PlayerLeaveIslandEvent(plugin, playerOB, playerOB.getIsland(false), teleport);
    }

    public PlayerLeaveIslandEvent(OneBlock plugin, PlayerOB playerOB) {
        new PlayerLeaveIslandEvent(plugin, playerOB, playerOB.getIsland(false));
    }

    public PlayerLeaveIslandEvent(OneBlock plugin, PlayerOB playerOB, Island island, Location teleport) {
        this.playerOB = playerOB;
        this.playerOBUUID = playerOB.getPlayer();
        this.player = Bukkit.getPlayer(playerOBUUID);
        this.island = island;
        if (isCancelled())
            return;

        player.getInventory().clear();
        player.closeInventory();
        player.getEnderChest().clear();
        player.setLevel(0);
        player.setExp(0);
        island.getMeta().getMembers().remove(playerOB.getPlayer());
        playerOB.setIsland(null);
        player.teleport(teleport);
        this.player.teleport(plugin.getSpawnLocation());
        if (island.getMeta().getMembers().size() <= 0) {
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
