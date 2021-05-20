package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.player.events.PlayerKickIslandEvent;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class IslandKick implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandKick(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
        if (args.size() != 1) {
            invalidArguments(sender);
            return;
        }
        Player player = (Player) sender;
        PlayerOB owner = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        Island island = owner.getIsland(false);
        if (island == null) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.KICK)) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_KICK));
        }
        UUID kicked;
        try {
            kicked = UUID.fromString(plugin.getPlayerManager().getUUIDByName(args.get(0)));
        } catch (PlayerNoExistException e) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.PLAYER_NO_EXISTS));
            return;
        }
        HashMap<UUID, RANK> members = island.getMeta().getMembers();

        if (!members.containsKey(kicked)) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_KICK_PART).replace("{name}", args.get(0)));
            return;
        }
        if (args.get(0).equalsIgnoreCase(player.getName())) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_KICK_SELF));
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerKickIslandEvent(plugin, kicked, island));
        sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_KICK_OTHER));
        if (Bukkit.getPlayer(kicked) != null) {
            Bukkit.getPlayer(kicked).sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_KICKED));
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
