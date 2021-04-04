package ro.deiutzblaxo.oneblock.phase.gui;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MESSAGELIST;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.phase.PhaseManager;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.Collection;

public class PhaseMenu {


    public static Inventory getInventory(OneBlock plugin ,Phase phaseNow){
    Inventory inv = UTILS.customSizeInventory(plugin,plugin.getLangManager().get(MESSAGE.GUI_TITLE_PHASES),plugin.getPhaseManager().getPhaseHashMap().size());
    Collection<Phase> phases = plugin.getPhaseManager().getPhaseHashMap().values();
    phases.forEach(phase -> {
        ItemStack item = new ItemStack(phase.getFirstBlock().getMaterial());//ADD ICON MATERIAL
        if(phase == phaseNow){
            item.addUnsafeEnchantment(Enchantment.getByName("glow"),1);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(plugin.getLangManager().getList(MESSAGELIST.PHASES_NOW_LORE));
            item.setItemMeta(meta);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessagesManager.translate(phase.getPhaseName()));

        item.setItemMeta(meta);
        inv.addItem(item);
    });
    return inv;
    }


}
