package ro.deiutzblax.expendeditems.enchant;


import lombok.Getter;
import ro.deiutzblax.expendeditems.enchant.objects.CustomEnchantment;
import ro.deiutzblax.expendeditems.enchant.objects.GlowEnchantment;
import ro.deiutzblax.expendeditems.enchant.objects.TierEnchantment;

public enum Enchantments {

    GLOW(new GlowEnchantment()), TIER(new TierEnchantment());

    @Getter
    public CustomEnchantment enchantment;

    Enchantments(CustomEnchantment enchantment) {
        this.enchantment = enchantment;
    }
}
