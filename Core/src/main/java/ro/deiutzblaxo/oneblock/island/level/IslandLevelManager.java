package ro.deiutzblaxo.oneblock.island.level;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.level.calculate.IslandLevelCalculateManager;
import ro.deiutzblaxo.oneblock.island.level.calculate.IslandLevelCalculator;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.Action;
import ro.deiutzblaxo.oneblock.menu.objects.buttons.PrefabButton;
import ro.deiutzblaxo.oneblock.player.Rank;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.Triplet;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class IslandLevelManager {
    @Getter
    private final ArrayList<Triplet<String, Integer, IslandMeta>> topIslands = new ArrayList<>();
    @Getter
    private final IslandLevelCalculateManager islandLevelCalculateManager;
    private final OneBlock plugin;

    public IslandLevelManager(OneBlock plugin) {
        this.plugin = plugin;

        islandLevelCalculateManager = new IslandLevelCalculateManager(plugin);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                updateTOP();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 15, 20 * 60 * 5);
        updateBlockValue();
    }

    private void updateTOP() throws Exception {
        ResultSet set;
        PreparedStatement statement = plugin.getDbManager().getPrepareStatement("SELECT UUID,LEVEL FROM LEVEL ORDER BY LEVEL DESC LIMIT 10");
        topIslands.clear();
        set = statement.executeQuery();
        while (set.next()) {
            String uuid = set.getString("UUID");
            int level = set.getInt("LEVEL");
            IslandMeta meta = IslandMeta.deserialize(plugin.getDbManager().getString(TableType.ISLANDS.table, "META", "UUID", uuid));
            topIslands.add(new Triplet(uuid, level, meta));
        }
        set.close();
        statement.getConnection().close();
        CompletableFuture.runAsync(() -> {

            Menu menu = plugin.getMenuManager().getMenu("top");
            menu.getButtons().clear();
            int i = 0;
            List<String> lore;

            for (Triplet<String, Integer, IslandMeta> trip : topIslands) {
                lore = new ArrayList<>();
                lore.add(ChatColor.AQUA + "Level: " + trip.getMiddle());
                lore.add(" ");
                lore.add(ChatColor.GREEN + "Membrii");
                UUID owner = null;
                for (UUID uuid : trip.getLast().getMembers().keySet()) {
                    if (trip.getLast().getMembers().get(uuid).equals(Rank.RankEnum.OWNER)) {
                        owner = uuid;
                    }
                    try {
                        lore.add(ChatColor.GRAY + plugin.getPlayerManager().getNameByUUID(uuid));
                    } catch (PlayerNoExistException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    menu.addButton(i, new PrefabButton(UTILS.getSkull(owner), ChatColor.GREEN + plugin.getPlayerManager().getNameByUUID(owner), lore, Action.CLOSE, null, menu));
                } catch (PlayerNoExistException e) {
                    e.printStackTrace();
                }
                i++;
                if (i == 9)
                    return;
            }

        }, plugin.generalPool);
        plugin.getLogger().log(Level.INFO, "Top menu updated!");
    }

    private void updateBlockValue() {
        File file = new File(plugin.getDataFolder(), "blockconfig.yml");
        if (!file.exists()) {
            plugin.saveResource("blockconfig.yml", true);
            updateBlockValue();
            return;

        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("blocks");
        section.getKeys(true).forEach(s -> {
            IslandLevelCalculator.blockValue.put(Material.matchMaterial(s), section.getInt(s));
        });
    }


}
