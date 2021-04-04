package ro.deiutzblaxo.bungee.connectionmanager;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class EventListener implements Listener {

    @EventHandler
    public void onPostJoin(PostLoginEvent e) {
        Main.getInstance().getServerManager().checkServers();

        ServerInfo server = Main.getInstance().getServerManager().getServerBestFit(e.getPlayer());
        if (e.getPlayer().getServer() != null)
            if (!e.getPlayer().getServer().getInfo().equals(server))
                e.getPlayer().connect(server);
        Main.getInstance().getProxy().broadcast(new TextComponent("Player send to " + server.getName()));


    }
}
