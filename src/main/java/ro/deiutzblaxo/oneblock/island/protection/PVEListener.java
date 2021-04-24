package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;

public class PVEListener implements Listener {
    private OneBlock plugin;

    public PVEListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    public void onPVE(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasPermission("oneblock.bypass.pve"))
            return;
        if (event.getEntity().getType() == EntityType.PLAYER)
            return;
        Island island = plugin.getIslandManager().getIsland(event.getEntity().getWorld().getName());
        if (island == null) return;
        if (!island.isAllow(event.getDamager().getUniqueId(), PERMISSIONS.PVE)) {
            event.setCancelled(true);
        }
    }
}
