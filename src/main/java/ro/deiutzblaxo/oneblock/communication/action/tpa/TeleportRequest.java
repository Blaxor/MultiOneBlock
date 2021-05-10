package ro.deiutzblaxo.oneblock.communication.action.tpa;

import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TeleportRequest {


    public static void sendRequest(OneBlock plugin, Player player, String server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            /*out.writeUTF(player.getUniqueId().toString());*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", stream.toByteArray());


    }
}
