package ro.deiutzblaxo.oneblock.menu.objects.menus;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import ro.deiutzblaxo.oneblock.menu.objects.Button;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;

import java.util.HashMap;

public class PrefabMenu implements Menu {

    private HashMap<Integer, Button> buttons = new HashMap<>();
    private String id;
    private String permission;
    private String name;
    private int size;

    public PrefabMenu(String id, String permission, String name, int size) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.size = size;
    }

    @Override
    public HashMap<Integer, Button> getButtons() {
        return buttons;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public Inventory getInterface() {
        InventoryHolderCustom invhold = new InventoryHolderCustom("menu");
        Inventory inv = Bukkit.createInventory(invhold, size, name);
        invhold.setInventory(inv);
        getButtons().forEach((integer, button) -> inv.setItem(integer, button.getItem()));
        return inv;
    }

}
