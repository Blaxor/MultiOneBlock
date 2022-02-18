package ro.deiutzblaxo.enchants;


import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.enchants.objects.CustomEnchantment;
import ro.deiutzblaxo.enchants.objects.GlowEnchantment;
import ro.deiutzblaxo.enchants.objects.TierEnchantment;

public enum Enchantments {

    GLOW(new GlowEnchantment()), TIER(new TierEnchantment());

    @Getter
    public CustomEnchantment enchantment;

    Enchantments(CustomEnchantment enchantment) {
        this.enchantment = enchantment;
    }
}
