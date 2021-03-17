package ro.deiutzblaxo.bungee.connectionmanager.ServerManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ro.deiutzblaxo.bungee.connectionmanager.Main;
import ro.deiutzblaxo.bungee.connectionmanager.Utils.TableTypes;

import java.util.*;

public class ServerManager {

    public List<ServerInfo> onlineServers = new ArrayList<>();
    private final Main plugin;

    public ServerManager(Main main) {
        plugin = main;
    }

    public ServerInfo getServerBestFit(ProxiedPlayer player) {

        String uuid = player.getUniqueId().toString();
        if (plugin.getDbManager().existString(TableTypes.PLAYERS.table, "UUID", uuid)) {
            try {
                String uuidisland = plugin.getDbManager().getString(TableTypes.PLAYERS.table, "WORLD", "UUID", uuid);
                String server = plugin.getDbManager().getString(TableTypes.ISLANDS.table, "SERVER", "UUID", uuidisland);
                if (!server.equalsIgnoreCase("nothing")) {
                    ServerInfo[] info = (ServerInfo[]) onlineServers.stream().filter(serverInfo -> serverInfo.getName().equalsIgnoreCase(server)).toArray();
                    if (info.length != 0) {
                        return info[0];
                    }
                }
            } catch (Exception ignored) {}
        }
        onlineServers.sort(Collections.reverseOrder(Comparator.comparingInt(o -> o.getPlayers().size())));

        return onlineServers.size() == 0 ? player.getServer().getInfo(): onlineServers.get(onlineServers.size()-1);
    }

    public void checkServers() {
        ProxyServer proxy = plugin.getProxy();
        Map<String, ServerInfo> allServers = proxy.getServers();
        allServers.values().forEach(server -> server.ping((result, error) -> {
            if (error == null)
                onlineServers.add(server);
            else onlineServers.remove(server);

        }));
    }


}
