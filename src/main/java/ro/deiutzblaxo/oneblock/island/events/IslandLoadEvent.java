package ro.deiutzblaxo.oneblock.island.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

public class IslandLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private Island island;
    private PlayerOB playerOB;
    private IslandType type;

    public IslandLoadEvent(PlayerOB player, IslandType type) {

        this.playerOB = player;
        this.type = type;
        switch (type) {
            case WORLD:
                this.island = OneBlock.getInstance().getIslandManager().getIsland(player.getPlayer(), IslandType.WORLD.name() + "_" + player.getPlayer().toString(), IslandType.WORLD);
                player.setOverworld(island);
                island.loadWorld();
                this.island.setPhase(OneBlock.getInstance().getPhaseManager().getPhase(getIsland().getCount()));
                break;
            case NETHER:
                island = OneBlock.getInstance().getIslandManager().getIsland(player.getPlayer(), IslandType.NETHER.name() + "_" + player.getPlayer().toString(), IslandType.NETHER);
                island.loadWorld();
                player.setNether(island);
                break;
            case END:
                this.island = OneBlock.getInstance().getIslandManager().getIsland(player.getPlayer(), IslandType.NETHER.name() + "_" + player.getPlayer().toString(), IslandType.END);
                player.setThe_end(island);
                island.loadWorld();
                break;
        }

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
