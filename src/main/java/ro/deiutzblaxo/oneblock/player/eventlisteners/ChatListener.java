package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.chat.global.ChatGlobalSender;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {
    private final OneBlock plugin;

    public ChatListener(OneBlock plugin) {
        this.plugin = plugin;

    }

    public static Set<Player> globalRecipients = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        String message = event.getMessage();
        if (playerOB.isGlobalChat()) {

            String msg = plugin.getLangManager().get(player,MESSAGE.CHAT_GLOBAL_PREFIX).replace("{name}", player.getDisplayName()) + message;
            event.setCancelled(true);
            globalRecipients.forEach(player1 -> {
                player1.sendMessage(plugin.getLangManager().get(player,MESSAGE.CHAT_GLOBAL_PREFIX).replace("{name}", player.getDisplayName()) + message);
            });
            ChatGlobalSender.sendMessage(plugin, player, msg);

        } else {
            event.setFormat(plugin.getLangManager().get(player,MESSAGE.CHAT_SERVER_PREFIX).replace("{name}", player.getDisplayName()) + message);
        }


    }
}
