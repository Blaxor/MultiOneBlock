package ro.deiutzblaxo.oneblock.island.radius;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.customenchants.enchants.IslandTireEnchant;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.radius.exceptions.BorderHighestTireException;
import ro.deiutzblaxo.oneblock.island.radius.exceptions.BorderLowestTireException;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MESSAGELIST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class BorderHandler {
    public static HashMap<String, ArrayList<Integer>> radius = new HashMap<>();
    public static HashMap<String, String> permission = new HashMap<>();

    public BorderHandler(OneBlock plugin) {
        ConfigurationSection radiuss = plugin.getConfig().getConfigurationSection("radius");
        radiuss.getKeys(false).forEach(s -> {

            radius.put(s, new ArrayList<Integer>() {{
                Arrays.asList(plugin.getConfig().getString("radius." + s + ".tiers").split(",")).stream()
                        .sorted(Comparator.comparingInt(Integer::parseInt)).forEach(s1 -> add(Integer.parseInt(s1)));
            }});

            permission.put(s, plugin.getConfig().getString("radius." + s + ".permission"));
        });
        EnchantsManager.registerEnchantment(new IslandTireEnchant(plugin, "tier", radius.values().stream().max(Comparator.comparingInt(ArrayList::size)).get().size()));
    }

    public static ItemStack getItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(OneBlock.getInstance().getLangManager().get(MESSAGE.UPGRADE_TIER_ITEM_DISPLAY_NAME));
        meta.setLore(OneBlock.getInstance().getLangManager().getList(MESSAGELIST.UPGRADE_TIER_ITEM_LORE));
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(EnchantsManager.ENCHANTMENTS.get("tier"), 1);
        return item;
    }


    public static int getRadius(String type, Integer tier) {
        return radius.get(type).get(tier - 1);
    }

    public static void upgradeBorder(Island island) {
        if (island.getMeta().getRadiusTire() + 1 > radius.get(island.getMeta().getRadiusType()).size()) {
            throw new BorderHighestTireException("The island " + island.getUuidIsland() + " has the highest tire!");
        }
        island.getMeta().setRadiusTire(island.getMeta().getRadiusTire() + 1);
        island.changeBorder();
    }

    public static void downgradeBorder(Island island) {
        if (island.getMeta().getRadiusTire() > 1) {
            throw new BorderLowestTireException("The island " + island.getUuidIsland() + " has the lowest tire!");
        }
        island.getMeta().setRadiusTire(island.getMeta().getRadiusTire() - 1);
        island.changeBorder();
    }

    public static void changeBorderType(Island island, String type) {
        island.getMeta().setRadiusType(type);
        island.changeBorder();
    }

    public static void changeBorderTire(Island island, int tire) {
        island.getMeta().setRadiusTire(tire);
        island.changeBorder();
    }

    public static String getPermissionByType(String type) {
        return permission.get(type);
    }

    public static String getTypeByPermission(Player player) {
        String realType = "member";
        for (String type : permission.keySet()) {
            if (player.hasPermission(permission.get(type)))
                realType = type;
        }
        return realType;
    }

    public static int getMaxTier(String type) {
        ArrayList<Integer> tiers = radius.get(type);
        if (tiers == null) {
            return 0;
        }
        if (tiers.isEmpty()) {
            return 0;
        }
        return tiers.size();
    }

    public static ItemStack getItemByTier(String type, int tier) {
        if (getMaxTier(type) < tier) {
            throw new BorderHighestTireException("The island is on highest level");
        }
        ItemStack item = new ItemStack(Material.GRASS_BLOCK);

        ArrayList<String> lore = new ArrayList<String>() {{
            add("You will set the tier of your island to " + tier);
        }};
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.YELLOW + "Tier update!");

        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.getByName("tier_" + type), tier);

        return item;
    }

}
