package ro.deiutzblaxo.oneblock.communication.action.invite;

import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.tpa.TeleportRequest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Invite {


    public static void sendInvite(OneBlock plugin, Player sender, RequestInvite invite) {
        if (OneBlock.REDIS_ENABLED) {
            plugin.getRedisManager().send("oneblock:invite:request", invite.toString());
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF("request");
                out.writeUTF(invite.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendPluginMessage(plugin, "oneblock:invite", stream.toByteArray());
        }
    }

    public static void sendResponse(OneBlock plugin, Player sender, ResponseInvite responseInvite) {
        if (OneBlock.REDIS_ENABLED) {
            plugin.getRedisManager().send("oneblock:invite:response", responseInvite.toString());
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF("response");
                out.writeUTF(responseInvite.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendPluginMessage(plugin, "oneblock:invite", stream.toByteArray());

        }
    }


    public static void sendPlayerToServer(OneBlock plugin, Player sender, String server) {
        TeleportRequest.sendRequest(plugin, sender, server);
    }

}

