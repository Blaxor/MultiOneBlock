package ro.deiutzblaxo.oneblock.player.eventlisteners;

import me.stefan923.playerdatastorage.playerdata.PlayerData;
import me.stefan923.playerdatastorage.util.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.util.Arrays;

public class PlayerJoinListener implements Listener {
    private OneBlock plugin;

    public PlayerJoinListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PlayerOB playerOB = plugin.getPlayerManager().loadPlayer(event.getPlayer().getUniqueId());
            Island island = plugin.getIslandManager().getIsland(playerOB.getIsland());
            if (island == null) {
                island = plugin.getIslandManager().loadIsland(playerOB.getIsland() == null ? "WORLD_" + playerOB.getPlayer() : playerOB.getIsland());
                IslandMeta meta = island.getMeta();
                if (!meta.getMembers().containsKey(playerOB.getPlayer())) {
                    meta.getMembers().put(playerOB.getPlayer(), RANK.OWNER);
                    island.setMeta(meta);
                }

            }
            if (Bukkit.getPlayer(island.getOwner()) != null)
                island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
            playerOB.setIsland(island.getUuidIsland());
            island.changeBorder();

            Player player = event.getPlayer();
            PlayerData playerData = plugin.getPlayerSaveStorage().getPlayerData(player.getUniqueId());
            if (playerData != null) {
                player.getInventory().setContents(playerData.getInventoryContent());
                player.getEnderChest().setContents(playerData.getEnderChestContent());
                player.addPotionEffects(Arrays.asList(playerData.getPotionEffects()));
                ExperienceUtil.setTotalExperience(player, playerData.getTotalExperience());
            }

        }, 5);

    }
}
