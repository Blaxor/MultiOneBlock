package ro.deiutzblaxo.oneblock.customenchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.customenchants.enchants.GlowEnchant;
import ro.deiutzblaxo.oneblock.customenchants.enchants.LockEnchant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class EnchantsManager {
    private final OneBlock plugin;
    public static HashMap<String, Enchantment> a;

    public EnchantsManager(OneBlock main) {
        plugin = main;
        registerEnchantment(new GlowEnchant(plugin, "glow"));
        registerEnchantment(new LockEnchant(plugin, "lock"));


    }

    public static ItemStack addGlow(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.getByName("glow"), 1);
        return item;
    }


    private static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
            System.out.println(enchantment.getName() + " succesfully loaded!");

        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
            System.out.println(enchantment.getName() + " failed to load!");
        }
        if (registered) {

        }
    }
}
