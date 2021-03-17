package ro.deiutzblaxo.oneblock.island.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.logging.Level;

public class IslandUnLoadEvent extends Event{

        private static final HandlerList HANDLERS = new HandlerList();
        @Getter
        private Island island;
        private PlayerOB playerOB;
        private IslandType type;

        public IslandUnLoadEvent(OneBlock plugin , Island island) {
            this.island = island;
            for(Player player : island.getBukkitWorld().getPlayers()){

                //TODO CHANGE WORLD SPAWN IF NEEDED
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
            island.getAutosave().cancel();
            Bukkit.getScheduler().runTaskLater(plugin,() -> {
                if(island.getMembers().isEmpty()){
                    Bukkit.getPluginManager().callEvent(new IslandDeleteEvent(plugin,null,island));

                    return;
                }
                //TODO TEST ^^^^^^^^^^^^^^^
                //TODO DELETE ISLAND ON ELSE
                WorldUtil.saveSlimeWorld(island.getWorld(), true);
                plugin.getDbManager().set(TableType.ISLANDS.table, "MEMBERS", "UUID", UTILS.fromHashMapToString(island.getMembers()), island.getUuidIsland());
                plugin.getDbManager().set(TableType.ISLANDS.table, "SERVER", "UUID", "nothing", island.getUuidIsland());
                Location loc = island.getSpawnLocation();
                plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNX", "UUID", loc.getX(), island.getUuidIsland());
                plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNY", "UUID", loc.getY(), island.getUuidIsland());
                plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNZ", "UUID", loc.getZ(), island.getUuidIsland());
                plugin.getDbManager().set(TableType.ISLANDS.table, "COUNT", "UUID", island.getCount(), island.getUuidIsland());
                plugin.getLogger().log(Level.INFO, "Island " + island.getUuidIsland() + " unloaded");
                plugin.getIslandManager().getIslands().remove(island.getUuidIsland());
            },2);



        }
        public Island getIsland(){
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
