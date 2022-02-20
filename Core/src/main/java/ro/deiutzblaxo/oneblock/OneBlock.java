package ro.deiutzblaxo.oneblock;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblax.expendeditems.enchant.EnchantManager;
import ro.deiutzblaxo.cloud.data.mysql.MySQLManager;
import ro.deiutzblaxo.cloud.data.mysql.classic.MySQLManagerNormal;
import ro.deiutzblaxo.cloud.data.mysql.hikari.MySQLConnectionHikari;
import ro.deiutzblaxo.cloud.nus.NameUUIDManager;
import ro.deiutzblaxo.cloud.nus.prefab.NameUUIDStorageMySQL;
import ro.deiutzblaxo.oneblock.addons.PAPIAddon;
import ro.deiutzblaxo.oneblock.commands.admin.AdminCommand;
import ro.deiutzblaxo.oneblock.commands.chat.GlobalCommand;
import ro.deiutzblaxo.oneblock.commands.island.IslandCommand;
import ro.deiutzblaxo.oneblock.communication.action.chat.ChannelChatListenerPluginMessage;
import ro.deiutzblaxo.oneblock.communication.action.invite.ChannelInviteListener;
import ro.deiutzblaxo.oneblock.communication.action.invite.ChannelInviteListenerRedis;
import ro.deiutzblaxo.oneblock.communication.redis.RedisManager;
import ro.deiutzblaxo.oneblock.island.IslandManager;
import ro.deiutzblaxo.oneblock.island.level.IslandLevelManager;
import ro.deiutzblaxo.oneblock.island.protection.*;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.island.radius.BorderItemListener;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.menu.MenuManager;
import ro.deiutzblaxo.oneblock.menu.listener.InventoryClickListener;
import ro.deiutzblaxo.oneblock.phase.PhaseManager;
import ro.deiutzblaxo.oneblock.player.PlayerManager;
import ro.deiutzblaxo.oneblock.player.eventlisteners.*;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.psi.PlayerRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public final class OneBlock extends JavaPlugin {
    private ro.deiutzblaxo.cloud.data.mysql.MySQLConnection dbConnection;
    private MySQLManager dbManager;
    private IslandManager islandManager;
    private PlayerManager playerManager;
    private SlimePlugin slimePlugin;
    private SlimeLoader loader;
    private PhaseManager phaseManager;
    @Setter
    private MessagesManager langManager;
    private IslandLevelManager islandLevelManager;
    private MenuManager menuManager;
    private PlayerRepo PSIRepo;
    private RedisManager redisManager;
    private NameUUIDManager nameUUIDManager;
    public static String SERVER;
    public static int THREADS_NUMBER;
    public static boolean REDIS_ENABLED = false;
    public static boolean LOG_REDIS = false;
    private static OneBlock instance;
    public ExecutorService generalPool = Executors.newFixedThreadPool(8);

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        THREADS_NUMBER = getConfig().getInt("threads_level");
        SERVER = getConfig().getString("server-name");

        this.dbConnection = new MySQLConnectionHikari(getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"), "autoReconnect=true",10,1);
        this.dbManager = new MySQLManagerNormal(this.dbConnection, 1);
        nameUUIDManager = new NameUUIDManager(/*new NameUUIDStorageYaml(getDataFolder(), 1000, PriorityNUS.HIGH),*/ new NameUUIDStorageMySQL(dbManager, "NAME"));
        PSIRepo = new PlayerRepo(dbConnection, 2, "PSI_TABLE");

        getDbManager().createTable(TableType.PLAYERS.table, new String[]{"UUID varchar(256)", "ISLAND varchar(256)", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.ISLANDS.table, new String[]{"UUID varchar(256)", "META JSON", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.LEVEL.table, new String[]{"UUID varchar(256)", "LEVEL INT"});
        this.playerManager = new PlayerManager(this);
        this.islandManager = new IslandManager(this);
        this.slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = this.slimePlugin.getLoader("mysql");
        this.phaseManager = new PhaseManager(this);
        this.phaseManager.populatePhases();
        this.langManager = new MessagesManager(this);
        this.islandLevelManager = new IslandLevelManager(this);
        this.menuManager = new MenuManager(this);


        new BorderHandler(this);


        registerListeners();

        new IslandCommand(this, new String[]{"is"}, "island");
        new GlobalCommand(this, new String[]{"global"}, "global");
        new AdminCommand(this, new String[]{"isa"}, "admin");
        OneBlock.REDIS_ENABLED = getConfig().getBoolean("redis.enabled");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        if (!OneBlock.REDIS_ENABLED) {
            getServer().getMessenger().registerIncomingPluginChannel(this, "oneblock:invite", new ChannelInviteListener(this));
            getServer().getMessenger().registerOutgoingPluginChannel(this, "oneblock:invite");
            getServer().getMessenger().registerOutgoingPluginChannel(this, "oneblock:chat");
            getServer().getMessenger().registerOutgoingPluginChannel(this, "oneblock:teleport");
            getServer().getMessenger().registerIncomingPluginChannel(this, "oneblock:chat", new ChannelChatListenerPluginMessage(this));
        } else {
            LOG_REDIS = getConfig().getBoolean("redis.log");
            redisManager = new RedisManager(this, getConfig().getString("redis.hostname"), getConfig().getInt("redis.port"), getConfig().getString("redis.password"));
            redisManager.registerListener(new String[]{"oneblock:invite:request", "oneblock:invite:response", "oneblock:global", "oneblock:pm"}, new ChannelInviteListenerRedis(this));
        }

        registerAddons();


    }

    @SneakyThrows
    public void onDisable() {
        islandManager.getIslands().forEach((s, island) -> {
            island.save(true);
        });
        playerManager.getPlayers().forEach((uuid, playerOB) -> {
            playerOB.save();
        });


        if (REDIS_ENABLED)
            redisManager.onDisable();
        getLogger().log(Level.INFO, "Waiting max 15 seconds to terminate all tasks from pool.");
        generalPool.awaitTermination(5, TimeUnit.SECONDS);
        dbManager.close();


    }

    public static OneBlock getInstance() {
        return instance;
    }

    public MySQLManager getDbManager() {
        return this.dbManager;
    }

    public ro.deiutzblaxo.cloud.data.mysql.MySQLConnection getDbConnection() {
        return this.dbConnection;
    }

    public SlimePlugin getSlimePlugin() {
        return this.slimePlugin;
    }

    public SlimeLoader getLoader() {
        return this.loader;
    }

    public IslandManager getIslandManager() {
        return this.islandManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    public MessagesManager getLangManager() {
        return this.langManager;
    }

    public Location getSpawnLocation() {
        return getServer().getWorld(getConfig().getString("spawn-world")).getSpawnLocation();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BreakEventListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        //getServer().getPluginManager().registerEvents(new PhaseLockListener(this), this);TODO MAYBE ERROR ON PLACEING WOOL
        getServer().getPluginManager().registerEvents(new BorderItemListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChangeWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new FireSpreadListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBurnListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockIgniteListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new DecayListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        getServer().getPluginManager().registerEvents(new MobSpawningListener(this), this);
        getServer().getPluginManager().registerEvents(new PVEListener(this), this);
        getServer().getPluginManager().registerEvents(new PVPListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
    }

    private void registerAddons() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            new PAPIAddon(this).register();
            getLogger().log(Level.INFO, ChatColor.GREEN + "PlaceHolderAPI have been hooked!");
        }
    }

}
