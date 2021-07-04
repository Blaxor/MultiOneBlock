package ro.deiutzblaxo.playersave;

import lombok.Getter;
import me.stefan923.playerdatastorage.PlayerDataStorage;
import me.stefan923.playerdatastorage.mysql.MySQLConnection;
import me.stefan923.playerdatastorage.mysql.MySQLPlayerDataStorage;
import me.stefan923.playerdatastorage.playerdata.PlayerData;
import me.stefan923.playerdatastorage.util.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import ro.deiutzblaxo.playersave.enchants.EnchantManager;
import ro.deiutzblaxo.playersave.listeners.JoinQuitListener;

import java.util.Collection;

@Getter
public final class PlayerSave extends JavaPlugin {

    public static MySQLConnection playerSaveConnection;
    public static PlayerDataStorage playerSaveStorage;
    @Getter
    private static PlayerSave instance;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        playerSaveConnection = new MySQLConnection("playerData_", getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        playerSaveStorage = new MySQLPlayerDataStorage(playerSaveConnection);
        playerSaveStorage.createTable();
        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
        EnchantManager.registerEnchantments();


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
}
