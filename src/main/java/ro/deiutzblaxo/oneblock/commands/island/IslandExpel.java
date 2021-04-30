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

public class IslandExpel implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandExpel(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.EXPEL)) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_ERROR_EXPEL));
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(plugin.getPlayerManager().getUUIDByName(args.get(0)));
        } catch (PlayerNoExistException e) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.PLAYER_NO_EXISTS));
            return;
        }
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.PLAYER_OFFLINE));
            return;
        }

        if (!target.getLocation().getWorld().getName().equalsIgnoreCase(island.getBukkitWorld().getName())) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_NOT_ENTERED));
            return;
        }
        if (island.getMeta().getMembers().get(uuid) != null) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_EXPEL_MEMBER));
            return;
        }
        if (plugin.getPlayerManager().getPlayer(uuid).getIsland(false) != null)
            plugin.getPlayerManager().getPlayer(uuid).getIsland(false).teleportHere(target);
        else
            target.teleport(plugin.getSpawnLocation());
        target.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_EXPELED));
        player.sendMessage(plugin.getLangManager().get(player, MESSAGE.ISLAND_EXPEL).replace("{name}", player.getName()));
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
