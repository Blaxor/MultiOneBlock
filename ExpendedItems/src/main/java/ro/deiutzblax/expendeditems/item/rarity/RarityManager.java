package ro.deiutzblax.expendeditems.item.rarity;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblax.expendeditems.item.utill.tags.ItemTags;

import java.util.ArrayList;
import java.util.List;

public class RarityManager {

    public static ItemStack setRarity(@NonNull ItemStack item, @NonNull RARITY rarity) {

        if (ItemTags.containtsKNBTTag(item, "rarity"))
            return item;
        item = ItemTags.addNBTTag(item, "rarity", rarity.name());

        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore())
            meta.getLore().add(ChatColor.translateAlternateColorCodes('&', rarity.getColor() + rarity.name()));
        else meta.setLore(new ArrayList<String>() {
            {
                add(ChatColor.translateAlternateColorCodes('&', rarity.getColor() + rarity.name()));
            }
        });
        item.setItemMeta(meta);
        return item;
    }

    public static RARITY getRarity(ItemStack item) {
        if (ItemTags.containtsKNBTTag(item, "rarity"))
            return RARITY.valueOf(ItemTags.getNBTTag(item, "rarity"));
        else return null;
    }

    public static ItemStack replaceRarity(@NonNull ItemStack item, @NonNull RARITY newRarity) {

        item = ItemTags.removeNBTTag(item, "rarity");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (meta.hasLore()) {
            lore.remove(lore.size() - 1);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return setRarity(item, newRarity);


    }


}
