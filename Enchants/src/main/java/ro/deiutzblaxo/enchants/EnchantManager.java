package ro.deiutzblaxo.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.enchants.objects.CustomEnchantment;

import java.lang.reflect.Field;

public class EnchantManager {

    public static boolean contains(ItemStack item, Enchantment enchantment) {
        for (Enchantment enchantment1 : item.getEnchantments().keySet())
            if (enchantment1 instanceof CustomEnchantment)
                if (enchantment1.getName().equalsIgnoreCase(enchantment.getName()))
                    return true;

        return false;
    }

    public static ItemStack addGlow(ItemStack item) {
        item.addUnsafeEnchantment(Enchantments.GLOW.getEnchantment(), 1);
        return item;
    }


    public static void registerEnchantments() {
        for (Enchantments enc : Enchantments.values()) {
            boolean registered = true;
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
                Enchantment.registerEnchantment(enc.getEnchantment());
                System.out.println(enc.getEnchantment().getName() + " succesfully loaded!");

            } catch (Exception e) {
                registered = false;
                e.printStackTrace();
                System.out.println(enc.getEnchantment().getName() + " failed to load!");
            }
            if (registered) {

            }
        }
    }


}
