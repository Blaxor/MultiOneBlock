package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.eventlisteners.ChatListener;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerUnLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private PlayerOB player;

    public PlayerUnLoadEvent(OneBlock plugin, UUID player_){
        player = plugin.getPlayerManager().getPlayer(player_);
        player.getAutosave().cancel();
        player.setServer("none");
        player.save();
        plugin.getPlayerManager().getPlayers().remove(player_);
        ChatListener.globalRecipients.remove(player);
        plugin.getLogger().log(Level.INFO, "The player" + player.toString() + " (" + player + ") unloaded");
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
