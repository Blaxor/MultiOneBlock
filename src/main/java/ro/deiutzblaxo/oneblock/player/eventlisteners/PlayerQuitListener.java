package ro.deiutzblaxo.oneblock.player.eventlisteners;

import me.stefan923.playerdatastorage.playerdata.PlayerData;
import me.stefan923.playerdatastorage.util.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.Collection;
import java.util.logging.Level;

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
            Player player = event.getPlayer();

            Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
            PlayerData playerData = new PlayerData(
                    player.getUniqueId(),
                    player.getInventory().getContents(),
                    player.getEnderChest().getContents(),
                    potionEffects.toArray(new PotionEffect[0]),
                    ExperienceUtil.getTotalExperience(player.getLevel(), player.getExp())
            );

            plugin.getPlayerSaveStorage().savePlayerData(playerData);
        }, 2);

    }
}
