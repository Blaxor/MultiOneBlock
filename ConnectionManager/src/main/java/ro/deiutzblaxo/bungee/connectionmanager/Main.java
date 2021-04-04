package ro.deiutzblaxo.bungee.connectionmanager;

import net.md_5.bungee.api.plugin.Plugin;
import ro.deiutzblaxo.bungee.connectionmanager.ServerManager.ServerManager;
import ro.deiutzblaxo.bungee.connectionmanager.communication.PluginMessageListener;
import ro.nexs.db.manager.connection.DBConnection;
import ro.nexs.db.manager.manager.DBManager;

public final class Main extends Plugin {

    private static Main instance;
    private DBConnection dbConnection;
    private DBManager dbManager;
    private ServerManager manager;



    @Override
    public void onEnable() {
        instance = this;
        dbConnection = new DBConnection("localhost", 3306, "oneblock", "abc", "password");
        dbManager = new DBManager(dbConnection);
        manager = new ServerManager(this);
        getProxy().registerChannel("oneblock:invite");
        getProxy().registerChannel("oneblock:chat");
        getProxy().getPluginManager().registerListener(this, new EventListener());
        getProxy().getPluginManager().registerListener(this, new PluginMessageListener());



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance(){
        return instance;
    }
    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public DBManager getDbManager() {
        return dbManager;
    }
    public ServerManager getServerManager(){
        return manager;
    }
}
