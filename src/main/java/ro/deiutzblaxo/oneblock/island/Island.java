package ro.deiutzblaxo.oneblock.island;

import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.events.IslandDeleteEvent;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Setter
public class Island {



    private HashMap<UUID, RANK> members;
    private String uuidIsland;
    private IslandType type;
    private SlimeWorld world;
    private String server;//TODO
    private Location spawnLocation;
    private World bukkitWorld;
    private int count;
    private Phase phase;
    private BukkitTask autosave;

    public void setPhase(Phase phase) {
        OneBlock.getInstance().getLogger().log(Level.INFO,phase.getPhaseName()+","+phase.getBlockNumber());
        this.phase = phase;
    }

    public Island(HashMap<UUID, RANK> members, String uuidIsland, IslandType type, int count) {
        this.members = members;
        this.uuidIsland = uuidIsland;
        this.type = type;
        this.count = count;
        autosave =Bukkit.getScheduler().runTaskTimerAsynchronously(OneBlock.getInstance(), new Runnable() {
            @Override
            public void run() {
                OneBlock.getInstance().getLogger().log(Level.INFO,"Auto-Saving island " , uuidIsland );
                save();
            }
        },20*60*5,20*60*5);
        setPhase(OneBlock.getInstance().getPhaseManager().getPhase(count));
    }

    public void loadWorld() {
        OneBlock.getInstance().getLogger().log(Level.INFO, "loading island " + world.getName());
        if (Bukkit.getWorld(getWorld().getName()) == null)
            OneBlock.getInstance().getSlimePlugin().generateWorld(world);
        bukkitWorld = Bukkit.getWorld(getWorld().getName());

        if (getMiddleBlock().getType() == Material.AIR) {
            getMiddleBlock().setType(Material.GRASS_BLOCK);
        }
        try {
            setSpawnLocation(OneBlock.getInstance().getDbManager().get(TableType.ISLANDS.table, "SPAWNX", "UUID", uuidIsland, Double.class),
                    OneBlock.getInstance().getDbManager().get(TableType.ISLANDS.table, "SPAWNY", "UUID", uuidIsland, Double.class),
                    OneBlock.getInstance().getDbManager().get(TableType.ISLANDS.table, "SPAWNZ", "UUID", uuidIsland, Double.class));
        } catch (NoDataFoundException e) {
            e.printStackTrace();
        }
        switch (type){
            case WORLD:
                ChunkUtils.changeBiome(bukkitWorld,phase.getPhaseBiome(),200);
                break;
            case NETHER:
                ChunkUtils.changeBiome(bukkitWorld, Biome.NETHER_WASTES,100);
                break;
            case END:
                ChunkUtils.changeBiome(bukkitWorld,Biome.THE_END,50);
                break;
        }

    }
    public void save(){
        OneBlock plugin = OneBlock.getInstance();
        Bukkit.getScheduler().runTaskLater(plugin,() -> {
            if(this.getMembers().isEmpty()){
                Bukkit.getPluginManager().callEvent(new IslandDeleteEvent(plugin,null,this));
                return;
            }
            //TODO TEST ^^^^^^^^^^^^^^^
            //TODO DELETE ISLAND ON ELSE
            WorldUtil.saveSlimeWorld(this.getWorld(), false);
            plugin.getDbManager().set(TableType.ISLANDS.table, "MEMBERS", "UUID", UTILS.fromHashMapToString(this.getMembers()), this.getUuidIsland());
            plugin.getDbManager().set(TableType.ISLANDS.table, "SERVER", "UUID", "nothing", this.getUuidIsland());
            Location loc = this.getSpawnLocation();
            plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNX", "UUID", loc.getX(), this.getUuidIsland());
            plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNY", "UUID", loc.getY(), this.getUuidIsland());
            plugin.getDbManager().set(TableType.ISLANDS.table, "SPAWNZ", "UUID", loc.getZ(), this.getUuidIsland());
            plugin.getDbManager().set(TableType.ISLANDS.table, "COUNT", "UUID", this.getCount(), this.getUuidIsland());
            plugin.getLogger().log(Level.INFO, "Island " + this.getUuidIsland() + " saved");
            plugin.getIslandManager().getIslands().remove(this.getUuidIsland());
        },2);

    }
    public UUID getOwner(){
        UUID uuidPlayer = null;
        for(UUID uuid : members.keySet()){
            if(members.get(uuid).equals(RANK.OWNER)){
                uuidPlayer = uuid;
                break;
            }
        }
        return uuidPlayer;
    }


    public void teleportHere(Player player){
        player.teleport(getSpawnLocation());


    }
    public Block getMiddleBlock() {
        return bukkitWorld.getBlockAt(0, 81, 0);
    }

    public void setSpawnLocation(double x, double y, double z) {
        spawnLocation = new Location(bukkitWorld, x, y, z);
    }
}
