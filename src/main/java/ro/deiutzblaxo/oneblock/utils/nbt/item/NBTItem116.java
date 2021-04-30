package ro.deiutzblaxo.oneblock.utils.nbt.item;


import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItem116 {

    public static ItemStack addNBTTag(ItemStack item, String key, String value) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        tag.setString(key, value);
        stack.setTag(tag);
        return CraftItemStack.asCraftMirror(stack);
    }

    public static ItemStack removeNBTTag(ItemStack item, String key) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        tag.remove(key);
        return CraftItemStack.asCraftMirror(stack);
    }

    public static String getNBTTag(ItemStack item, String key) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        return tag.getString(key);

    }


    public static boolean containsKVNBTTag(ItemStack item, String key, String value) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) return false;
        if (!tag.hasKey(key)) return false;
        return tag.getString(key) == value;
    }

    public static boolean containtsKNBTTag(ItemStack item, String key) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) return false;
        return tag.hasKey(key);
    }

    public static boolean containsVNBTTag(ItemStack item, String value) {
        net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) return false;
        for (String string : tag.getKeys()) {
            if (tag.getString(string) == value) {
                return true;
            }
        }
        return false;

    }
}
