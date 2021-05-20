package ro.deiutzblaxo.playersave;

import lombok.Getter;
import me.stefan923.playerdatastorage.PlayerDataStorage;
import me.stefan923.playerdatastorage.mysql.MySQLConnection;
import me.stefan923.playerdatastorage.mysql.MySQLPlayerDataStorage;
import me.stefan923.playerdatastorage.playerdata.PlayerData;
import me.stefan923.playerdatastorage.util.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.util.Arrays;
import java.util.Collection;

@Getter
public final class Playersave extends JavaPlugin implements Listener {

    public static MySQLConnection playerSaveConnection;
    public static PlayerDataStorage playerSaveStorage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        playerSaveConnection = new MySQLConnection("playerData_", getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        playerSaveStorage = new MySQLPlayerDataStorage(playerSaveConnection);
        playerSaveStorage.createTable();
        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
                PlayerData playerData = new PlayerData(
                        player.getUniqueId(),
                        player.getInventory().getContents(),
                        /*       player.getEnderChest().getContents(),*/
                        potionEffects.toArray(new PotionEffect[0]),
                        ExperienceUtil.getTotalExperience(player.getLevel(), player.getExp())
                );

                playerSaveStorage.savePlayerData(playerData);

            });

        }, 20 * 60 * 5, 20 * 60 * 5);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = playerSaveStorage.getPlayerData(player.getUniqueId());
        if (playerData != null) {
            player.getInventory().setContents(playerData.getInventoryContent());
            /*player.getEnderChest().setContents(playerData.getEnderChestContent());*/
            player.addPotionEffects(Arrays.asList(playerData.getPotionEffects()));
            ExperienceUtil.setTotalExperience(player, playerData.getTotalExperience());
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
        PlayerData playerData = new PlayerData(
                player.getUniqueId(),
                player.getInventory().getContents(),
                /*       player.getEnderChest().getContents(),*/
                potionEffects.toArray(new PotionEffect[0]),
                ExperienceUtil.getTotalExperience(player.getLevel(), player.getExp())
        );

        playerSaveStorage.savePlayerData(playerData);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
