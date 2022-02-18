package ro.deiutzblaxo.oneblock.menu;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import ro.deiutzblaxo.enchants.EnchantManager;
import ro.deiutzblaxo.menucontroller.objects.Button;
import ro.deiutzblaxo.menucontroller.objects.Menu;
import ro.deiutzblaxo.menucontroller.objects.buttons.Action;
import ro.deiutzblaxo.menucontroller.objects.buttons.ButtonObject;
import ro.deiutzblaxo.menucontroller.objects.buttons.PrefabButton;
import ro.deiutzblaxo.menucontroller.objects.menus.PrefabMenu;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.langs.MESSAGELIST;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.menu.listener.PlayerOpenMenuEvent;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MenuManager extends ro.deiutzblaxo.menucontroller.MenuManager {
    private final HashMap<String, Menu> menus = new HashMap<>();
    private OneBlock plugin;

    public MenuManager(OneBlock plugin) {
        super(plugin, PlayerOpenMenuEvent.class);
        this.plugin = plugin;

        addMenu(new PrefabMenu("phase", "test", plugin.getLangManager().get(MESSAGE.GUI_TITLE_PHASES), 9));
        addMenu(new PrefabMenu("members", "test", plugin.getLangManager().get(MESSAGE.ISLAND_TEAM_MENU_TITLE), 9));
        addMenu(new PrefabMenu("top", "test", plugin.getLangManager().get(MESSAGE.ISLAND_TOP_MENU), 9));
        addMenu(new PrefabMenu("info", "test", plugin.getLangManager().get(MESSAGE.MENU_INFO_TITLE), 18));
        addMenu(new PrefabMenu("banned", "test", plugin.getLangManager().get(MESSAGE.MENU_BANNED), 54));
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

    public Menu getPhaseMenu(Island island, Menu previous) {

        Menu menu = getMenu("phase");
        menu.getButtons().clear();
        menu.setSize(getPlugin().getPhaseManager().getPhaseHashMap().size());
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
        if (previous != null) {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, plugin.getLangManager().get(MESSAGE.MENU_BACK), new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(previous), menu));
        } else {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, plugin.getLangManager().get(MESSAGE.MENU_BACK), new ArrayList<>(), Action.CLOSE, null, menu));
        }
        return menu;
    }

    public Menu getMembersMenu(Island island, Menu previous) {
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
        if (previous != null) {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(previous), menu));//TODO MESSAGE
        } else {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.CLOSE, null, menu));//TODO MESSAGE
        }

        return menu;
    }

    @SneakyThrows
    public Menu getTopMenu(Menu previous) {

        Menu menu = getMenu("top");
        if (previous != null) {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null && !menu.getButton(menu.getSize() - 1).getName().equalsIgnoreCase(ChatColor.RED + "Inapoi!") ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(previous), menu));//TODO MESSAGE
        } else {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null && !menu.getButton(menu.getSize() - 1).getName().equalsIgnoreCase(ChatColor.RED + "Inapoi!") ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.CLOSE, null, menu));//TODO MESSAGE
        }
        return menu;
    }

    public Menu getInfoIslandMenu(Island island) {
        Menu menu = getMenu("info");
        menu.getButtons().clear();

        Button members = new PrefabButton(UTILS.getEffectArrow(new ItemStack(Material.TIPPED_ARROW), PotionType.INSTANT_HEAL),
                ChatColor.GREEN + "Membrii" + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + island.getMeta().getMembers().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + island.getMeta().getMaxMembers() + ChatColor.DARK_GRAY + ")"
                , new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(getMembersMenu(island, menu)), menu);

        Button infinityBlocks = new PrefabButton(Material.COARSE_DIRT, ChatColor.YELLOW + "Blocurile Regenerabile", new ArrayList<>(), Action.SEND_MESSAGE,
                new ButtonObject(ChatColor.RED + "In lucru...", true), menu);

        Button spawn = new PrefabButton(Material.POLISHED_ANDESITE, ChatColor.AQUA + "Spawn-ul insulei", new ArrayList<String>() {{
            add(ChatColor.GRAY + "La cordonatele: " + ChatColor.WHITE + (int) island.getMeta().getSpawn().getX() + " " + (int) island.getMeta().getSpawn().getY() + " " + (int) island.getMeta().getSpawn().getZ());
        }}, Action.ISLAND_TP, null, menu);

        Button phase = new PrefabButton(new ItemStack(Material.NETHERITE_PICKAXE, 1), ChatColor.YELLOW + "Etape", new ArrayList<>() {{
            add(ChatColor.GRAY + "Blocuri sparte: " + ChatColor.WHITE + island.getMeta().getCount());
        }}, Action.OPEN_MENU, new ButtonObject(getPhaseMenu(island, menu)), menu);

        Button radius = new PrefabButton(EnchantManager.addGlow(new ItemStack(Material.TRIPWIRE_HOOK)), ChatColor.GREEN + "Tier: " + island.getMeta().getRadiusTire(), new ArrayList<>(), Action.NONE, null, menu);

        Button name = new PrefabButton(Material.NAME_TAG, island.getMeta().getName(), new ArrayList<>(), Action.NONE, null, menu);

        Button banned = new PrefabButton(UTILS.getEffectArrow(new ItemStack(Material.TIPPED_ARROW), PotionType.INSTANT_DAMAGE), ChatColor.RED + "Banatii", new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(getBannedMenu(island, menu)), menu);

        menu.addButton(9, members);
        menu.addButton(11, phase);
        menu.addButton(12, spawn);
        menu.addButton(4, name);
        menu.addButton(14, radius);
        menu.addButton(15, infinityBlocks);
        menu.addButton(17, banned);
        return menu;
    }


    @SneakyThrows
    public Menu getBannedMenu(Island island, Menu previous) {
        Menu menu = getMenu("banned");
        menu.getButtons().clear();
        int i = 0;
        for (UUID uuid : island.getMeta().getBanned()) {
            menu.addButton(i, new PrefabButton(UTILS.getSkull(uuid), ChatColor.RED + plugin.getPlayerManager().getNameByUUID(uuid), new ArrayList<>(), Action.CLOSE, null, menu));
            i++;
        }
        if (previous != null) {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.OPEN_MENU, new ButtonObject(previous), menu));//TODO MESSAGE
        } else {
            menu.addButton(menu.getButton(menu.getSize() - 1) != null ? menu.getSize() + 8 : menu.getSize() - 1, new PrefabButton(Material.BARRIER, ChatColor.RED + "Inapoi!", new ArrayList<>(), Action.CLOSE, null, menu));//TODO MESSAGE
        }


        return menu;

    }
}
