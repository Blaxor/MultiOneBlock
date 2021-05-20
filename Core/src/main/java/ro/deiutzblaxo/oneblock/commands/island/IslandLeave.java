package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.events.PlayerLeaveIslandEvent;

import java.util.HashMap;
import java.util.List;

public class IslandLeave implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandLeave(OneBlock plugin, String[] aliases, String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;

        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return;
        }
        Player player = (Player) sender;
        PlayerOB owner = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        Island island = owner.getIsland(false);
        if (island == null) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        if (island.getMeta().getMembers().get(player.getUniqueId()) == RANK.OWNER) {
            if (island.getMeta().getMembers().size() > 1) {
                sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_LEAVE));
                return;
            }
        }
        Bukkit.getPluginManager().callEvent(new PlayerLeaveIslandEvent(plugin, owner, island));
        sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_LEAVE));


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
