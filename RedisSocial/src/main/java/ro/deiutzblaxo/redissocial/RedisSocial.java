package ro.deiutzblaxo.redissocial;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.cloud.data.mysql.MySQLConnection;
import ro.deiutzblaxo.cloud.data.redis.RedisConnection;
import ro.deiutzblaxo.cloud.nus.NameUUIDManager;
import ro.deiutzblaxo.cloud.nus.prefab.NameUUIDStorageMySQL;
import ro.deiutzblaxo.cloud.nus.prefab.NameUUIDStorageRedis;
import ro.deiutzblaxo.cloud.nus.prefab.NameUUIDStorageYaml;
import ro.deiutzblaxo.redissocial.configmanagers.config.ConfigManager;
import ro.deiutzblaxo.redissocial.configmanagers.language.MessageManager;
import ro.deiutzblaxo.redissocial.listeners.JoinQuitListener;
import ro.deiutzblaxo.redissocial.object.PlayerSocial;
import ro.deiutzblaxo.redissocial.pm.PMManager;
import ro.deiutzblaxo.redissocial.pm.commands.PMCommand;
import ro.deiutzblaxo.redissocial.pm.commands.ReplayCommand;
import ro.deiutzblaxo.redissocial.pm.commands.SocialSpyCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
public final class RedisSocial extends JavaPlugin {
    private RedisConnection connection;
    private HashMap<UUID, PlayerSocial> player = new HashMap<>();
    private ArrayList<UUID> spions = new ArrayList<>();
    private NameUUIDManager nameUUIDManager;
    private PMManager PMManager;
    private MessageManager messageManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        connection = new RedisConnection(configManager.getFileConfiguration().getString("redis.hostname"),
                configManager.getFileConfiguration().getInt("redis.port"),
                configManager.getFileConfiguration().getString("redis.user").equalsIgnoreCase("NONE") ? "" : configManager.getFileConfiguration().getString("redis.user"),
                configManager.getFileConfiguration().getString("redis.password").equalsIgnoreCase("NONE") ? "" : configManager.getFileConfiguration().getString("redis.password"));
        nameUUIDManager = new NameUUIDManager(new NameUUIDStorageYaml(getDataFolder()), new NameUUIDStorageRedis(connection, "RedisSocial_"));

        if (configManager.getFileConfiguration().getBoolean("mysql-enable")) {
            MySQLConnection connection = new MySQLConnection(configManager.getFileConfiguration().getString("mysql.hostname"),
                    configManager.getFileConfiguration().getInt("mysql.port"), configManager.getFileConfiguration().getString("mysql.database"),
                    configManager.getFileConfiguration().getString("mysql.username"), configManager.getFileConfiguration().getString("mysql.password"), "autoReconnect=true");
            nameUUIDManager.getStorages().add(new NameUUIDStorageMySQL(connection, 2, "NAME"));
        }
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        setMessageManager(new MessageManager(this));
        setPMManager(new PMManager(this));

        getCommand("pm").setExecutor(new PMCommand(this));
        getCommand("r").setExecutor(new ReplayCommand(this));
        getCommand("socialspy").setExecutor(new SocialSpyCommand(this));
        getCommand("rsreload").setExecutor((sender, command, label, args) -> {messageManager.getMessageStringHashMap().clear();return false;});
        player.clear();
        getServer().getOnlinePlayers().forEach(player1 -> {
            player.put(player1.getUniqueId(), new PlayerSocial());
            if (player1.hasPermission("redissocial.spy.auto"))
                spions.add(player1.getUniqueId());
        });
    }

    @Override
    public void onDisable() {
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            getConnection().delete("socialredis:online:" + onlinePlayer.getName().toLowerCase(Locale.ROOT));
        }
        connection.getJedisPool().close();

    }
}


