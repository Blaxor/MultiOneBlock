package ro.deiutzblaxo.bungee.connectionmanager.communication;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ro.deiutzblaxo.bungee.connectionmanager.Main;
import ro.deiutzblaxo.bungee.connectionmanager.ServerManager.ServerManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onMessageRecived(PluginMessageEvent event) {
        byte[] backup = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subC;
        String data = "";
        switch (event.getTag()) {
            case "oneblock:invite":
                subC = in.readUTF();
                if (subC.equalsIgnoreCase("request")) {
                    data = in.readUTF();
                    Main.getInstance().getProxy().getPlayer(UUID.fromString(data.split(";")[0])).getServer().sendData("oneblock:invite", backup);
                } else if (subC.equalsIgnoreCase("response")) {
                    data = in.readUTF();
                    Main.getInstance().getProxy().getPlayer(UUID.fromString(data.split(";")[1])).getServer().sendData("oneblock:invite", backup);
                } else {
                    Main.getInstance().getProxy().getLogger().log(Level.INFO, "SubChannel " + subC + " invalid in channel oneblock:invite!");
                }
                break;
            case "oneblock:chat":
                subC = in.readUTF();
                if (subC.equalsIgnoreCase("pm")) {
                    data = in.readUTF();
                    Main.getInstance().getProxy().getPlayer(UUID.fromString(data.split(";")[1])).getServer().sendData("oneblock:chat", backup);
                } else if (subC.equalsIgnoreCase("global")) {
                    String oldServer = in.readUTF();
                    Main.getInstance().getServerManager().onlineServers.forEach(serverInfo -> {
                        System.out.println(!serverInfo.getName().equalsIgnoreCase(oldServer));
                        if (!serverInfo.getName().equalsIgnoreCase(oldServer))
                            serverInfo.sendData("oneblock:chat", backup);

                    });


                } else {
                    Main.getInstance().getProxy().getLogger().log(Level.INFO, "SubChannel " + subC + " invalid in channel oneblock:chat!");
                }
                break;
        }
    }
}
