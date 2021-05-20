package ro.deiutzblaxo.oneblock.menu.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.menu.events.PlayerOpenMenuEvent;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.Action;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.ButtonObject;
import ro.deiutzblaxo.oneblock.utils.nbt.item.NBTItem116;

import java.util.List;
import java.util.logging.Level;

public interface Button {

    default void onClick(Player player) {
        Bukkit.getLogger().log(Level.INFO, "MenuManager: " + player.getName() + " clicked on the button " + ChatColor.stripColor(getName()) + " in the menu " + getParent().getID());
        switch (getAction()) {
            case CONSOLE_COMMAND:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getButtonObject().getCommand());
                break;
            case GIVE_ITEM:
                player.getInventory().addItem(getButtonObject().getItem());
                break;
            case OPEN_MENU:
                Bukkit.getPluginManager().callEvent(new PlayerOpenMenuEvent(getButtonObject().getMenu(), player));
                break;
            case SEND_MESSAGE:
                player.sendMessage(getButtonObject().getMessage());
                break;
            case PLAYER_COMMAND:
                Bukkit.dispatchCommand(player, getButtonObject().getCommand());
                break;
            case NONE:
                break;
            case CLOSE:
                player.closeInventory();
                break;
        }
    }

    Material getMaterial();

    ItemStack getItemDefault();

    void setMaterial(Material material);

    String getName();

    void setName(String name);

    List<String> getLore();

    void setLore(List<String> lore);

    Action getAction();

    ButtonObject getButtonObject();

    Menu getParent();

    boolean isGlow();

    default ItemStack getItem() {
        ItemStack item;
        if (getMaterial() == null)
            item = this.getItemDefault();
        else
            item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(getLore());
        item.setItemMeta(meta);
        if (isGlow()) {
            item = EnchantsManager.addGlow(item);
        }

        return NBTItem116.addNBTTag(item, "menu", getParent().getID());


    }


}
