package ro.deiutzblaxo.playersave.listeners;

import me.stefan923.playerdatastorage.playerdata.PlayerData;
import me.stefan923.playerdatastorage.util.ExperienceUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import ro.deiutzblaxo.playersave.PlayerSave;

import java.util.Arrays;
import java.util.Collection;

public class JoinQuitListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerSave.playerSaveStorage.getPlayerData(player.getUniqueId());
        if (playerData != null) {
            player.getInventory().setContents(playerData.getInventoryContent());
            /*player.getEnderChest().setContents(playerData.getEnderChestContent());*/
            player.addPotionEffects(Arrays.asList(playerData.getPotionEffects()));
            ExperienceUtil.setTotalExperience(player, playerData.getTotalExperience());
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
        PlayerData playerData = new PlayerData(
                player.getUniqueId(),
                player.getInventory().getContents(),
                /*       player.getEnderChest().getContents(),*/
                potionEffects.toArray(new PotionEffect[0]),
                ExperienceUtil.getTotalExperience(player.getLevel(), player.getExp())
        );

        PlayerSave.playerSaveStorage.savePlayerData(playerData);
    }
}
