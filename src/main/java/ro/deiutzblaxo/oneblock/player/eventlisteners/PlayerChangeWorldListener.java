package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class PlayerChangeWorldListener implements Listener {
    private final OneBlock plugin;

    public PlayerChangeWorldListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChangeWorld(PlayerTeleportEvent event) {
        if (plugin.getIslandManager().getIsland(event.getTo().getWorld().getName()) != null) {
            Island island = plugin.getIslandManager().getIsland(event.getTo().getWorld().getName());
            if (island.isBanned(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(), MESSAGE.ISLAND_BANNED));
                event.setCancelled(true);
            }
            if (!island.isAllow(event.getPlayer().getUniqueId(), PERMISSIONS.TELEPORT)) {
                event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(),MESSAGE.ISLAND_ENTER_NOT_ALLOW));
                event.setCancelled(true);
            }
        }
    }
}
