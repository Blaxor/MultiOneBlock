package ro.deiutzblaxo.oneblock.island.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNotHaveIsland;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TeamInfoMenu {

    /**
     *
     * @param island the island
     * @return the menu.
     * @throws PlayerNotHaveIsland - if {@link PlayerOB} don`t have island
     */
    public static Inventory getTeamMenu(OneBlock plugin , Island island){


            Inventory inv = UTILS.customSizeInventory(plugin,plugin.getLangManager().get(MESSAGE.ISLAND_TEAM_MENU_TITLE),island.getMeta().getMembers().size());
            island.getMeta().getMembers().forEach((uuid, rank) -> {
                ItemStack item = UTILS.getSkull(uuid);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Bukkit.getOfflinePlayer(uuid).getName());
                ArrayList<String> lista = new ArrayList<>();
                lista.add(ChatColor.GREEN + rank.name());
                meta.setLore(lista);
                item.setItemMeta(meta);
                inv.addItem(item);

            });
            return inv;
    }
}
