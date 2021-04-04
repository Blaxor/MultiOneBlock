package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.invite.InviteResponses;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.ResponseInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandHasPlayersOnlineException;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.HashMap;
import java.util.List;

public class IslandTeamConfirm implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTeamConfirm(OneBlock plugin, String aliases[], String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;


        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        PlayerOB invited = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (invited.getTimers().containsKey(SCOPE_CONFIRMATION.INVITE)) {

            if (invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0) instanceof RequestInvite) {


                RequestInvite requestInvite = (RequestInvite) invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0);
                String oldIsland = invited.getIsland();
                if (plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().get(invited.getPlayer()).equals(RANK.OWNER) && plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().size() > 1) {

                    sender.sendMessage("You can`t accept the invite because there are other members on island . you need to remove then or change the ownership");//TODO MESSAGE
                    return;
                }
                plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().remove(invited);
                invited.setIsland(requestInvite.islandInviter);
                invited.save();
                // send reponse
                Invite.sendResponse(plugin, player, new ResponseInvite(requestInvite.invited, requestInvite.inviter, InviteResponses.ACCEPT));
                //TODO TELEPORT TO THE ISLAND SERVER
                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    Invite.sendPlayerToServer(plugin, player, plugin.getIslandManager().getServer(requestInvite.islandInviter));

                    if (plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().size() == 1) {
                        try {
                            plugin.getIslandManager().unloadIsland(plugin.getIslandManager().getIsland(oldIsland), false);
                        } catch (IslandHasPlayersOnlineException e) {
                            e.printStackTrace();
                        }
                        try {
                            plugin.getIslandManager().deleteIsland(oldIsland);
                        } catch (IslandLoadedException e) {
                            e.printStackTrace();
                        }

                    }

                }, 1);
                return;
            }


            player.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_ACCEPT));
            List<Object> data = invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE);
            PlayerOB inviter = (PlayerOB) data.get(0);

            Bukkit.getPlayer(inviter.getPlayer()).sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_INVITE_ACCEPT).replace("{name}",
                    Bukkit.getPlayer(inviter.getPlayer()).getDisplayName()));

            Island inviterIsland = (Island) data.get(1);

            invited.getTimers().remove(SCOPE_CONFIRMATION.INVITE);
            invited.getParticipant().remove(SCOPE_CONFIRMATION.INVITE);

            String oldIsland = invited.getIsland();
            plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().remove(invited.getPlayer());
            invited.setIsland(inviter.getIsland());
            invited.save();
            inviterIsland.getMeta().getMembers().put(invited.getPlayer(), RANK.MEMBER);
            inviterIsland.save(false);

            Bukkit.getPlayer(invited.getPlayer()).teleport(plugin.getIslandManager().getIsland(inviter.getIsland()).getSpawnLocation());
            if (plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().size() == 1) {
                try {
                    plugin.getIslandManager().unloadIsland(plugin.getIslandManager().getIsland(oldIsland), false);
                } catch (IslandHasPlayersOnlineException e) {
                    e.printStackTrace();
                }
                try {
                    plugin.getIslandManager().deleteIsland(oldIsland);
                } catch (IslandLoadedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void addSubCommand(String command, SubCommand subCommand) {
        subCommands.put(command, subCommand);
    }

    @Override
    public OneBlock getPlugin() {
        return plugin;
    }

    @Override
    public HashMap<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public Command getParent() {
        return parent;
    }


}
