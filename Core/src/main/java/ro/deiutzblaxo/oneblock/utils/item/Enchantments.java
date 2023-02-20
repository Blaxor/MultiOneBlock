package ro.deiutzblaxo.oneblock.utils.item;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Minecart;
import ro.deiutzblaxo.oneblock.utils.item.enchs.Glow;

public enum Enchantments {
    TIER, GLOW;

    public Enchantment getEnchantment() {
        return Enchantment.getByKey(NamespacedKey.minecraft(this.name().toLowerCase()));
    }
}
