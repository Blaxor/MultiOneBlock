package ro.deiutzblaxo.oneblock.communication.action.chat.pm;

import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PMSender {

    public static void sendMessage(OneBlock plugin, Player sender, PM pm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("pm");
            out.writeUTF(pm.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendPluginMessage(plugin, "oneblock:chat", stream.toByteArray());


    }
}
