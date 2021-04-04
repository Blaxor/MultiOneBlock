package ro.deiutzblaxo.oneblock.phase.phaselock;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

public class PhaseLockListener implements Listener {
    private OneBlock plugin;

    public PhaseLockListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))
            if (event.hasItem()) {
                ItemStack item = event.getItem();
                if (item.hasItemMeta()) {
                    if (item.getEnchantments() != null && !item.getEnchantments().isEmpty())
                        if (item.containsEnchantment(Enchantment.getByName("lock"))) {
                            event.setCancelled(true);
                            event.getPlayer().openInventory(PhaseLock.openInventory(plugin));
                        }
                }
            }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() != null) {
            ItemStack item = event.getItemInHand();
            if (item.hasItemMeta()) {
                if (item.containsEnchantment(Enchantment.getByName("look"))) {
                    event.setCancelled(true);
                    event.getPlayer().openInventory(PhaseLock.openInventory(plugin));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equalsIgnoreCase(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_TITLE))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null)
                if (event.getCurrentItem().hasItemMeta())
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_ITEM_LOCK_DISPLAY))) {

                        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
                        Island island = playerOB.getIsland(false);
                        if (island == null) {
                            event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
                            event.getWhoClicked().closeInventory();
                            return;
                        }
                        if (island.getMeta().isLocked()) {
                            event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.PHASES_ISLAND_ALREADY_LOCKED));
                            event.getWhoClicked().closeInventory();
                            return;
                        }
                        island.getMeta().setLocked(true);
                        island.save(false);
                        event.getWhoClicked().getInventory().setItem(event.getWhoClicked().getInventory().first(Material.BARRIER), null);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.PHASES_LOCK_MENU_MESSAGE_LOCK));
                        return;
                    } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_ITEM_UNLOCK_DISPLAY))) {

                        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
                        Island island = playerOB.getIsland(false);

                        if (island == null) {
                            event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
                            event.getWhoClicked().closeInventory();
                            return;
                        }
                        if (!island.getMeta().isLocked()) {
                            event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.PHASES_ISLAND_ALREADY_UNLOCKED));
                            event.getWhoClicked().closeInventory();
                            return;
                        }
                        island.getMeta().setLocked(false);
                        island.save(false);
                        event.getWhoClicked().getInventory().setItem(event.getWhoClicked().getInventory().first(Material.BARRIER), null);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(plugin.getLangManager().get(MESSAGE.PHASES_LOCK_MENU_MESSAGE_UNLOCK));

                    } else {
                        event.getWhoClicked().closeInventory();
                    }


        }
    }
}
