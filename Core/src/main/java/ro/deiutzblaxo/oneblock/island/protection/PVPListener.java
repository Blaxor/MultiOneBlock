package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class PVPListener implements Listener {
    private final OneBlock plugin;

    public PVPListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (event.getDamager().hasPermission("oneblock.bypass.pvp"))
            return;

        Island island = plugin.getIslandManager().getIsland(event.getEntity().getLocation().getWorld().getName());
        if (island == null) return;
        if (!island.getSetting(ISLANDSETTINGS.PVP)) {
            event.setCancelled(true);
            event.getDamager().sendMessage(plugin.getLangManager().get((Player) event.getDamager(), MESSAGE.ISLAND_PVP_NOT_ALLOW));
        }


    }
}
