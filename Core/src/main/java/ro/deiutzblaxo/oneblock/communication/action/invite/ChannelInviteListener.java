package ro.deiutzblaxo.oneblock.communication.action.invite;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;
import ro.deiutzblaxo.oneblock.player.events.PlayerJoinIslandEvent;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.ArrayList;
import java.util.UUID;

public class ChannelInviteListener implements PluginMessageListener {

    private final OneBlock plugin;

    public ChannelInviteListener(OneBlock plugin) {
        this.plugin = plugin;
    }


    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channel.
     *
     * @param channel Channel that the message was sent through.
     * @param player  Source of the message.
     * @param message The raw message that was sent.
     */
    @SneakyThrows
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("oneblock:invite")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subC = in.readUTF();
            if (subC.equalsIgnoreCase("request")) {
                RequestInvite requestInvite = new RequestInvite(in.readUTF());
                PlayerOB invited = plugin.getPlayerManager().getPlayer(UUID.fromString(requestInvite.getInvited()));
                invited.getTimers().put(SCOPE_CONFIRMATION.INVITE, 10);
                ArrayList<Object> a = new ArrayList<>();
                a.add(0, requestInvite);
                invited.getParticipant().put(SCOPE_CONFIRMATION.INVITE, a);
                Bukkit.getPlayer(invited.getPlayer()).sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_INVITE_RECEIVER).replace("{name}",
                        plugin.getDbManager().getLikeString(TableType.NAME.table, "UUID", requestInvite.inviter, "NAME")));

            } else if (subC.equalsIgnoreCase("response")) {
                ResponseInvite responseInvite = new ResponseInvite(in.readUTF());
                Player inviter = Bukkit.getPlayer(UUID.fromString(responseInvite.getInviter()));
                PlayerOB inviterOB = plugin.getPlayerManager().getPlayer(inviter.getUniqueId());
                switch (responseInvite.getError()) {
                    case ACCEPT:
                        Bukkit.getPluginManager().callEvent(new PlayerJoinIslandEvent(plugin, responseInvite.getInvited(),
                                inviterOB, inviterOB.getIsland(false), false, true));
                        inviter.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_INVITE_ACCEPT).replace("{name}",
                                plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())));
                        break;
                    case REJECT:
                        responseInvite.getInvited();
                        inviter.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_INVITE_REJECT).replace("{name}",
                                plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())));
                }

            }
        }
    }
}
