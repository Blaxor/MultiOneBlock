package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.Rank.RankEnum;

import java.util.UUID;

public class PlayerJoinIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    @Getter
    PlayerOB invited;
    PlayerOB inviter;
    Island inviterIsland;


    public PlayerJoinIslandEvent(OneBlock plugin, String invited, PlayerOB inviter, Island inviterIsland, boolean callLeaveEvent, boolean far) {
        this.invited = plugin.getPlayerManager().getPlayer(UUID.fromString(invited));
        this.inviter = inviter;
        this.inviterIsland = inviterIsland;
        if (isCancelled())
            return;
        if (callLeaveEvent)
            plugin.getServer().getPluginManager().callEvent(new PlayerLeaveIslandEvent(plugin, this.invited, inviterIsland.getSpawnLocation()));
        inviterIsland.getMeta().getMembers().put(UUID.fromString(invited), RankEnum.MEMBER);
        inviterIsland.save(false);
        if (!far) {
            this.invited.setIsland(inviterIsland.getUuidIsland());
            this.invited.save();

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
