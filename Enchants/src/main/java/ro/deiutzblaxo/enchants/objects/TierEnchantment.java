package ro.deiutzblaxo.enchants.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.enchants.EnchantManager;
import ro.deiutzblaxo.oneblock.OneBlock;

public class TierEnchantment extends CustomEnchantment {

    public TierEnchantment() {
        super(OneBlock.getInstance(), "tier");
    }

    @Override
    public boolean isPlaceable() {
        return false;
    }

    @Override
    public String getName() {
        return "tier";
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
