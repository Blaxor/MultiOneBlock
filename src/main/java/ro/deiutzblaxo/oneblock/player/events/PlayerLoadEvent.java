package ro.deiutzblaxo.oneblock.player.events;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.utils.TableType;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.util.UUID;

public class PlayerLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private PlayerOB player;


    @SneakyThrows
    public PlayerLoadEvent(OneBlock plugin, UUID player_) {
        player = new PlayerOB(plugin, player_);
        try {
            player.setIsland(plugin.getDbManager().getString(TableType.PLAYERS.table, "ISLAND", "UUID", player_.toString()));
        } catch (NoDataFoundException e) {

        }
        player.setServer(OneBlock.SERVER);
        player.save();
        plugin.getPlayerManager().getPlayers().put(player_, player);
        if (!plugin.getDbManager().existString(TableType.NAME.table, "UUID", player_.toString()))
            plugin.getDbManager().insert(TableType.NAME.table, new String[]{"UUID", "NAME"}, new Object[]{player_.toString(), Bukkit.getPlayer(player_).getName()});

    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
