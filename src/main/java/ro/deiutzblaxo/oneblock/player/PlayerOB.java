package ro.deiutzblaxo.oneblock.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.island.events.IslandLoadEvent;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.nexs.db.manager.exception.DifferentArgLengthException;
import ro.nexs.db.manager.manager.DBManager;

import java.util.UUID;
import java.util.logging.Level;

@Getter

public class PlayerOB {


    private UUID player;
    @Setter
    private Island overworld;
    @Setter
    private Island nether;
    @Setter
    private Island the_end;
    private BukkitTask autosave;

    public PlayerOB(UUID player) {

        this.player = player;
        autosave = Bukkit.getScheduler().runTaskTimerAsynchronously(OneBlock.getInstance(), new Runnable() {
            @Override
            public void run() {
                OneBlock.getInstance().getLogger().log(Level.INFO,"Auto-Saving island " , player.toString() );
                save();
            }
        },20*60*5,20*60*5);

    }


    public void save() {
        OneBlock.getInstance().getLogger().log(Level.INFO, "Saving player " + player.toString());
        DBManager db = OneBlock.getInstance().getDbManager();
        if (db.existString(TableType.PLAYERS.table, "UUID", player.toString())) {

            db.setString(TableType.PLAYERS.table, IslandType.WORLD.name(), "UUID", overworld.getUuidIsland(), player.toString());
            if (nether != null)
                db.setString(TableType.PLAYERS.table, IslandType.NETHER.name(), "UUID", nether.getUuidIsland(), player.toString());
            if (the_end != null)
                db.setString(TableType.PLAYERS.table, IslandType.END.name(), "UUID", the_end.getUuidIsland(), player.toString());
        } else {
            try {
                db.insert(TableType.PLAYERS.table, new String[]{"UUID"}//TODO ADDING ALL THE WORLDS
                        , new String[]{player.toString()});
                save();
            } catch (DifferentArgLengthException e) {
                e.printStackTrace();
            }
        }
        OneBlock.getInstance().getLogger().log(Level.INFO, "Player " + player.toString() + " saved!");
    }

    public Island getOverworld(boolean forceLoad) {
        if (forceLoad) {
            Bukkit.getPluginManager().callEvent(new IslandLoadEvent(this, IslandType.WORLD));
        }
        return overworld;
    }

    public void setOverworld(Island overworld) {
        this.overworld = overworld;
    }

    public Island getNether(boolean forceLoad) {
        if (forceLoad) {
            Bukkit.getPluginManager().callEvent(new IslandLoadEvent(this, IslandType.NETHER));
        }
        return nether;
    }

    public void setNether(Island nether) {
        this.nether = nether;
    }

    public Island getThe_end(boolean forceLoad) {
        if (forceLoad) {
            Bukkit.getPluginManager().callEvent(new IslandLoadEvent(this, IslandType.END));
        }
        return the_end;
    }


    public void setThe_end(Island the_end) {
        this.the_end = the_end;
    }


}
