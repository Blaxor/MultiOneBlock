package ro.deiutzblaxo.enchants.tags;


import com.google.common.util.concurrent.ClosingFuture;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemTags {
    //TODO TEST THIS
    public static ItemStack addNBTTag(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft(key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeNBTTag(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(NamespacedKey.minecraft(key));
        item.setItemMeta(meta);
        return item;
    }

    public static String getNBTTag(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(NamespacedKey.minecraft(key), PersistentDataType.STRING);

    }


    public static boolean containsKVNBTTag(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        String found = meta.getPersistentDataContainer().get(NamespacedKey.minecraft(key), PersistentDataType.STRING);
        return found == value;
    }

    public static boolean containtsKNBTTag(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft(key), PersistentDataType.STRING);

    }

/*    public static boolean containsVNBTTag(ItemStack item, String value) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) return false;
        for (String string : tag.getKeys()) {
            if (tag.getString(string) == value) {
                return true;
            }
        }
        return false;

    }*/
}
