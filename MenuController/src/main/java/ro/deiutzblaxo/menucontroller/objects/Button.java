package ro.deiutzblaxo.menucontroller.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblax.expendeditems.enchant.EnchantManager;
import ro.deiutzblax.expendeditems.item.utill.tags.ItemTags;
import ro.deiutzblaxo.menucontroller.objects.buttons.Action;
import ro.deiutzblaxo.menucontroller.objects.buttons.ButtonObject;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;

public interface Button {

    default void onClick(Player player, ClickType clickType, Class openMenuEvent) {
        Bukkit.getLogger().log(Level.INFO, "MenuManager: " + player.getName() + " clicked on the button " + ChatColor.stripColor(getName()) + " in the menu " + getParent().getID());
        switch (getAction()) {
            case CONSOLE_COMMAND:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getButtonObject().getCommand());
                break;
            case GIVE_ITEM:
                player.getInventory().addItem(getButtonObject().getItem());
                break;
            case OPEN_MENU:
                try {
                    Bukkit.getPluginManager().callEvent((Event) openMenuEvent.getConstructor(Menu.class, Player.class).newInstance(getButtonObject().getMenu(), player));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
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
            item = EnchantManager.addGlow(item);
        }

        return ItemTags.addNBTTag(item, "menu", getParent().getID());


    }


}
