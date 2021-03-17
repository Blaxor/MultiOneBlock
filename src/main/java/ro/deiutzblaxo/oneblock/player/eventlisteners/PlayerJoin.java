package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(OneBlock.getInstance(), new Runnable() {
            @Override
            public void run() {
                OneBlock.getInstance().getPlayerManager().loadPlayer(event.getPlayer().getUniqueId());
                PlayerOB player = OneBlock.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());

                event.getPlayer().teleport(player.getOverworld(false).getSpawnLocation());

            }
        }, 5);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        PlayerOB player = OneBlock.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(OneBlock.getInstance(), new Runnable() {
            @Override
            public void run() {
                OneBlock.getInstance().getPlayerManager().unloadPlayer(player.getPlayer());

            }
        }, 2);
    }

}
