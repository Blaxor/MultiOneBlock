package ro.deiutzblaxo.oneblock.utils.item;

import com.google.common.reflect.Reflection;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.utils.item.enchs.Glow;

import java.lang.reflect.Field;

public class EnchantManager {

    public static ItemStack addGlow(ItemStack itemStack) {

        // adds protection to bows and infinity to every other item as infinity is only useful on bows and protection is only useful on armor
        itemStack.addUnsafeEnchantment(Enchantments.GLOW.getEnchantment(), 1);
        // hides the enchantments
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        // returns the new itemstack
        return itemStack;

    }

    public static boolean contains(ItemStack item, Enchantment enchantment) {
        if (item.getItemMeta().getEnchants().containsKey(enchantment))
            return true;
        return false;
    }


    public static void registerEnchantment(OneBlock plugin, Enchantment ench) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            Enchantment.registerEnchantment(ench);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
