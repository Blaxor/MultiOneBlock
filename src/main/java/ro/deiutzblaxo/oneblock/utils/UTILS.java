package ro.deiutzblaxo.oneblock.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UTILS {

    public static HashMap<UUID, RANK> fromStringToHashMap(String string) {
        String[] players = string.split(";");
        HashMap<UUID, RANK> rank = new HashMap<>();
        for (String player : players) {
            String[] ranks = player.split(",");
            rank.put(UUID.fromString(ranks[0]), RANK.valueOf(ranks[1]));
        }
        return rank;
    }

    public static String fromHashMapToString(HashMap<UUID, RANK> hmap) {

        AtomicReference<String> str = new AtomicReference<>("");
        AtomicInteger i = new AtomicInteger();
        hmap.forEach((player, rank) -> {
            str.set(str.toString() + player.toString() + "," + rank.name());
            i.getAndIncrement();
            if (i.get() < hmap.size()) {
                str.set(str.toString() + ";");
            }
        });
        return str.toString();
    }

    public static String getEnchants(ItemStack i) {
        List<String> e = new ArrayList<String>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for (Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    public static String deserialize(ItemStack i) {
        String[] parts = new String[6];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        parts[3] = i.getItemMeta().getDisplayName();
        parts[4] = String.valueOf(i.getData().getData());
        parts[5] = getEnchants(i);
        return StringUtils.join(parts, ";");
    }

    public static ItemStack deserial(String p) {

        String[] a = p.split(";");
        ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
        i.setDurability((short) Integer.parseInt(a[2]));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(a[3]);
        i.setItemMeta(meta);
        MaterialData data = i.getData();
        data.setData((byte) Integer.parseInt(a[4]));
        i.setData(data);
        if (a.length > 5) {
            String[] parts = a[5].split(",");
            for (String s : parts) {
                String label = s.split(":")[0];
                String amplifier = s.split(":")[1];
                Enchantment type = Enchantment.getByName(label);
                if (type == null)
                    continue;
                int f;
                try {
                    f = Integer.parseInt(amplifier);
                } catch (Exception ex) {
                    continue;
                }
                i.addEnchantment(type, f);
            }
        }
        return i;
    }

    public static ItemStack getDecorativeGlass(OneBlock plugin) {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        item.addUnsafeEnchantment(Enchantment.getByName("glow"), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.getLangManager().get(MESSAGE.DECORATIVE_GLASS));
        //meta.setLore(MessagesManager.translate(Main.getMessagesManager().getList(MESSAGELIST.GLASS_LORE)));
        item.setItemMeta(meta);
        return item;
    }

    public static Inventory customSizeInventory(OneBlock plugin ,String name, int nr) {
        ItemStack item = getDecorativeGlass(plugin);
        if (nr <= 7) {

            Inventory inv = Bukkit.createInventory(null, 27, name);
            for (int i = 0; i < 9; i++)
                inv.setItem(i, item);
            for (int i = 18; i < 27; i++)
                inv.setItem(i, item);
            inv.setItem(9, item);
            inv.setItem(17, item);
            return inv;
        } else if (nr <= 14) {

            Inventory inv = Bukkit.createInventory(null, 36, name);
            for (int i = 0; i < 9; i++)
                inv.setItem(i, item);
            for (int i = 27; i < 36; i++)
                inv.setItem(i, item);
            inv.setItem(9, item);
            inv.setItem(17, item);

            inv.setItem(18, item);
            inv.setItem(26, item);
            return inv;
        } else if (nr <= 21) {
            Inventory inv = Bukkit.createInventory(null, 45, name);
            for (int i = 0; i < 9; i++)
                inv.setItem(i, item);
            for (int i = 36; i < 45; i++)
                inv.setItem(i, item);
            inv.setItem(9, item);
            inv.setItem(17, item);

            inv.setItem(18, item);
            inv.setItem(26, item);

            inv.setItem(27, item);
            inv.setItem(35, item);
            return inv;
        } else if (nr <= 28) {
            Inventory inv = Bukkit.createInventory(null, 54, name);
            for (int i = 0; i < 9; i++)
                inv.setItem(i, item);
            for (int i = 45; i < 54; i++)
                inv.setItem(i, item);
            inv.setItem(9, item);
            inv.setItem(17, item);

            inv.setItem(18, item);
            inv.setItem(26, item);

            inv.setItem(27, item);
            inv.setItem(35, item);

            inv.setItem(36, item);
            inv.setItem(44, item);
            return inv;
        }
        return null;
    }

    public static ItemStack getSkull(UUID uuidPlayer) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuidPlayer));
        skull.setItemMeta(skullMeta);
        return skull;
    }


}
