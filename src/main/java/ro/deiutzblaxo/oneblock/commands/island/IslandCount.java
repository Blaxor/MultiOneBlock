package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;

import java.util.HashMap;
import java.util.List;

public class IslandCount implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandCount(OneBlock plugin, String aliases[], String permission, Command parent) {
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
        if (plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false) == null) {
            player.sendMessage("Please use /is go first!");//TODO MESSAGE
            return;
        }
        Island island = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false);
        sender.sendMessage("The block braked: " + ChatColor.translateAlternateColorCodes('&', "" + island.getMeta().getCount()));//TODO MESSAGE
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
