package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.psi.PlayerRepo;

import java.util.logging.Level;

//TOOD SAVE INV

public class PlayerQuitListener implements Listener {
    private final OneBlock plugin;

    public PlayerQuitListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PlayerOB playerOB = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            playerOB.save();
            plugin.getPSIRepo().savePlayer(event.getPlayer());

            if (plugin.getIslandManager().getIsland(playerOB.getIsland()) != null) {
                try {
                    plugin.getIslandManager().unloadIsland(plugin.getIslandManager().getIsland(playerOB.getIsland()), true);
                } catch (IslandHasPlayersOnlineException e) {
                    e.printStackTrace();
                }
                plugin.getLogger().log(Level.INFO, "Island is not null");
            } else {
                plugin.getLogger().log(Level.WARNING, "island is null");
            }
            plugin.getPlayerManager().unloadPlayer(event.getPlayer().getUniqueId());
        }, 2);

    }
}
