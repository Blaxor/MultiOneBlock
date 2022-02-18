package ro.deiutzblaxo.enchants.objects;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomEnchantment extends Enchantment {
    public CustomEnchantment(JavaPlugin plugin, String name) {
        super(new NamespacedKey(plugin, name));
    }

    public abstract boolean isPlaceable();
}
