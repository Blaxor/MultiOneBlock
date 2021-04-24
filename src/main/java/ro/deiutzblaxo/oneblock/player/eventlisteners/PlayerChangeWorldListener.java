package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;

public class PlayerChangeWorldListener implements Listener {
    private OneBlock plugin;

    public PlayerChangeWorldListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChangeWorld(PlayerTeleportEvent event) {
        if (plugin.getIslandManager().getIsland(event.getTo().getWorld().getName()) != null) {
            Island island = plugin.getIslandManager().getIsland(event.getTo().getWorld().getName());
            if (island.isBanned(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage("You are banned and can`t teleport there.");//TODO MESSAGE
                event.setCancelled(true);
            }
            if (!island.isAllow(event.getPlayer().getUniqueId(), PERMISSIONS.TELEPORT)) {
                event.getPlayer().sendMessage("You can`t enter on this island!");//TODO MESSAGE
                event.setCancelled(true);
            }
        }
    }
}
