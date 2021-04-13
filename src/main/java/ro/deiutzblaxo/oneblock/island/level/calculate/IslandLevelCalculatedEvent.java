package ro.deiutzblaxo.oneblock.island.level.calculate;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;

public class IslandLevelCalculatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Results results;

    public IslandLevelCalculatedEvent(OneBlock plugin, Results results) {
        this.results = results;
        Island island = results.getIsland().get();
        island.setLevel(results.getLevel());
        island.saveLevel();
        island.getMeta().getMembers().forEach((uuid, rank) -> {
            if (Bukkit.getPlayer(uuid) != null) {
                Bukkit.getPlayer(uuid).sendMessage("The level of island is: " + island.getLevel() + "(" + results.getRemainPoints() + ")");
            }
        });
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
