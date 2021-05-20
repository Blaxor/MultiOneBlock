package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class MobSpawningListener implements Listener {
    private final OneBlock plugin;

    public MobSpawningListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) return;
        Island island = plugin.getIslandManager().getIsland(event.getLocation().getWorld().getName());
        if (island == null) return;
        if (island.getSetting(ISLANDSETTINGS.MOB_SPAWNING)) return;
        event.setCancelled(true);

    }
}
