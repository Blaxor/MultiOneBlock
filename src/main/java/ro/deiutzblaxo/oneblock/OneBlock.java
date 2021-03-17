package ro.deiutzblaxo.oneblock;


import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandManager;
import ro.deiutzblaxo.oneblock.island.IslandType;
import ro.deiutzblaxo.oneblock.commands.island.IslandCommand;
import ro.deiutzblaxo.oneblock.phase.BreakEvent;
import ro.deiutzblaxo.oneblock.phase.PhaseManager;
import ro.deiutzblaxo.oneblock.player.PlayerManager;
import ro.deiutzblaxo.oneblock.player.eventlisteners.PlayerJoin;
import ro.deiutzblaxo.oneblock.utils.DBManager;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.deiutzblaxo.oneblock.utils.UTILS;
import ro.nexs.db.manager.connection.DBConnection;

public final class OneBlock extends JavaPlugin {
    private DBConnection dbConnection;
    private DBManager dbManager;
    private IslandManager islandManager;
    private PlayerManager playerManager;
    private SlimePlugin slimePlugin;
    private SlimeLoader loader;
    private static OneBlock instance;
    private PhaseManager phaseManager;

    @Override
    public void onEnable() {

        /*@Deiu. https://github.com/Stefan923/PlayerDataStorage
        Gata, ai in readme tot ce trebuie (mai jos, la API)*/
        // load config
        saveDefaultConfig();
        reloadConfig();
        getConfig().set("chest.str.nr",UTILS.deserialize(new ItemStack(Material.GRASS_BLOCK)));
        saveConfig();

        instance = this;
        dbConnection = new DBConnection("localhost", 3306, "oneblock", "abc", "password");
        dbManager = new DBManager(dbConnection);
        getDbManager().createTable(TableType.PLAYERS.table, new String[]{"UUID varchar(256)", IslandType.WORLD.name() + " varchar(256)", IslandType.NETHER.name() + " varchar(256)", IslandType.END.name() + " varchar(256)"});
        getDbManager().createTable(TableType.ISLANDS.table, new String[]{"UUID varchar(256)", "MEMBERS varchar(256)", "TYPE varchar(256)", "SPAWNX double", "SPAWNY double", "SPAWNZ double", "COUNT INT", "SERVER varchar(256)","PHASE_LOCK boolean"});


        //MANAGERS
        playerManager = new PlayerManager(this);
        islandManager = new IslandManager(this);
        slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");
        loader = slimePlugin.getLoader("mysql");
        phaseManager = new PhaseManager();
        phaseManager.populatePhases();

        //LISTENERS

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(this), this);

        new IslandCommand(this, new String[]{""}, "island");
        getCommand("test").setExecutor((sender, command, label, args) -> {
            Player player = (Player) sender;
            Island island = getPlayerManager().getPlayer(player.getUniqueId()).getNether(true);
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {

                    player.teleport(island.getSpawnLocation());
                }
            }, 30);


            return false;
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public SlimeLoader getLoader() {
        return loader;
    }

    public static OneBlock getInstance() {
        return instance;
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
}
