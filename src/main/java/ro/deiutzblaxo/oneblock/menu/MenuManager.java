package ro.deiutzblaxo.oneblock.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MESSAGELIST;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.menu.events.PlayerOpenMenuEvent;
import ro.deiutzblaxo.oneblock.menu.objects.Button;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.Action;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.PrefabButton;
import ro.deiutzblaxo.oneblock.menu.objects.menus.PrefabMenu;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MenuManager {
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final OneBlock plugin;

    public MenuManager(OneBlock plugin) {
        this.plugin = plugin;

        addMenu(new PrefabMenu("phase", "test", plugin.getLangManager().get(MESSAGE.GUI_TITLE_PHASES), 9));
        addMenu(new PrefabMenu("members", "test", plugin.getLangManager().get(MESSAGE.ISLAND_TEAM_MENU_TITLE), 9));
    }

    public Menu getMenu(String id) {
        if (menus.containsKey(id)) {
            return menus.get(id);
        }
        throw new RuntimeException("Menu " + id + " not found!");
    }

    public void openMenu(String id, Player player) {

        Bukkit.getPluginManager().callEvent(new PlayerOpenMenuEvent(getMenu(id), player));

    }

    public void addMenu(Menu menu) {
        menus.put(menu.getID(), menu);
    }

    public Menu getPhaseMenu(Island island) {
        Menu menu = getMenu("phase");
        menu.getButtons().clear();
        menu.setSize(plugin.getPhaseManager().getPhaseHashMap().size());
        int pos = 0;
        Phase now = island.getPhase();
        for (Phase phase : plugin.getPhaseManager().getPhaseHashMap().values()) {
            if (phase.isReset())
                continue;
            PrefabButton button = new PrefabButton(phase.getFirstBlock().getMaterial(), cc(phase.getPhaseName()), MessagesManager.replace(plugin.getLangManager().getList(MESSAGELIST.PHASES_LORE), "{blocks}", phase.getBlockNumber() + ""), Action.CLOSE, null, menu);
            if (phase.equals(now)) {
                button.setGlow(true);
                button.setLore(MessagesManager.replace(plugin.getLangManager().getList(MESSAGELIST.PHASES_NOW_LORE), "{blocks}", phase.getBlockNumber() + ""));
            }


            menu.addButton(pos, button);
            pos++;
        }
        return menu;
    }

    public Menu getMembersMenu(Island island) {
        Menu menu = getMenu("members");
        menu.getButtons().clear();
        menu.setSize(island.getMeta().getMembers().size());
        AtomicInteger i = new AtomicInteger();
        island.getMeta().getMembers().forEach((uuid, rank) -> {
            try {
                Button button = new PrefabButton(UTILS.getSkull(uuid), plugin.getPlayerManager().getNameByUUID(uuid), new ArrayList<String>() {{
                    add(ChatColor.GREEN + rank.name());
                }}, Action.CLOSE, null, menu);
                menu.addButton(i.get(), button);
            } catch (PlayerNoExistException e) {
                e.printStackTrace();
            }
            i.addAndGet(1);
        });

        return menu;
    }

    private String cc(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


}
