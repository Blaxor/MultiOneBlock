package ro.deiutzblaxo.oneblock.menu;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
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
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.Triplet;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MenuManager {
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final OneBlock plugin;

    public MenuManager(OneBlock plugin) {
        this.plugin = plugin;

        addMenu(new PrefabMenu("phase", "test", plugin.getLangManager().get(MESSAGE.GUI_TITLE_PHASES), 9));
        addMenu(new PrefabMenu("members", "test", plugin.getLangManager().get(MESSAGE.ISLAND_TEAM_MENU_TITLE), 9));
        addMenu(new PrefabMenu("top", "test", plugin.getLangManager().get(MESSAGE.ISLAND_TOP_MENU), 9));
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

    @SneakyThrows
    public Menu getTopMenu() {

        ArrayList<Triplet<String, Integer, IslandMeta>> triplets = plugin.getIslandLevelManager().getTopIslands();
        Menu menu = getMenu("top");
        int i = 0;
        List<String> lore;

        for (Triplet<String, Integer, IslandMeta> trip : triplets) {
            lore = new ArrayList<>();
            lore.add(ChatColor.AQUA + "Level: " + trip.getMiddle());
            lore.add(" ");
            lore.add(ChatColor.GREEN + "Membrii");
            UUID owner = null;
            for (UUID uuid : trip.getLast().getMembers().keySet()) {
                if (trip.getLast().getMembers().get(uuid).equals(RANK.OWNER)) {
                    owner = uuid;
                }
                lore.add(ChatColor.GRAY + plugin.getPlayerManager().getNameByUUID(uuid));
            }
            menu.addButton(i, new PrefabButton(UTILS.getSkull(owner), ChatColor.GREEN + plugin.getPlayerManager().getNameByUUID(owner), lore, Action.CLOSE, null, menu));
            i++;
            if (i == 9)
                return menu;

        }
        return menu;


    }

    private String cc(String string) {
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
