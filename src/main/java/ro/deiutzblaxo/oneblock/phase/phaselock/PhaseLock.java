package ro.deiutzblaxo.oneblock.phase.phaselock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MESSAGELIST;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;

public class PhaseLock {

    public static ItemStack getPhaseLockItem(OneBlock plugin) {
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(plugin.getLangManager().getList(MESSAGELIST.ITEM_LOCK_LORE));
        meta.setDisplayName(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_ITEM_DISPLAY));
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.getByName("lock"), 1);
        return item;
    }

    //TODO CONFIGURABLE
    public static Inventory openInventory(OneBlock plugin) {
        Inventory inventory = Bukkit.createInventory(null, 9, plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_TITLE));
        ItemStack lock = new ItemStack(Material.TRAPPED_CHEST);
        ItemMeta lock_meta = lock.getItemMeta();
        lock_meta.setDisplayName(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_ITEM_LOCK_DISPLAY));
        lock_meta.setLore(plugin.getLangManager().getList(MESSAGELIST.PHASE_LOCK_MENU_ITEM_LOCK_LORE));
        lock.setItemMeta(lock_meta);
        inventory.setItem(2, EnchantsManager.addGlow(lock));

        ItemStack unlock = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta unlock_meta = unlock.getItemMeta();
        unlock_meta.setDisplayName(plugin.getLangManager().get(MESSAGE.PHASE_LOCK_MENU_ITEM_UNLOCK_DISPLAY));
        unlock_meta.setLore(plugin.getLangManager().getList(MESSAGELIST.PHASE_LOCK_MENU_ITEM_UNLOCK_LORE));
        unlock.setItemMeta(unlock_meta);
        inventory.setItem(5, EnchantsManager.addGlow(unlock));
        return inventory;


    }
}

