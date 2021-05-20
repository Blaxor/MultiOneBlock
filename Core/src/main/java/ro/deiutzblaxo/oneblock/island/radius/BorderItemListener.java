package ro.deiutzblaxo.oneblock.island.radius;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

public class BorderItemListener implements Listener {
    private final OneBlock plugin;

    public BorderItemListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Island island = plugin.getIslandManager().getIsland(event.getPlayer().getLocation().getWorld().getName());
        if (island == null) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
            return;
        }

        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.UPGRADE_RADIUS)) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_ACCESS_DENY));
            return;
        }
        if (event.getItem() == null)
            return;

        if (!event.getItem().containsEnchantment(EnchantsManager.ENCHANTMENTS.get("tier")))
            return;
        if (BorderHandler.getMaxTier(island.getMeta().getRadiusType()) <= island.getMeta().getRadiusTire()) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_MAX));
            event.setCancelled(true);
            return;
        }
        player.getInventory().setItem(event.getHand(), null);
        BorderHandler.upgradeBorder(island);
        player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_SUCCES));


        event.setCancelled(true);
    }


}
