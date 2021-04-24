package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.utils.Location;

import java.util.HashMap;
import java.util.List;

public class IslandSetSpawn implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandSetSpawn(OneBlock plugin, String aliases[], String permission, Command parent) {
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
        if (!player.getWorld().getName().equals(island.getBukkitWorld().getName())) {
            player.sendMessage("You are not on your island. Please go on your island!");//TODO MESSAGE
            return;
        }
        if(!island.isAllow(player.getUniqueId(), PERMISSIONS.SETSPAWN)){
            player.sendMessage("You can`t do that!");//TODO
        }
        if (player.getLocation().getWorld().getHighestBlockYAt(player.getLocation()) < 5) {
            player.sendMessage("This location is not safe , please select another location!");//TODO MESSAGE
            return;
        }
        island.setSpawnLocation(Location.toLocation(player.getLocation()));
        player.sendMessage("You have set a new spawn point for the island!");

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
