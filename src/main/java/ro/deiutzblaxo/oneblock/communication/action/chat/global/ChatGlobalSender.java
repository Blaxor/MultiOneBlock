package ro.deiutzblaxo.oneblock.communication.action.chat.global;

import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatGlobalSender {

    public static void sendMessage(OneBlock plugin, Player sender, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {

            out.writeUTF("global");
            out.writeUTF(OneBlock.SERVER);
            out.writeUTF(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendPluginMessage(plugin, "oneblock:chat", stream.toByteArray());
    }

}
