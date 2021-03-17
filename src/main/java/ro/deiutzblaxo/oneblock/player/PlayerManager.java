package ro.deiutzblaxo.oneblock.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.events.PlayerLoadEvent;
import ro.deiutzblaxo.oneblock.player.events.PlayerUnLoadEvent;


import java.util.HashMap;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager {

    private OneBlock plugin;

    @Getter
    private  HashMap<UUID, PlayerOB> players = new HashMap<UUID, PlayerOB>();

    public PlayerManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    public PlayerOB getPlayer(UUID player) {
        if(!players.containsKey(player))
            return new PlayerOB(player);

        return players.get(player);
    }

    public PlayerOB loadPlayer(UUID player) {
        if(players.containsKey(player)){
            return players.get(player);
        }
        PlayerLoadEvent event = new PlayerLoadEvent(plugin,player);
        Bukkit.getPluginManager().callEvent(event);
        return event.getPlayer();
    }



    public void unloadPlayer(UUID player) {
        if (!players.containsKey(player)) {
            plugin.getLogger().log(Level.WARNING, "The player " + player + " is not loaded so can`t be unloaded");
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerUnLoadEvent(plugin,player));
    }

}
