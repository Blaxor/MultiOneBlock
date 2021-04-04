package ro.deiutzblaxo.oneblock;


import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.oneblock.commands.chat.GlobalCommand;
import ro.deiutzblaxo.oneblock.commands.chat.PmCommand;
import ro.deiutzblaxo.oneblock.communication.action.invite.ChannelInviteListener;
import ro.deiutzblaxo.oneblock.communication.action.chat.ChannelChatListener;
import ro.deiutzblaxo.oneblock.customenchants.EnchantsManager;
import ro.deiutzblaxo.oneblock.island.IslandManager;
import ro.deiutzblaxo.oneblock.commands.island.IslandCommand;
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

    @Override
    public void onEnable() {

        /*@Deiu. https://github.com/Stefan923/PlayerDataStorage
        Gata, ai in readme tot ce trebuie (mai jos, la API)*/
        // load config
        saveDefaultConfig();
        reloadConfig();
        SERVER = getConfig().getString("server-name");


        dbConnection = new DBConnection(getConfig().getString("hostname"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        dbManager = new DBManager(dbConnection);
        getDbManager().createTable(TableType.PLAYERS.table, new String[]{"UUID varchar(256)", "ISLAND varchar(256)", "SERVER varchar(256)"});
        getDbManager().createTable(TableType.ISLANDS.table, new String[]{"UUID varchar(256)", "META BLOB", "SERVER varchar(256)"});

        getDbManager().createTable(TableType.NAME.table, new String[]{"NAME varchar(256)", "UUID varchar(256)"});

        //MANAGERS
        playerManager = new PlayerManager(this);
        islandManager = new IslandManager(this);
        slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");
        loader = slimePlugin.getLoader("mysql");
        phaseManager = new PhaseManager(this);
        phaseManager.populatePhases();
        langManager = new MessagesManager(this);
        new EnchantsManager(this);

        //LISTENERS

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PhaseLockListener(this), this);

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
            sender.sendMessage(playerManager.getPlayers().toString());
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(" ");
            sender.sendMessage(islandManager.getIslands().toString());
            return false;
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "Unloading players");
        playerManager.getPlayers().values().forEach(playerOB -> playerOB.save());
        islandManager.getIslands().values().forEach(island -> island.save(true));
        getLogger().log(Level.INFO, "Players unloaded");

    }


    public DBManager getDbManager() {
        return dbManager;
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public SlimeLoader getLoader() {
        return loader;
    }


    public IslandManager getIslandManager() {
        return islandManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public MessagesManager getLangManager() {
        return langManager;
    }

}
