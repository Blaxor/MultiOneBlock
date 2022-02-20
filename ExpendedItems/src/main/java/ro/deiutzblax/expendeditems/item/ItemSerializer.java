package ro.deiutzblax.expendeditems.item;

import com.google.gson.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ro.deiutzblax.expendeditems.enchant.EnchantManager;
import ro.deiutzblax.expendeditems.item.rarity.RARITY;
import ro.deiutzblax.expendeditems.item.rarity.RarityManager;
import ro.deiutzblax.expendeditems.item.utill.tags.ItemTags;
import ro.deiutzblaxo.cloud.utils.objects.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemSerializer {

    public static String toJson(String id, ItemStack item) {

        HashMap<String, String> nbts = new HashMap<>();
        item.getItemMeta().getPersistentDataContainer().getKeys().forEach(namespacedKey -> {
            nbts.put(namespacedKey.getKey(), item.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING));
        });

        List<String> enchantments = new ArrayList<>();
        item.getEnchantments().forEach((enchantment, integer) -> {
            enchantments.add(enchantment.getKey().getKey() + "," + integer);
        });

        JsonObject obj = new JsonObject();

        obj.addProperty("id_item", id);
        obj.addProperty("material", item.getType().name());
        obj.addProperty("rarity", RarityManager.getRarity(item).name());
        obj.addProperty("display_name", item.getItemMeta().getDisplayName());
        JsonObject nbts_object = new JsonObject();
        nbts.forEach((key, value) -> {
            nbts_object.addProperty(key, value);
        });
        obj.add("nbt", nbts_object);


        JsonArray array = new JsonArray();
        if (item.getItemMeta().getLore() != null && item.getItemMeta().hasLore() && !item.getItemMeta().getLore().isEmpty()) {
            item.getItemMeta().getLore().forEach(array::add);
            obj.add("lore", array);
        }
        JsonArray array2 = new JsonArray();
        enchantments.forEach(array2::add);
        obj.add("enchantments", array2);
        return obj.toString();
    }

    public static Pair<String, ItemStack> toItem(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        Pair<String, ItemStack> pair = new Pair<>(null, null);
        if (obj.has("id_item")) {
            pair.setFirst(obj.get("id_item").getAsString());
        }
        ItemStack item;
        if (obj.has("material")) {
            item = new ItemStack(Material.matchMaterial(obj.get("material").getAsString()));
        } else {
            throw new IllegalStateException("No material found for item " + pair.getFirst());
        }
        if (obj.has("display_name")) {
            String displayName = obj.get("display_name").getAsString();
            System.out.println();
            System.out.println();
            System.out.println(displayName);
            System.out.println();
            System.out.println();
            if (displayName != "") {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
                item.setItemMeta(meta);
            }
        }
        if (obj.has("nbt")) {
            JsonObject nbts = obj.get("nbt").getAsJsonObject();
            nbts.keySet().forEach(s -> {
                ItemTags.addNBTTag(item, s, nbts.get(s).getAsString());
            });
        }

        if (obj.has("lore")) {
            JsonArray lore = obj.getAsJsonArray("lore");
            ArrayList<String> string = new ArrayList<>();
            lore.forEach(jsonElement -> {
                string.add(ChatColor.translateAlternateColorCodes('&', jsonElement.getAsString()));
            });
            ItemMeta meta = item.getItemMeta();
            meta.setLore(string);
            item.setItemMeta(meta);
        }
        if (obj.has("enchantments")) {
            JsonArray enchantments = obj.getAsJsonArray("enchantments");
            enchantments.forEach(jsonElement -> {
                String[] values = jsonElement.getAsString().split(",");
                item.addUnsafeEnchantment(EnchantManager.getEnchantment(values[0])
                        , Integer.valueOf(values[1]));
            });
        }

        if (obj.has("rarity"))
            if (RarityManager.getRarity(item) == null) {
                RarityManager.setRarity(item, RARITY.valueOf(obj.get("rarity").getAsString()));
            }
        pair.setLast(item);
        return pair;

    }


}
