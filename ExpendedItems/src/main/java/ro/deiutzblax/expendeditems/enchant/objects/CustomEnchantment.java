package ro.deiutzblax.expendeditems.enchant.objects;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomEnchantment extends Enchantment {
    @Getter
    private String description;

    public CustomEnchantment(String name, String description) {
        super(NamespacedKey.minecraft("customenchantment_" + name));
        this.description = description;
    }

    public abstract boolean isPlaceable();
}
