package ro.deiutzblaxo.oneblock.island.radius;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;

public class BorderItemListener implements Listener {
    private OneBlock plugin;

    public BorderItemListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))
            if (event.hasItem()) {
                ItemStack item = event.getItem();
                if (item.hasItemMeta()) {
                    if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                        Enchantment enchantment = item.getEnchantments().keySet().stream().filter(enchantment1 ->
                        {

                            return enchantment1.getName().split("_")[0].equalsIgnoreCase("tier");
                        }).findAny().get();


                        if (!(enchantment == null)) {
                            event.setCancelled(true);
                            Island island = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getIsland(false);
                            int tier = item.getEnchantments().get(enchantment);
                            if (island.getMeta().getRadiusTire() >= tier) {
                                event.getPlayer().sendMessage("You need a more powerfull upgrade!");
                                return;
                            }
                            BorderHandler.changeBorderTire(island, item.getEnchantments().get(enchantment));
                            event.getPlayer().sendMessage("You have upgraded your island!!!!!!");//TODO MESSAGE
                            item.setType(null);
                        }
                    }
                }
            }
    }
}
