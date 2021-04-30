package ro.deiutzblaxo.oneblock.commands.admin;

import org.bukkit.command.CommandSender;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SetCountCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public SetCountCommand(OneBlock plugin, String[] aliases, String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;

        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(permission)) {
            noPermission(sender);
            return;
        }
        if (args.size() < 2) {
            invalidArguments(sender);
            return;
        }
        Island island;
        try {
            island = plugin.getPlayerManager().getPlayer(UUID.fromString(plugin.getPlayerManager().getUUIDByName(args.get(0)))).getIsland(false);
        } catch (PlayerNoExistException e) {
            e.printStackTrace();
            sender.sendMessage("Player don`t exist!");//TODO MESSAGE
            return;
        }
        if (island == null) {
            sender.sendMessage("The player is offline or his island is not loaded!");
            return;
        }
        island.getMeta().setCount(Integer.parseInt(args.get(1)));
        sender.sendMessage("The count have been changed!");


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
