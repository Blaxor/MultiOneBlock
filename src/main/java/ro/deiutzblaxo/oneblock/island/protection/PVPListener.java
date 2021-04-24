package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class PVPListener implements Listener {
    private OneBlock plugin;

    public PVPListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasPermission("oneblock.bypass.pvp"))
            return;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Island island = plugin.getIslandManager().getIsland(event.getEntity().getLocation().getWorld().getName());
        if (island == null) return;
        if (!island.getSetting(ISLANDSETTINGS.PVP))
            event.setCancelled(true);


    }
}
