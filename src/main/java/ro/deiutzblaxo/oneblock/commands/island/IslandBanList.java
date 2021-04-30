package ro.deiutzblaxo.oneblock.commands.island;

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

public class IslandBanList implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandBanList(OneBlock plugin, String[] aliases, String permission, Command parent) {
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


        Player player = (Player) sender;
        Island island = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getIsland(false);
        if (island == null) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_NOT_LOADED));
            return;
        }
        if (!island.isAllow(player.getUniqueId(), PERMISSIONS.BANLIST)) {
            sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_BANLIST_NOT_ALLOW));
            return;
        }
        StringBuilder builder = new StringBuilder();
        island.getMeta().getBanned().forEach(uuid -> {
            try {
                builder.append(plugin.getPlayerManager().getNameByUUID(uuid) + " ");
            } catch (PlayerNoExistException e) {
                e.printStackTrace();
            }
        });
        sender.sendMessage(plugin.getLangManager().get(player,MESSAGE.ISLAND_BANLIST) + builder);

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
