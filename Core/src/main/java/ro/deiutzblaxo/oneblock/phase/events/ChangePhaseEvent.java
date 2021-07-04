package ro.deiutzblaxo.oneblock.phase.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;

public class ChangePhaseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private final Island island;
    @Getter
    private final Phase oldPhase;
    @Getter
    private final Phase newPhase;

    public ChangePhaseEvent(OneBlock plugin, Island island) {
        this.island = island;
        this.oldPhase = island.getPhase();
        this.newPhase = plugin.getPhaseManager().getNextPhase(island.getPhase());
        island.setPhase(newPhase);
        island.getMeta().setCount(island.getPhase().getBlockNumber());
        ChunkUtils.changeBiome(plugin, island);
        island.getPhaseObjectsQueue().clear();

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
