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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
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
                if (invited == null) {
                    if (OneBlock.LOG_REDIS)
                        plugin.getLogger().log(Level.INFO, "[request] " + invited + " is null as PlayerOB");
                    return;
                }
                invited.getTimers().put(SCOPE_CONFIRMATION.INVITE, 10);
                ArrayList<Object> a = new ArrayList<>();
                a.add(0, requestInvite);
                invited.getParticipant().put(SCOPE_CONFIRMATION.INVITE, a);
                Bukkit.getPlayer(invited.getPlayer()).sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_RECEIVER).replace("{name}",
                        /*plugin.getDbManager().getLikeString(TableType.NAME.table, "UUID", requestInvite.inviter, "NAME")*/
                        plugin.getPlayerManager().getNameByUUID(UUID.fromString(requestInvite.inviter))));
                break;
            case "oneblock:invite:response":
                ResponseInvite responseInvite = new ResponseInvite(message);
                Player inviter = Bukkit.getPlayer(UUID.fromString(responseInvite.getInviter()));
                if (inviter == null) {
                    if (OneBlock.LOG_REDIS)
                        plugin.getLogger().log(Level.INFO, "[response] " + responseInvite.getInviter() + " is null as Player");
                    return;
                }
                PlayerOB inviterOB = plugin.getPlayerManager().getPlayer(inviter.getUniqueId());
                if (inviterOB == null) {
                    if (OneBlock.LOG_REDIS)
                        plugin.getLogger().log(Level.INFO, "[response] " + responseInvite.getInviter() + " is null as PlayerOB");
                    return;
                }

                switch (responseInvite.getError()) {
                    case ACCEPT:
                        Bukkit.getPluginManager().callEvent(new PlayerJoinIslandEvent(plugin, responseInvite.getInvited(),
                                inviterOB, inviterOB.getIsland(false), false, true));
                        inviter.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_ACCEPT).replace("{name}",
                                plugin.getNameUUIDManager().getNameByUUID(UUID.fromString(responseInvite.getInvited()))
                                /*plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())*/));
                        break;
                    case REJECT:
                        responseInvite.getInvited();
                        inviter.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_REJECT).replace("{name}",
                                /*plugin.getDbManager().getString(TableType.NAME.table, "NAME", "UUID", responseInvite.getInvited())*/
                                plugin.getNameUUIDManager().getNameByUUID(UUID.fromString(responseInvite.getInvited()))));
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
                Player player = plugin.getServer().getPlayer(UUID.fromString(pm.getReciver()));
                if (player == null)
                    return;
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

