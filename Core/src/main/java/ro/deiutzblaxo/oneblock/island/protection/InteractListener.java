package ro.deiutzblaxo.oneblock.island.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class InteractListener implements Listener {
    private final OneBlock plugin;

    public InteractListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("oneblock.bypass.interact"))
            return;
        Island island = plugin.getIslandManager().getIsland(event.getPlayer().getWorld().getName());
        if (island == null)
            return;
        if (!island.isAllow(event.getPlayer().getUniqueId(), PERMISSIONS.INTERACT)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(),MESSAGE.ISLAND_INTERACT_NOT_ALLOW));
        }
    }
}
