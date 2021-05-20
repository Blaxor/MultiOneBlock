package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class FireSpreadListener implements Listener {
    protected OneBlock plugin;

    public FireSpreadListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        if (event.getSource().getType().equals(Material.FIRE)) {
            Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
            if (island == null) {
                return;
            }
            if (!island.getSetting(ISLANDSETTINGS.FIRE_SPREAD)) {
                event.setCancelled(true);
            }
        }

    }
}
