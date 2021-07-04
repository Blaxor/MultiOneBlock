package ro.deiutzblaxo.oneblock.player;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.communication.action.Responses;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.ResponseInvite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Getter

public class PlayerOB {

    private final OneBlock plugin;
    private final UUID player;
    @Setter
    private String island;
    @Setter
    private String server;
    @Setter
    private boolean globalChat = false;

    private final BukkitTask autosave;

    private final HashMap<SCOPE_CONFIRMATION, Integer> timers = new HashMap<>();
    private final HashMap<SCOPE_CONFIRMATION, List<Object>> participant = new HashMap<>();

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
                                    Invite.sendResponse(plugin, Bukkit.getPlayer(player), new ResponseInvite(invite.invited, invite.inviter, Responses.REJECT));
                                    return;
                                }
                                PlayerOB inviter = (PlayerOB) participant.get(scope).get(0);
                                try {
                                    Bukkit.getPlayer(player).sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_REJECTED).replace("{name}", plugin.getPlayerManager().getNameByUUID(player)));
                                } catch (PlayerNoExistException e) {
                                    e.printStackTrace();
                                }
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
            if (!plugin.getDbManager().exists(TableType.PLAYERS.table, "UUID", player.toString())) {
                plugin.getDbManager().insert(TableType.PLAYERS.table, new String[]{"UUID", "SERVER"}, new Object[]{player.toString(), server});
            } else {
                plugin.getDbManager().update(TableType.PLAYERS.table, "UUID", player.toString(), new String[]{"SERVER"}, new String[]{server});
            }
            if (island != null) {
                plugin.getDbManager().update(TableType.PLAYERS.table, "UUID", player.toString(), new String[]{"ISLAND"}, new String[]{island});
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
        return Bukkit.getPlayer(player).getWorld().getName().equalsIgnoreCase(island);

    }

    synchronized public void sendMessage(String message) {
        Bukkit.getPlayer(player).sendMessage(message);
    }

    @Override
    public String toString() {
        return player.toString();
    }
}
