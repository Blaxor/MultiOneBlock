package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.events.IslandUnLoadEvent;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerUnLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private PlayerOB player;

    public PlayerUnLoadEvent(OneBlock plugin, UUID player_){
        player = plugin.getPlayerManager().getPlayer(player_);
        player.getAutosave().cancel();
        Island island = player.getOverworld();
        Bukkit.getPluginManager().callEvent(new IslandUnLoadEvent(plugin,island));
        plugin.getDbManager().set(TableType.PLAYERS.table, IslandType.WORLD.name(), "UUID", island.getUuidIsland(), player.getPlayer().toString());
        island = player.getNether();
        if(island!= null) {
            Bukkit.getPluginManager().callEvent(new IslandUnLoadEvent(plugin,island));
            plugin.getDbManager().set(TableType.PLAYERS.table, IslandType.NETHER.name(), "UUID", island.getUuidIsland(), player.getPlayer().toString());
        }

        island = player.getThe_end();
        if(island!= null) {
            Bukkit.getPluginManager().callEvent(new IslandUnLoadEvent(plugin,island));
            plugin.getDbManager().set(TableType.PLAYERS.table, IslandType.END.name(), "UUID", island.getUuidIsland(), player.getPlayer().toString());
        }
        player.save();
        plugin.getPlayerManager().getPlayers().remove(player);
        plugin.getLogger().log(Level.WARNING, "The player" + player.toString() + "(" + player + ") unloaded");


    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
