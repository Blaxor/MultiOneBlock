package ro.deiutzblaxo.oneblock.commands.island;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.Responses;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.ResponseInvite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.Rank.RankEnum;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;
import ro.deiutzblaxo.oneblock.player.events.PlayerJoinIslandEvent;
import ro.deiutzblaxo.oneblock.player.events.PlayerLeaveIslandEvent;

import java.util.HashMap;
import java.util.List;

public class IslandTeamConfirm implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTeamConfirm(OneBlock plugin, String[] aliases, String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;


        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        PlayerOB invited = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (invited.getTimers().containsKey(SCOPE_CONFIRMATION.INVITE)) {

            if (invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0) instanceof RequestInvite) {


                RequestInvite requestInvite = (RequestInvite) invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0);
                String oldIsland = invited.getIsland();
                if (plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().get(invited.getPlayer()).equals(RankEnum.OWNER) && plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().size() > 1) {

                    sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_ERROR_TEAM_CONFIRM));
                    return;
                }
                plugin.getIslandManager().getIsland(oldIsland).getMeta().getMembers().remove(invited);

                // send reponse
                Invite.sendResponse(plugin, player, new ResponseInvite(requestInvite.invited, requestInvite.inviter, Responses.ACCEPT));

                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    Bukkit.getPluginManager().callEvent(new PlayerLeaveIslandEvent(plugin, invited));
                    invited.setIsland(requestInvite.islandInviter);
                    invited.save();
                    Invite.sendPlayerToServer(plugin, player, plugin.getIslandManager().getServer(requestInvite.islandInviter));


                }, 1);
                return;
            }

            List<Object> data = invited.getParticipant().get(SCOPE_CONFIRMATION.INVITE);
            PlayerOB inviter = (PlayerOB) data.get(0);
            player.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_INVITE_ACCEPTED));
            Bukkit.getPlayer(inviter.getPlayer()).sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_INVITE_ACCEPT).replace("{name}",
                    plugin.getPlayerManager().getNameByUUID(inviter.getPlayer())));

            Island inviterIsland = (Island) data.get(1);

            invited.getTimers().remove(SCOPE_CONFIRMATION.INVITE);
            invited.getParticipant().remove(SCOPE_CONFIRMATION.INVITE);

            Bukkit.getPluginManager().callEvent(new PlayerJoinIslandEvent(plugin, invited.getPlayer().toString(), inviter, inviterIsland, invited.getIsland() != null, false));
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
