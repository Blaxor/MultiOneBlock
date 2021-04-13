package ro.deiutzblaxo.oneblock.menu;

import lombok.Getter;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;

import java.util.HashMap;

@Getter
public class MenuManager {
    HashMap<String, Menu> menus = new HashMap<>();

    public Menu getMenu(String id) {
        if (menus.containsKey(id)) {
            return menus.get(id);
        }
        throw new RuntimeException("Menu " + id + " not found!");
    }

}
