package ro.deiutzblaxo.oneblock.player;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.communication.action.invite.InviteResponses;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.ResponseInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.*;
import java.util.logging.Level;

@Getter

public class PlayerOB {

    private OneBlock plugin;
    private UUID player;
    @Setter
    private String island;
    @Setter
    private String server;
    @Setter
    private boolean globalChat = false;

    private BukkitTask autosave;

    private HashMap<SCOPE_CONFIRMATION, Integer> timers = new HashMap<>();
    private HashMap<SCOPE_CONFIRMATION, List<Object>> participant = new HashMap<>();

    //if is local invite , [0] - PlayerOB object of inviter , [1] - Island object of inviter
    // if is multiserver , first object will be a RequestInvite object.


    public PlayerOB(OneBlock plugin, UUID player) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (SCOPE_CONFIRMATION scope : timers.keySet()) {
                if (timers.get(scope) <= 0) {
                    timers.remove(scope);
                    if (participant.containsKey(scope))
                        switch (scope) {
                            case INVITE:
                                if (participant.get(scope).get(0) instanceof RequestInvite) {
                                    RequestInvite invite = (RequestInvite) participant.get(scope).get(0);
                                    Invite.sendResponse(plugin, Bukkit.getPlayer(player), new ResponseInvite(invite.invited, invite.inviter, InviteResponses.REJECT));
                                    return;
                                }
                                PlayerOB inviter = (PlayerOB) participant.get(scope).get(0);
                                Bukkit.getPlayer(inviter.getPlayer()).sendMessage("Player didn`t respond to your invite (PlayerOB.42)");
                                Bukkit.getPlayer(player).sendMessage("The invitation expired!");
                                break;


                        }
                    participant.remove(scope);
                    return;
                }
                timers.put(scope, timers.get(scope) - 1);
            }

        }, 20, 20);

        this.player = player;
        this.plugin = plugin;
        this.server = OneBlock.SERVER;

        autosave = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getLogger().log(Level.INFO, "Auto-Saving player ", player.toString());
            save();
        }, 20 * 60 * 5, 20 * 60 * 5);

    }


    @SneakyThrows
    public void save() {
        plugin.getLogger().log(Level.INFO, "Saving player " + player.toString());
        if (player != null) {
            if (!plugin.getDbManager().existString(TableType.PLAYERS.table, "UUID", player.toString())) {
                plugin.getDbManager().insert(TableType.PLAYERS.table, new String[]{"UUID", "SERVER"}, new Object[]{player.toString(), server});

            } else {
                plugin.getDbManager().set(TableType.PLAYERS.table, "SERVER", "UUID", server, player.toString());
            }
            if (island != null) {
                plugin.getDbManager().set(TableType.PLAYERS.table, "ISLAND", "UUID", island.toString(), player.toString());
            } else {
                plugin.getDbManager().setNull(TableType.PLAYERS.table, "UUID", player.toString(), "ISLAND");
            }
        }
        plugin.getLogger().log(Level.INFO, "Player " + player.toString() + " saved!");
    }

    public Island getIsland(boolean forceLoad) {
        if (forceLoad) {
            plugin.getIslandManager().loadIsland(island);
        }
        return plugin.getIslandManager().getIsland(island);
    }

    public boolean hasIsland() {
        return island != null;
    }

    public boolean isOnIsland() {
        if (island == null)
            return false;
        if (Bukkit.getPlayer(player).getWorld().getName().equalsIgnoreCase(island)) {
            return true;
        }
        return false;

    }

    @Override
    public String toString() {
        return player.toString();
    }
}
