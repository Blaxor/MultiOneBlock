package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class BlockPlaceListener implements Listener {
    private final OneBlock plugin;

    public BlockPlaceListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("oneblock.bypass.place"))
            return;
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getWorld().getName());
        if (island == null) return;
        if (!island.isAllow(event.getPlayer().getUniqueId(), PERMISSIONS.PLACE)) {
            event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(), MESSAGE.ISLAND_PLACE_NOT_ALLOW));
            event.setCancelled(true);
        }

    }


}
