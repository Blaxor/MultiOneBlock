package ro.deiutzblaxo.oneblock.menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ro.deiutzblaxo.nbtapi.de.tr7zw.changeme.NBTContainer;
import ro.deiutzblaxo.nbtapi.de.tr7zw.changeme.NBTItem;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;
import ro.deiutzblaxo.oneblock.menu.objects.menus.InventoryTag;

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
                NBTContainer itemNBT = NBTItem.convertItemtoNBT(event.getCurrentItem());
                if (!itemNBT.hasKey("menu")) {
                    event.setCancelled(true);
                    return;
                }
                Menu menu = plugin.getMenuManager().getMenu(itemNBT.getString("menu"));
                menu.getButton(event.getSlot()).onClick((Player) event.getWhoClicked(), event.getClick());
                event.setCancelled(true);
            }
        }
    }
}
