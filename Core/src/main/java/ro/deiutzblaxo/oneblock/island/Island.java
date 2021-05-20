package ro.deiutzblaxo.oneblock.island;

import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.events.PlayerBanIslandEvent;
import ro.deiutzblaxo.oneblock.player.events.PlayerUnBanIslandEvent;
import ro.deiutzblaxo.oneblock.slimemanager.WorldUtil;
import ro.deiutzblaxo.oneblock.utils.ChunkUtils;
import ro.deiutzblaxo.oneblock.utils.Location;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.otherexceptions.NotSafeLocationException;
import ro.nexs.db.manager.exception.DifferentArgLengthException;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    private int level;


    public Island(OneBlock plugin, String uuid, IslandMeta meta) {
        this.meta = meta;
        this.plugin = plugin;
        this.uuidIsland = uuid;
        this.server = OneBlock.SERVER;
        setPhase(plugin.getPhaseManager().getPhase(meta.getCount()));


        autosave = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            plugin.getLogger().log(Level.INFO, "Auto-Saving island ", uuidIsland);
            if (bukkitWorld.getPlayers().isEmpty() && !Bukkit.getOnlinePlayers().stream().filter(player -> meta.getMembers().containsKey(player.getUniqueId())).findAny().isPresent()) {
                try {
                    plugin.getIslandManager().unloadIsland(this, true);
                } catch (IslandHasPlayersOnlineException e) {
                    e.printStackTrace();
                }
            } else
                save(false);
        }, 20 * 60 * 5, 20 * 60 * 5);

    }


    public void save(boolean unload) {
        try {
            plugin.getGeneralPool().submit(() -> {


                plugin.getLogger().log(Level.INFO, "Saving island " + uuidIsland);
                meta.setTime(getBukkitWorld().getTime());
                String seril = meta.serialize();

                if (plugin.getDbManager().existString(TableType.ISLANDS.table, "UUID", uuidIsland)) {
                    plugin.getDbManager().set(TableType.ISLANDS.table, "META", "UUID", seril, uuidIsland);
                    plugin.getDbManager().setString(TableType.ISLANDS.table, "SERVER", "UUID", server, uuidIsland);
                    saveLevel();
                    WorldUtil.saveSlimeWorld(plugin, this.getWorld());
                    plugin.getLogger().log(Level.INFO, "Saved island " + uuidIsland);
                    return;
                }
                try {
                    plugin.getDbManager().insert(TableType.ISLANDS.table, new String[]{"UUID", "META", "SERVER"}, new Object[]{uuidIsland, seril, server});
                } catch (DifferentArgLengthException e) {
                    e.printStackTrace();
                }

                saveLevel();
                WorldUtil.saveSlimeWorld(plugin, this.getWorld());
            });
            if (unload)
                WorldUtil.unloadSlimeWorld(plugin, this.getBukkitWorld());
            plugin.getLogger().log(Level.INFO, "Saved island " + uuidIsland);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void saveLevel() {
        if (plugin.getDbManager().existString(TableType.LEVEL.table, "UUID", uuidIsland))
            plugin.getDbManager().setInt(TableType.LEVEL.table, "LEVEL", "UUID", level, uuidIsland);
        else
            plugin.getDbManager().insert(TableType.LEVEL.table, new String[]{"UUID", "LEVEL"}, new Object[]{uuidIsland, level});
    }

    public void loadWorld() {

        plugin.getLogger().log(Level.INFO, "Loading world" + world.getName());
        plugin.getSlimePlugin().generateWorld(world);

        bukkitWorld = Bukkit.getWorld(world.getName());
        meta.getBlock().forEach(location -> {
            Block block = bukkitWorld.getBlockAt(location.toBukkitLocation(bukkitWorld));
            if (block == null)
                block.setType(getPhase().getFirstBlock().getMaterial());
            if (block.getType() == Material.AIR)
                block.setType(getPhase().getFirstBlock().getMaterial());
        });
        setSpawnLocation(meta.getSpawn());
        if (Bukkit.getPlayer(getOwner()) != null)
            getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(getOwner())));
        changeBorder();
        ChunkUtils.changeBiome(plugin, this);
        bukkitWorld.setTime(meta.getTime());

    }

    public UUID getOwner() {
        AtomicReference<UUID> owner = new AtomicReference<>();
        getMeta().getMembers().forEach((uuid, rank) -> {
            if (rank == RANK.OWNER) {
                owner.set(uuid);
                return;
            }

        });
        return owner.get();
    }


    public void teleportHere(Player player) {
        try {
            player.teleport(Location.getSafeLocation(meta.getSpawn().toBukkitLocation(bukkitWorld)));
        } catch (NotSafeLocationException e) {
            player.teleport(plugin.getSpawnLocation());
            player.sendMessage(plugin.getLangManager().get(player, MESSAGE.LOCATION_NOT_SAFE));

        }
/*        if (Location.isSafeLocation(meta.getSpawn().toBukkitLocation(bukkitWorld)))
            player.teleport(meta.getSpawn().toBukkitLocation(bukkitWorld));
        else {
            player.teleport(plugin.getSpawnLocation());
            player.sendMessage(plugin.getLangManager().get(player, MESSAGE.LOCATION_NOT_SAFE));
        }*/
    }

    public Block getBlock(org.bukkit.Location location) {
        if (meta.getBlock().contains(new Location(location)))
            return bukkitWorld.getBlockAt(location);
        return null;
    }

    public void setSpawnLocation(ro.deiutzblaxo.oneblock.utils.Location location) {
        meta.setSpawn(location);
    }

    public boolean isLocked() {
        return meta.isLocked();
    }

    public void setLocked(boolean locked) {
        meta.setLocked(locked);
    }

    public void changeBorder() {
        bukkitWorld.getWorldBorder().setSize(BorderHandler.getRadius(this.getMeta().getRadiusType(), this.getMeta().getRadiusTire()) * 2);
        bukkitWorld.getWorldBorder().setCenter(0, 0);

    }

    public void setPhase(Phase phase) {
        plugin.getLogger().log(Level.INFO, "Changed the phase of island " + uuidIsland + " to " + phase.getPhaseName());
        this.phase = phase;


    }

    public org.bukkit.Location getSpawnLocation() {
        return meta.getSpawn().toBukkitLocation(bukkitWorld);
    }

    public boolean isBanned(UUID uuid) {
        return meta.getBanned().contains(uuid);
    }

    public boolean isBanned(Player player) {
        return isBanned(player.getUniqueId());
    }

    public boolean ban(UUID uuid) {
        if (isBanned(uuid)) {
            return false;
        }

        Bukkit.getPluginManager().callEvent(new PlayerBanIslandEvent(plugin, uuid, this));
        return true;
    }

    public boolean ban(Player player) {
        return ban(player.getUniqueId());
    }

    public boolean unban(UUID uuid) {
        if (!isBanned(uuid)) {
            return false;
        }
        Bukkit.getPluginManager().callEvent(new PlayerUnBanIslandEvent(plugin, uuid, this));
        return true;
    }


    public boolean isAllow(UUID uuid, PERMISSIONS permission) {
        RANK target = meta.getMembers().get(uuid) == null ? RANK.GUEST : meta.getMembers().get(uuid);
        RANK permissionlowest = meta.getPermissions().get(permission) == null ? permission.getLowestRankDefault() : meta.getPermissions().get(permission);
        return PERMISSIONS.isAllow(target, permissionlowest);
    }

    public boolean getSetting(ISLANDSETTINGS setting) {
        return meta.getSettings().getOrDefault(setting, setting.isAllowDefault());

    }
}
//199
//200