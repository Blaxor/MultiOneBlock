package ro.deiutzblaxo.oneblock.menu.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.level.calculate.Results;

public class PlayerOpenMenuEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Results results;

    public PlayerOpenMenuEvent(Player player, String id) {

    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
