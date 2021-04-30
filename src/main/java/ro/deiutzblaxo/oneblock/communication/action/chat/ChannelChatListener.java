package ro.deiutzblaxo.oneblock.communication.action.chat;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.chat.pm.PM;
import ro.deiutzblaxo.oneblock.player.eventlisteners.ChatListener;

import java.util.UUID;

public class ChannelChatListener implements PluginMessageListener {
    private final OneBlock plugin;

    public ChannelChatListener(OneBlock plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("oneblock:chat")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subC = in.readUTF();
            switch (subC) {
                case "pm":
                    PM pm = new PM(in.readUTF());
                    Player rec = Bukkit.getPlayer(UUID.fromString(pm.getReciver()));
                    rec.sendMessage(ChatColor.translateAlternateColorCodes('&', pm.getMessage()));
                    break;

                case "global":
                    String server = in.readUTF();
                    if (server == OneBlock.SERVER)
                        return;
                    String msg = in.readUTF();
                    ChatListener.globalRecipients.forEach(player1 -> {
                        player1.sendMessage(msg);
                    });
                    break;


            }
        }
    }
}
