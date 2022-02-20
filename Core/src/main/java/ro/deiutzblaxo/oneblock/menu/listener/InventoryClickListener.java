package ro.deiutzblaxo.oneblock.menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ro.deiutzblax.expendeditems.item.utill.tags.ItemTags;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.menucontroller.objects.Menu;
import ro.deiutzblaxo.menucontroller.objects.menus.InventoryTag;

public class InventoryClickListener implements Listener {
    private final OneBlock plugin;

    public InventoryClickListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof InventoryTag) {
            if (((InventoryTag) inventory.getHolder()).getType().equalsIgnoreCase("menu")) {
                if (!ItemTags.containtsKNBTTag(event.getCurrentItem(), "menu")) {
                    event.setCancelled(true);
                    return;
                }
                Menu menu = plugin.getMenuManager().getMenu(ItemTags.getNBTTag(event.getCurrentItem(), "menu"));
                menu.getButton(event.getSlot()).onClick((Player) event.getWhoClicked(), event.getClick(),PlayerOpenMenuEvent.class);
                event.setCancelled(true);
                return;
            }
        }
    }
}
