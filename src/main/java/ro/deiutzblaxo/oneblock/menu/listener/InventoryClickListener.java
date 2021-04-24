package ro.deiutzblaxo.oneblock.menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;
import ro.deiutzblaxo.oneblock.menu.objects.menus.InventoryTag;
import ro.deiutzblaxo.oneblock.utils.nbt.item.NBTItem116;

public class InventoryClickListener implements Listener {
    private OneBlock plugin;

    public InventoryClickListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof InventoryTag) {
            if (((InventoryTag) inventory.getHolder()).getType().equalsIgnoreCase("menu")) {
                if (!NBTItem116.containtsKNBTTag(event.getCurrentItem(), "menu")) {
                    event.setCancelled(true);
                    return;
                }
                Menu menu = plugin.getMenuManager().getMenu(NBTItem116.getNBTTag(event.getCurrentItem(), "menu"));
                menu.getButton(event.getSlot()).onClick((Player) event.getWhoClicked());
                event.setCancelled(true);
                return;
            }
        }
    }
}
