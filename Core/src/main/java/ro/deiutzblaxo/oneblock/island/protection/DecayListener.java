package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;

public class DecayListener implements Listener {
    private final OneBlock plugin;

    public DecayListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getWorld().getName());
        if (island == null) return;

        if (!island.getSetting(ISLANDSETTINGS.DECAY)) {
            event.setCancelled(true);
        }

    }
}
