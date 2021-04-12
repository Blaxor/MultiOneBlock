package ro.deiutzblaxo.oneblock;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
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
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.island.radius.BorderItemListener;
import ro.deiutzblaxo.oneblock.langs.MessagesManager;
import ro.deiutzblaxo.oneblock.phase.BreakEvent;
import ro.deiutzblaxo.oneblock.phase.PhaseManager;
import ro.deiutzblaxo.oneblock.phase.phaselock.PhaseLock;
import ro.deiutzblaxo.oneblock.phase.phaselock.PhaseLockListener;
import ro.deiutzblaxo.oneblock.player.PlayerManager;
import ro.deiutzblaxo.oneblock.player.eventlisteners.ChatListener;
import ro.deiutzblaxo.oneblock.player.eventlisteners.PlayerJoinListener;
import ro.deiutzblaxo.oneblock.player.eventlisteners.PlayerQuitListener;
import ro.deiutzblaxo.oneblock.utils.DBManager;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.nexs.db.manager.connection.DBConnection;

import java.util.logging.Level;

public final class OneBlock extends JavaPlugin {
    private DBConnection dbConnection;

    private DBManager dbManager;

    private IslandManager islandManager;

    private PlayerManager playerManager;

    private SlimePlugin slimePlugin;

    private SlimeLoader loader;

    public static String SERVER;

    private PhaseManager phaseManager;

    private MessagesManager langManager;

    private IslandLevelManager islandLevelManager;

    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        SERVER = getConfig().getString("server-name");
        this.dbConnection = new DBConnection(getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        this.dbManager = new DBManager(this.dbConnection);
        getDbManager().createTable(TableType.PLAYERS.table, new String[]{"UUID varchar(256)", "ISLAND varchar(256)", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.ISLANDS.table, new String[]{"UUID varchar(256)", "META BLOB", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.NAME.table, new String[]{"NAME varchar(256)", "UUID varchar(256)"});
        this.playerManager = new PlayerManager(this);
        this.islandManager = new IslandManager(this);
        this.slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = this.slimePlugin.getLoader("mysql");
        this.phaseManager = new PhaseManager(this);
        this.phaseManager.populatePhases();
        this.langManager = new MessagesManager(this);
        this.islandLevelManager = new IslandLevelManager(this);

        new EnchantsManager(this);
        new BorderHandler(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PhaseLockListener(this), this);
        getServer().getPluginManager().registerEvents(new BorderItemListener(this), this);

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
            player.getInventory().addItem(PhaseLock.getPhaseLockItem(this));
            Island island = this.playerManager.getPlayer(((Player) sender).getUniqueId()).getIsland(false);
            sender.sendMessage(BorderHandler.radius.toString());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(island.getMeta().getRadiusTire() + " " + island.getMeta().getRadiusType());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(BorderHandler.permission.toString());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage("Calculating island level");
            islandLevelManager.addInQueue(island);
            try {
                player.getInventory().addItem(BorderHandler.getItemByTier(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire() + 1));
            } catch (Exception e) {
                player.getInventory().addItem(BorderHandler.getItemByTier(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire()));
            }
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

    public IslandLevelManager getIslandLevelManager() {
        return this.islandLevelManager;
    }
}
