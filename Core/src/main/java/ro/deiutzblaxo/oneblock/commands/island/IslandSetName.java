package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;

import java.util.HashMap;
import java.util.List;

public class IslandSetName implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandSetName(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
        if (args.size() < 1) {
            invalidArguments(sender);
            return;
        }

        if (plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false) == null) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        Island island = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false);
        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.CHANGENAME)) {
            player.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_NAME_ALLOW));
            return;
        }
        StringBuilder builder = new StringBuilder(args.get(0));
        args.remove(args.get(0));
        args.forEach(s -> {
            builder.append(" " + s);
        });
        if (builder.toString().equals(island.getMeta().getName())) {
            player.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_NAME_SAME));
            return;
        }
        island.getMeta().setName(builder.toString());
        player.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NAME) +  builder);

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
