package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.Rank.RankEnum;
import ro.deiutzblaxo.oneblock.utils.UTILS;
import ro.deiutzblaxo.oneblock.utils.otherexceptions.NotFoundException;

public class PlayerJoinListener implements Listener {
    private final OneBlock plugin;

    public PlayerJoinListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PlayerOB playerOB = plugin.getPlayerManager().loadPlayer(event.getPlayer().getUniqueId());
            Island island = plugin.getIslandManager().getIsland(playerOB.getIsland());
            System.out.println("______" + playerOB.getPlayer().toString());
            if (island == null) {
                island = plugin.getIslandManager().loadIsland(playerOB.getIsland());
                IslandMeta meta = island.getMeta();
                System.out.println("_____" + meta.getName());
                if (meta == null) {
                    if (!meta.getMembers().containsKey(playerOB.getPlayer())) {
                        meta.getMembers().put(playerOB.getPlayer(), RankEnum.OWNER);
                        island.setMeta(meta);
                    }
                }


            }
            if (Bukkit.getPlayer(island.getOwner()) != null)
                island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
            playerOB.setIsland(island.getUuidIsland());
            island.changeBorder();


        }, 5);

    }
}
