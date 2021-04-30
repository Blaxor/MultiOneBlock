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
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class IslandUnBan implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandUnBan(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
            sender.sendMessage("You need to be a player to do this or use /isa");
            return;
        }
        if (args.size() != 1) {
            invalidArguments(sender);
            return;
        }


        Player player = (Player) sender;
        Island island = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false);
        if (island == null) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(plugin.getPlayerManager().getUUIDByName(args.get(0)));
        } catch (PlayerNoExistException e) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.PLAYER_NO_EXISTS));
            return;
        }
        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.UNBAN)) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_UNBAN_ALLOW));
            return;
        }
        if (!island.unban(uuid)) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_ERROR_UNBAN_BAN));
            return;
        }
        try {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_UNBAN).replace("{name}", plugin.getPlayerManager().getNameByUUID(uuid)));
        } catch (PlayerNoExistException e) {
            e.printStackTrace();
        }
        if (Bukkit.getPlayer(uuid) != null) {
            Bukkit.getPlayer(uuid).sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_UNBANNED));
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
