package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class BlockIgniteListener implements Listener {

    private OneBlock plugin;

    public BlockIgniteListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLight(BlockIgniteEvent event) {
        if (event.getPlayer().hasPermission("oneblock.bypass.ignite"))
            return;
        if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL || event.getCause() != BlockIgniteEvent.IgniteCause.ARROW)
            return;
        Island island = plugin.getIslandManager().getIsland(event.getIgnitingBlock().getWorld().getName());
        if (island == null) return;

        if (!island.getSetting(ISLANDSETTINGS.IGNITE))
            event.setCancelled(true);
    }
}
