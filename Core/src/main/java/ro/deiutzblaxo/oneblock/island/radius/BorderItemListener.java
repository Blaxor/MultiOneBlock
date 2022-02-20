package ro.deiutzblaxo.oneblock.island.radius;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblax.expendeditems.enchant.EnchantManager;
import ro.deiutzblax.expendeditems.enchant.Enchantments;
import ro.deiutzblaxo.oneblock.OneBlock;
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

        if (event.getItem() == null)
            return;

        if (!EnchantManager.contains(event.getItem(), Enchantments.TIER.getEnchantment()))
            return;
        if (island == null) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
            return;
        }

        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.UPGRADE_RADIUS)) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_ACCESS_DENY));
            return;
        }
        if (BorderHandler.getMaxTier(island.getMeta().getRadiusType()) <= island.getMeta().getRadiusTire()) {
            player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_MAX));
            event.setCancelled(true);
            return;
        }
        if (player.getInventory().getItem(event.getHand()).getAmount() > 1) {
            ItemStack item = player.getInventory().getItem(event.getHand());
            item.setAmount(item.getAmount() - 1);
        } else
            player.getInventory().setItem(event.getHand(), null);
        BorderHandler.upgradeBorder(island);
        player.sendMessage(plugin.getLangManager().get(MESSAGE.UPGRADE_TIER_SUCCES));


        event.setCancelled(true);
    }


}
