package ro.deiutzblaxo.oneblock.communication.action.invite;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.chat.pm.PM;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;
import ro.deiutzblaxo.oneblock.player.eventlisteners.ChatListener;
import ro.deiutzblaxo.oneblock.player.events.PlayerJoinIslandEvent;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChannelInviteListenerRedis extends JedisPubSub {
    private OneBlock plugin;

    public ChannelInviteListenerRedis(OneBlock plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public void onMessage(String channel, String message) {
        if (OneBlock.LOG_REDIS)
            System.out.println("[REDIS] Got a message on " + channel + " message: " + message);
        switch (channel) {
            case "oneblock:invite:request":
                RequestInvite requestInvite = new RequestInvite(message);
                PlayerOB invited = plugin.getPlayerManager().getPlayer(UUID.fromString(requestInvite.getInvited()));
                invited.getTimers().put(SCOPE_CONFIRMATION.INVITE, 10);
                ArrayList<Object> a = new ArrayList<>();
                a.add(0, requestInvite);
                invited.getParticipant().put(SCOPE_CONFIRMATION.INVITE, a);
                Bukkit.getPlayer(invited.getPlayer()).sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_RECEIVER).replace("{name}",
                        plugin.getDbManager().getLikeString(TableType.NAME.table, "UUID", requestInvite.inviter, "NAME")));
                break;
            case "oneblock:invite:response":
                ResponseInvite responseInvite = new ResponseInvite(message);
                Player inviter = Bukkit.getPlayer(UUID.fromString(responseInvite.getInviter()));
                PlayerOB inviterOB = plugin.getPlayerManager().getPlayer(inviter.getUniqueId());
                switch (responseInvite.getError()) {
                    case ACCEPT:
                        Bukkit.getPluginManager().callEvent(new PlayerJoinIslandEvent(plugin, responseInvite.getInvited(),
                                inviterOB, inviterOB.getIsland(false), false, true));
                        inviter.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_ACCEPT).replace("{name}",
                                plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())));
                        break;
                    case REJECT:
                        responseInvite.getInvited();
                        inviter.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_REJECT).replace("{name}",
                                plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())));
                        break;
                }
            case "oneblock:global":
                if (OneBlock.SERVER.equalsIgnoreCase(message.split(";")[0])) {
                    return;
                } else {
                    String str = Arrays.stream(message.split(";")).skip(1).collect(Collectors.joining(" "));
                    ChatListener.globalRecipients.forEach(player1 -> {
                        player1.sendMessage(str);
                    });
                }
                break;
            case "oneblock:pm":
                PM pm = new PM(message);
                UUID uuidplayer = UUID.fromString(pm.getReciver());
                System.out.println(uuidplayer);
                Player player = plugin.getServer().getPlayer(uuidplayer);
                System.out.println(player.getName());
                System.out.println(pm.getMessage());
                System.out.println(pm.getSender());
                player.sendMessage(pm.getMessage());

                break;

        }

    }
/*            if (channel.equalsIgnoreCase("oneblock:global")) {
        if (OneBlock.SERVER.equalsIgnoreCase(message.split(";")[0])) {
            return;
        } else {
            String str = Arrays.stream(message.split(";")).skip(1).collect(Collectors.joining(" "));
            ChatListener.globalRecipients.forEach(player1 -> {
                player1.sendMessage(str);
            });
        }
    } else if (channel.equalsIgnoreCase("oneblock:pm")) {
        PM pm = new PM(message);
        Player rec = Bukkit.getPlayer(UUID.fromString(pm.getReciver()));
        rec.sendMessage(ChatColor.translateAlternateColorCodes('&', pm.getMessage()));
    }*/

}

