package ro.deiutzblaxo.oneblock.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.io.File;
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
    public static String getEnchants(ItemStack i){
        List<String> e = new ArrayList<String>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for(Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    public static String deserialize(ItemStack i){
        String[] parts = new String[6];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        parts[3] = i.getItemMeta().getDisplayName();
        parts[4] = String.valueOf(i.getData().getData());
        parts[5] = getEnchants(i);
        return StringUtils.join(parts, ";");
    }

    public static ItemStack deserial(String p){

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
                } catch(Exception ex) {
                    continue;
                }
                i.addEnchantment(type, f);
            }
        }
        return i;
    }

}
