package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.logging.Level;

public class PlayerQuitListener implements Listener {
    private OneBlock plugin;

    public PlayerQuitListener(OneBlock plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin ,() -> {
            PlayerOB playerOB = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            playerOB.save();

            if(plugin.getIslandManager().getIsland(playerOB.getIsland()) != null){
                try {
                    plugin.getIslandManager().unloadIsland(plugin.getIslandManager().getIsland(playerOB.getIsland()),true);
                } catch (IslandHasPlayersOnlineException e) {
                    e.printStackTrace();
                }
                plugin.getLogger().log(Level.INFO,"Island is not null");
            }else {
                plugin.getLogger().log(Level.WARNING,"island is null");
            }
            plugin.getPlayerManager().unloadPlayer(event.getPlayer().getUniqueId());


        },2);

    }
}
