package ro.deiutzblaxo.oneblock;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import lombok.Getter;
import me.stefan923.playerdatastorage.PlayerDataStorage;
import me.stefan923.playerdatastorage.mysql.MySQLConnection;
import me.stefan923.playerdatastorage.mysql.MySQLPlayerDataStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.oneblock.commands.chat.GlobalCommand;
import ro.deiutzblaxo.oneblock.commands.chat.PmCommand;
import ro.deiutzblaxo.oneblock.commands.island.IslandCommand;
import ro.deiutzblaxo.oneblock.communication.action.chat.ChannelChatListener;
import ro.deiutzblaxo.oneblock.communication.action.invite.ChannelInviteListener;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandManager;
import ro.deiutzblaxo.oneblock.island.level.IslandLevelManager;
import ro.deiutzblaxo.oneblock.island.protection.*;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.island.radius.BorderItemListener;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.menu.MenuManager;
import ro.deiutzblaxo.oneblock.menu.listener.InventoryClickListener;
import ro.deiutzblaxo.oneblock.phase.PhaseManager;
import ro.deiutzblaxo.oneblock.phase.phaselock.PhaseLockListener;
import ro.deiutzblaxo.oneblock.player.PlayerManager;
import ro.deiutzblaxo.oneblock.player.eventlisteners.*;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.database.DBManager;
import ro.deiutzblaxo.oneblock.utils.database.NameUUIDLocal;
import ro.nexs.db.manager.connection.DBConnection;

import java.util.logging.Level;

@Getter
public final class OneBlock extends JavaPlugin {
    private DBConnection dbConnection;
    private DBManager dbManager;
    private IslandManager islandManager;
    private PlayerManager playerManager;
    private SlimePlugin slimePlugin;
    private SlimeLoader loader;
    private PhaseManager phaseManager;
    private MessagesManager langManager;
    private IslandLevelManager islandLevelManager;
    private MenuManager menuManager;
    private NameUUIDLocal nameUUIDLocal;
    private MySQLConnection playerSaveConnection;
    private PlayerDataStorage playerSaveStorage;
    public static String SERVER;


    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        SERVER = getConfig().getString("server-name");
        this.dbConnection = new DBConnection(getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        this.dbManager = new DBManager(this.dbConnection);
        playerSaveConnection = new MySQLConnection("playerData_", getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        playerSaveStorage = new MySQLPlayerDataStorage(playerSaveConnection);
        playerSaveStorage.createTable();
        getDbManager().createTable(TableType.PLAYERS.table, new String[]{"UUID varchar(256)", "ISLAND varchar(256)", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.ISLANDS.table, new String[]{"UUID varchar(256)", "META JSON", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.NAME.table, new String[]{"NAME varchar(256)", "UUID varchar(256)"});
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
        this.nameUUIDLocal = new NameUUIDLocal(this);

        new EnchantsManager(this);
        new BorderHandler(this);


        registerListeners();

        new IslandCommand(this, new String[]{"is"}, "oneblock.island");
        new PmCommand(this, new String[]{"message"}, "oneblock");
        new GlobalCommand(this, new String[]{"global"}, "oneblock");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "oneblock:invite", new ChannelInviteListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "oneblock:invite");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "oneblock:chat");
        getServer().getMessenger().registerIncomingPluginChannel(this, "oneblock:chat", new ChannelChatListener(this));

        getCommand("test").setExecutor((sender, command, label, args) -> {
            Player player = (Player) sender;
            /*player.getInventory().addItem(PhaseLock.getPhaseLockItem(this));
            Island island = this.playerManager.getPlayer(((Player) sender).getUniqueId()).getIsland(false);
            //sender.sendMessage(BorderHandler.radius.toString());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(playerManager.getPlayers().toString());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(islandManager.getIslands().toString());
            try {
                player.getInventory().addItem(BorderHandler.getItemByTier(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire() + 1));
            } catch (Exception e) {
                player.getInventory().addItem(BorderHandler.getItemByTier(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire()));
            }*/
            Island island = playerManager.getPlayer(player.getUniqueId()).getIsland(false);
            player.getInventory().addItem(BorderHandler.getItemByTier(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire()));

            return false;
        });
    }

    public void onDisable() {
        getLogger().log(Level.INFO, "Unloading players");
        this.playerManager.getPlayers().values().forEach(playerOB -> playerOB.save());
        this.islandManager.getIslands().values().forEach(island -> island.save(true));
        getLogger().log(Level.INFO, "Players unloaded");
    }

    public DBManager getDbManager() {
        return this.dbManager;
    }

    public DBConnection getDbConnection() {
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
        getServer().getPluginManager().registerEvents(new PhaseLockListener(this), this);
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

}
