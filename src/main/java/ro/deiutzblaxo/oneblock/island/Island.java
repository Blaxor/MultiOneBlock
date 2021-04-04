package ro.deiutzblaxo.oneblock.island;

import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.sql.Blob;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Setter
public class Island {

    private OneBlock plugin;
    private String uuidIsland;
    private String server;
    private SlimeWorld world;
    private World bukkitWorld;
    private IslandMeta meta;
    private BukkitTask autosave;
    private Phase phase;


    public Island(OneBlock plugin, String uuid, IslandMeta meta) {
        this.meta = meta;
        this.plugin = plugin;
        this.uuidIsland = uuid;
        this.server = OneBlock.SERVER;
        setPhase(plugin.getPhaseManager().getPhase(meta.getCount()));

        autosave = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getLogger().log(Level.INFO, "Auto-Saving island ", uuidIsland);
            save(false);
        }, 20 * 60 * 5, 20 * 60 * 5);
    }


    public void save(boolean unload) {
        try {
            plugin.getLogger().log(Level.INFO, "Saving island " + uuidIsland);
            Blob blob = plugin.getDbConnection().getConnection().createBlob();
            byte[] seril = meta.serialize();
            blob.setBytes(1, seril);
            if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuidIsland)) {
                plugin.getDbManager().setBlob(TableType.ISLANDS.table, "META", "UUID", (com.mysql.jdbc.Blob) blob, uuidIsland);
                plugin.getDbManager().setString(TableType.ISLANDS.table, "SERVER", "UUID", server, uuidIsland);
                WorldUtil.saveSlimeWorld(plugin, this.getWorld(), unload);
                plugin.getLogger().log(Level.INFO, "Saved island " + uuidIsland);
                return;
            }
            plugin.getDbManager().insert(TableType.ISLANDS.table, new String[]{"UUID", "META", "SERVER"}, new Object[]{uuidIsland, blob, server});
            WorldUtil.saveSlimeWorld(plugin, this.getWorld(), unload);

            plugin.getLogger().log(Level.INFO, "Saved island " + uuidIsland);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWorld() {

        plugin.getLogger().log(Level.INFO, "Loading world" + world.getName());
        plugin.getSlimePlugin().generateWorld(world);

        bukkitWorld = Bukkit.getWorld(world.getName());
        if (getMiddleBlock().getType() == Material.AIR) {
            getMiddleBlock().setType(getPhase().getFirstBlock().getMaterial());
        }
        setSpawnLocation(meta.getXSpawn(), meta.getYSpawn(), meta.getZSpawn());
        changeBorder();
        ChunkUtils.changeBiome(plugin, this);

    }

    public UUID getOwner() {
        UUID uuidPlayer = null;
        for (UUID uuid : meta.getMembers().keySet()) {
            if (meta.getMembers().get(uuid).equals(RANK.OWNER)) {
                uuidPlayer = uuid;
                break;
            }
        }
        return uuidPlayer;
    }


    public void teleportHere(Player player) {
        player.teleport(new Location(bukkitWorld, meta.getXSpawn(), meta.getYSpawn(), meta.getZSpawn()));
    }

    public Block getMiddleBlock() {
        return bukkitWorld.getBlockAt(0, 81, 0);
    }

    public void setSpawnLocation(double x, double y, double z) {
        meta.setXSpawn(x);
        meta.setYSpawn(y);
        meta.setZSpawn(z);
    }

    public boolean isLocked() {
        return meta.isLocked();
    }

    public void setLocked(boolean locked) {
        meta.setLocked(locked);
    }

    public void changeBorder() {
        bukkitWorld.getWorldBorder().setSize(meta.getRadius() * 2);
        bukkitWorld.getWorldBorder().setCenter(0, 0);

    }

    public void setPhase(Phase phase) {
        plugin.getLogger().log(Level.INFO, "Changed the phase of island " + uuidIsland + " to " + phase.getPhaseName());
        this.phase = phase;


    }

    public Location getSpawnLocation() {
        return new Location(bukkitWorld, meta.getXSpawn(), meta.getYSpawn(), meta.getZSpawn());
    }
}
