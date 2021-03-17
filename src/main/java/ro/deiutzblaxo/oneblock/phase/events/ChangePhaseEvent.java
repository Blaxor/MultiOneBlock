package ro.deiutzblaxo.oneblock.phase.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.island.events.IslandDeleteEvent;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.logging.Level;

public class ChangePhaseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Island island;
    @Getter
    private Phase oldPhase;
    @Getter
    private Phase newPhase;
    public ChangePhaseEvent(OneBlock plugin, Island island) {
        this.island = island;
        this.oldPhase = island.getPhase();
        this.newPhase = plugin.getPhaseManager().getNexsPhase(island.getPhase());
        island.setPhase(plugin.getPhaseManager().getNexsPhase(island.getPhase()));
        ChunkUtils.changeBiome(island.getBukkitWorld(),newPhase.getPhaseBiome(),200);

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
