package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.team.TeamInfoMenu;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.HashMap;
import java.util.List;

public class IslandTeam implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTeam(OneBlock plugin, String aliases[], String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;
        addSubCommand("confirm", new IslandTeamConfirm(plugin, new String[]{"yes", "da", "accept"}, "confirm", this));
        addSubCommand("invite", new IslandTeamInvite(plugin, new String[]{"inv"}, "invite", this));
        addSubCommand("deny", new IslandDenyCommand(plugin, new String[]{"no", "decline", "nu"}, "deny", this));
        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!doCommand(sender, args))
            return;
        Player player = (Player) sender;
        if (plugin.getIslandManager().getIsland(plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland()) != null)
            sendMembersList(player);
        else
            player.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
    }

    public void sendMembersList(Player player) {
        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        player.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_TEAM_MENU_TITLE));
        plugin.getIslandManager().getIsland(playerOB.getIsland()).getMeta().getMembers().forEach((uuid, rank) -> {

            player.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName() + " - " + rank.name());


        });
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
