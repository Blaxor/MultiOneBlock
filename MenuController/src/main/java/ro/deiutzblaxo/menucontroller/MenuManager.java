package ro.deiutzblaxo.menucontroller;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.menucontroller.objects.Menu;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Getter
public class MenuManager {
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final JavaPlugin plugin;

    private final Class openMenu;

    public MenuManager(JavaPlugin plugin, Class openMenu) {
        this.plugin = plugin;
        this.openMenu = openMenu;
    }

    public Menu getMenu(String id) {
        if (menus.containsKey(id)) {
            return menus.get(id);
        }
        throw new RuntimeException("Menu " + id + " not found!");
    }

    public void openMenu(String id, Player player) {

        try {
            Bukkit.getPluginManager().callEvent((Event) openMenu.getConstructor(Menu.class, Player.class).newInstance(getMenu(id), player));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public void addMenu(Menu menu) {
        menus.put(menu.getID(), menu);
    }


    protected String cc(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

/*        plugin.getIslandLevelManager().getTopIslands().forEach(triplet -> {
        AtomicReference<String> abc = new AtomicReference<>("");
        triplet.getLast().getMembers().forEach((uuid, rank) -> {
            if (rank == RANK.OWNER) {
                try {
                    abc.set(plugin.getPlayerManager().getNameByUUID(uuid));
                } catch (PlayerNoExistException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        abc.set(abc.get() + " level " + triplet.getMiddle());//TODO MESSAGE
        sender.sendMessage(abc.get());
    });*/
}
