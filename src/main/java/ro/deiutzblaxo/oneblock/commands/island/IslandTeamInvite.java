package ro.deiutzblaxo.oneblock.commands.island;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.invite.RequestInvite;
import ro.deiutzblaxo.oneblock.communication.action.invite.Invite;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.SCOPE_CONFIRMATION;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IslandTeamInvite implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTeamInvite(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
        PlayerOB player = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
        if (args.size() > 0) {
            if (Bukkit.getPlayer(args.get(0)) == null) {
                try {
                    String uuidInvited = plugin.getPlayerManager().getUUIDByName(args.get(0));
                    if (plugin.getPlayerManager().getServerByPlayerUUID(uuidInvited).equalsIgnoreCase("none")) {
                        sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.ISLAND_INVITE_OFFLINE));
                        return;
                    } else {
                        RequestInvite invite = new RequestInvite(uuidInvited, player.getPlayer().toString(), player.getIsland());
                        Invite.sendInvite(plugin, ((Player) sender).getPlayer(), invite);
                        sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.ISLAND_INVITE_SENDER).
                                replace("{name}", plugin.getDbManager().getLikeString(TableType.NAME.table, "NAME", args.get(0), "NAME")));
                        return;
                    }
                } catch (PlayerNoExistException e) {
                    sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.ISLAND_INVITE_EXISTS));
                    return;
                }
            }
            Player invitedPlayer = Bukkit.getPlayer(args.get(0));
            PlayerOB invited = plugin.getPlayerManager().getPlayer(invitedPlayer.getUniqueId());
            Island inviterIsland = plugin.getIslandManager().getIsland(player.getIsland());

            ArrayList<Object> data = new ArrayList<>();
            data.add(player);//INVITER
            data.add(inviterIsland);//INVITER ISLAND
            //add invite to timer
            invited.getParticipant().put(SCOPE_CONFIRMATION.INVITE, data);
            invited.getTimers().put(SCOPE_CONFIRMATION.INVITE, 10);
            sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.ISLAND_INVITE_SENDER).
                    replace("{name}", plugin.getDbManager().getLikeString(TableType.NAME.table, "NAME", args.get(0), "NAME")));
            invitedPlayer.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.ISLAND_INVITE_RECEIVER).replace("{name}", sender.getName()));


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
