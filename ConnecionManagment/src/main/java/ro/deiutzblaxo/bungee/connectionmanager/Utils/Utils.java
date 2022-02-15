package ro.deiutzblaxo.bungee.connectionmanager.Utils;

import net.md_5.bungee.api.config.ServerInfo;
import ro.deiutzblaxo.bungee.connectionmanager.Main;
import ro.nexs.db.manager.manager.DBManager;

public class Utils {


    public static ServerInfo getServerInfo(String server_name){
        return Main.getInstance().getProxy().getServerInfo(server_name);
    }


}
