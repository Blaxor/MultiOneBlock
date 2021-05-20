package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;

import java.util.UUID;

public class PlayerBanIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    @Getter
    private final UUID playerOB;
    @Getter
    private final Island island;
    @Getter
    private final Player player;

    public PlayerBanIslandEvent(OneBlock plugin, UUID playerOB, Island island) {
        this.playerOB = playerOB;
        this.player = Bukkit.getPlayer(playerOB);
        this.island = island;
        if (isCancelled())
            return;
        island.getMeta().getBanned().add(playerOB);
        if (player == null) {
            return;
        }
        if (island.getMeta().getMembers().containsKey(playerOB)) {
            Bukkit.getPluginManager().callEvent(new PlayerKickIslandEvent(plugin, playerOB, island));

        }
        if (island.getBukkitWorld().getPlayers().contains(player)) {
            player.teleport(plugin.getSpawnLocation());
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
