package ro.deiutzblax.expendeditems.enchant.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class GlowEnchantment extends CustomEnchantment {

    public String description;

    public GlowEnchantment() {
        super("glow", null);
    }

    public GlowEnchantment(String description) {
        super("glow", description);
    }

    @Override
    public boolean isPlaceable() {
        return false;
    }

    @Override
    public String getName() {
        return "glow";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}
