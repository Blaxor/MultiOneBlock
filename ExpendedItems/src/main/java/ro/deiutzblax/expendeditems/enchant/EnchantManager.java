package ro.deiutzblax.expendeditems.enchant;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblax.expendeditems.enchant.objects.CustomEnchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

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

    public static Enchantment getEnchantment(String id) {
        Optional<Enchantments> enchantment = Arrays.stream(Enchantments.values()).filter(enchantments -> enchantments.name().equalsIgnoreCase(id)).findAny();
        if (enchantment.isPresent())
            return enchantment.get().getEnchantment();
        return Enchantment.getByKey(NamespacedKey.minecraft(id));
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
