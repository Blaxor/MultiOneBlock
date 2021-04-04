package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.communication.action.invite.InviteResponses;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.ResponseInvite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class IslandDenyCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandDenyCommand(OneBlock plugin, String aliases[], String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;

        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        if (sender instanceof Player) {

            PlayerOB playerOB = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
            if (playerOB.getParticipant().containsKey(SCOPE_CONFIRMATION.INVITE)) {

                if (playerOB.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0) instanceof RequestInvite) {
                    RequestInvite invite = (RequestInvite) playerOB.getParticipant().get(SCOPE_CONFIRMATION.INVITE).get(0);
                    Invite.sendResponse(plugin, (Player) sender, new ResponseInvite(invite.invited, invite.inviter, InviteResponses.REJECT));
                    sender.sendMessage("you rejected the invitation");

                } else {
                    List<Object> data = playerOB.getParticipant().get(SCOPE_CONFIRMATION.INVITE);
                    PlayerOB inviter = (PlayerOB) data.get(0);
                    sender.sendMessage("you rejected the invitation");
                }

                playerOB.getParticipant().remove(SCOPE_CONFIRMATION.INVITE);
                playerOB.getTimers().remove(SCOPE_CONFIRMATION.INVITE);
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
