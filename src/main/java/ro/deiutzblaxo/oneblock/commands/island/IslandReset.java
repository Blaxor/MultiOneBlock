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
import ro.deiutzblaxo.oneblock.player.events.PlayerResetIslandEvent;

import java.util.HashMap;
import java.util.List;

public class IslandReset implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandReset(OneBlock plugin, String aliases[], String permission, Command parent) {
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
            sender.sendMessage("You are not a player!");//TODO MESSAGE
            return;
        }
        Player player = (Player) sender;
        PlayerOB owner = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        Island island = owner.getIsland(false);
        if (island == null) {
            sender.sendMessage(plugin.getLangManager().get(MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        if (!island.getBukkitWorld().getPlayers().isEmpty())
            island.getBukkitWorld().getPlayers().forEach(player1 -> {
                player1.teleport(plugin.getSpawnLocation());
            });
        Bukkit.getPluginManager().callEvent(new PlayerResetIslandEvent(plugin, owner, island));
        sender.sendMessage("You reset the island!");//TODO MESSAGE


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
