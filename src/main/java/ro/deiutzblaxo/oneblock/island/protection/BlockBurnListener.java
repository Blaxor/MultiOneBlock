package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class BlockBurnListener implements Listener {
    protected OneBlock plugin;

    public BlockBurnListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDestroy(BlockBurnEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
        if (island == null) {
            return;
        }
        if (!island.getSetting(ISLANDSETTINGS.BLOCK_BURN)) {
            event.setCancelled(true);
        }
    }


}
