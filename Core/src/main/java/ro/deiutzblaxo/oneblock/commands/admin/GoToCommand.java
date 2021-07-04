package ro.deiutzblaxo.oneblock.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.tpa.TeleportRequest;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.HashMap;
import java.util.List;

public class GoToCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public GoToCommand(OneBlock plugin, String[] aliases, String permission, Command parent) {
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
            sender.sendMessage("You need to be a player!");
            return;
        }
        Player player = (Player) sender;
        if (!sender.hasPermission(permission)) {
            noPermission(sender);
            return;
        }
        //String island = plugin.getPlayerManager().getServerByPlayerUUID(plugin.getPlayerManager().getUUIDByName(args.get(0)));
        String uuidTarget = null;
        try {
            uuidTarget = plugin.getPlayerManager().getUUIDByName(args.get(0));
        } catch (PlayerNoExistException e) {
            sender.sendMessage(plugin.getLangManager().get(player, MESSAGE.PLAYER_NO_EXISTS));
            return;
        }
        String uuidIsland = null;
        try {
            uuidIsland = plugin.getDbManager().getString(TableType.PLAYERS.table, "ISLAND", "UUID", uuidTarget);
        } catch (NoFoundException e) {
            sender.sendMessage(ChatColor.RED + "Player don`t have an island!");
            return;
        }
        String server = plugin.getIslandManager().getServer(uuidIsland);
        if (server != null)
            if (server.equalsIgnoreCase(OneBlock.SERVER)) {
                player.teleport(plugin.getIslandManager().getIsland(uuidIsland).getSpawnLocation());
                sender.sendMessage(ChatColor.GREEN + "Done!");
            } else {
                TeleportRequest.sendRequest(plugin, player, server);
            }
        else {
            sender.sendMessage("Island is not loaded anywhere. It will be loaded on this server!");
            Island island = plugin.getIslandManager().loadIsland(uuidIsland);
            player.teleport(island.getSpawnLocation());
            sender.sendMessage("Done");
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
