package ro.deiutzblaxo.playersave.enchants;

import lombok.Getter;
import ro.deiutzblaxo.playersave.enchants.objects.CustomEnchantment;
import ro.deiutzblaxo.playersave.enchants.objects.GlowEnchantment;
import ro.deiutzblaxo.playersave.enchants.objects.TierEnchantment;

public enum Enchantments {

    GLOW(new GlowEnchantment()), TIER(new TierEnchantment());
    @Getter
    private CustomEnchantment enchantment;

    Enchantments(CustomEnchantment enchantment) {
        this.enchantment = enchantment;
    }
}
