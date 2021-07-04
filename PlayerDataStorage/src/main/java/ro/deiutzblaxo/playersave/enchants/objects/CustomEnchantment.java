package ro.deiutzblaxo.playersave.enchants.objects;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import ro.deiutzblaxo.playersave.PlayerSave;

public abstract class CustomEnchantment extends Enchantment {
    public CustomEnchantment(@NotNull String name) {
        super(new NamespacedKey(PlayerSave.getInstance(), name));
    }

    public abstract boolean isPlaceable();
}
