package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class ExplosionListener implements Listener {
    private final OneBlock plugin;

    public ExplosionListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getLocation().getWorld().getName());
        if (island == null) return;
        if (event.getEntityType() == EntityType.CREEPER)
            if (!island.getSetting(ISLANDSETTINGS.CREEPER_EXPLOSION)) event.setCancelled(true);
        if (event.getEntityType() == EntityType.MINECART_TNT || event.getEntityType() == EntityType.PRIMED_TNT)
            if (!island.getSetting(ISLANDSETTINGS.TNT_EXPLOSION)) event.setCancelled(true);
    }
}
