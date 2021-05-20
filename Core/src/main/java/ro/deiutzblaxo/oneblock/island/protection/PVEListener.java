package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class PVEListener implements Listener {
    private final OneBlock plugin;

    public PVEListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPVE(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasPermission("oneblock.bypass.pve"))
            return;
        if (event.getEntity() instanceof Player)
            return;
        if (!(event.getDamager() instanceof Player))
            return;
        Island island = plugin.getIslandManager().getIsland(event.getEntity().getWorld().getName());
        if (island == null) return;
        if (!island.isAllow(event.getDamager().getUniqueId(), PERMISSIONS.PVE)) {
            event.setCancelled(true);
            event.getDamager().sendMessage(plugin.getLangManager().get((Player) event.getDamager(), MESSAGE.ISLAND_PVE_NOT_ALLOW));
        }
    }
}
