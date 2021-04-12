package ro.deiutzblaxo.oneblock.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.events.PlayerLoadEvent;
import ro.deiutzblaxo.oneblock.player.events.PlayerUnLoadEvent;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerOfflineException;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager {

    private OneBlock plugin;

    @Getter
    private HashMap<UUID, PlayerOB> players = new HashMap<UUID, PlayerOB>();

    public PlayerManager(OneBlock plugin) {
        this.plugin = plugin;
    }

    public PlayerOB getPlayer(UUID player) {
        return players.get(player);
    }

    public PlayerOB loadPlayer(UUID player) {
        if (players.containsKey(player)) {
            return players.get(player);
        }
        PlayerLoadEvent event = new PlayerLoadEvent(plugin, player);
        Bukkit.getPluginManager().callEvent(event);
        return event.getPlayer();
    }

    public String getUUIDByName(String name) throws PlayerNoExistException {
        try {
            return plugin.getDbManager().getLikeString(TableType.NAME.table, "NAME", name, "UUID");
        } catch (NoDataFoundException e) {
            throw new PlayerNoExistException("Player " + name + " don`t exist in database!");
        }
    }

    public String getServerByPlayerUUID(String uuid) {
        try {
            return plugin.getDbManager().getString(TableType.PLAYERS.table, "SERVER", "UUID", uuid);
        } catch (NoDataFoundException e) {
            throw new PlayerOfflineException("Player is offline");
        }
    }

    public void unloadPlayer(UUID player) {
        if (!players.containsKey(player)) {
            plugin.getLogger().log(Level.WARNING, "The player " + player + " is not loaded so can`t be unloaded");
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerUnLoadEvent(plugin, player));
    }

}
