package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.chat.global.ChatGlobalSender;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatListener implements Listener {
    private OneBlock plugin ;
    public ChatListener(OneBlock plugin){
        this.plugin= plugin;

    }
    public static Set<Player> globalRecipients = new HashSet<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){

        Player player = event.getPlayer();
        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        String message = event.getMessage();
        if(playerOB.isGlobalChat()){
            event.setCancelled(true);
            String msg = plugin.getLangManager().get(MESSAGE.CHAT_GLOBAL_PREFIX).replace("{name}",player.getDisplayName())+message;
            globalRecipients.forEach(player1 -> {
                player1.sendMessage(plugin.getLangManager().get(MESSAGE.CHAT_GLOBAL_PREFIX).replace("{name}",player.getDisplayName())+message);
            });
            ChatGlobalSender.sendMessage(plugin,player,msg);
        }else{
            event.setFormat(plugin.getLangManager().get(MESSAGE.CHAT_SERVER_PREFIX).replace("{name}",player.getDisplayName())+message);
        }

    }
/*    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        String message = e.getMessage();
        Stream<? extends Player> mentioned = Bukkit.getOnlinePlayers().stream().filter(player -> message.contains(player.getName()));
        Set<? extends Player> matches = mentioned.collect(Collectors.toSet());
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (matches.contains(player)) {
                player.sendMessage(e.getMessage().replaceAll(player.getName(), "&e@" + player.getName()));
            }
            else player.sendMessage(message);
        });
    }*/
}
