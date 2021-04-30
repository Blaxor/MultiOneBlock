package ro.deiutzblaxo.oneblock.commands.chat;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.communication.action.chat.pm.PM;
import ro.deiutzblaxo.oneblock.communication.action.chat.pm.PMSender;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.expcetions.PlayerNoExistException;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.*;
import java.util.stream.Collectors;

public class PmCommand implements Command, CommandExecutor, TabCompleter {

    protected String[] aliases;
    protected String permission;
    protected HashMap<String, SubCommand> subCommands = new HashMap<>();
    protected OneBlock plugin;

    public PmCommand(OneBlock plugin, String[] aliases, String permission) {
        this.aliases = aliases;
        this.permission = "oneblock." + permission;
        PluginCommand command = plugin.getCommand("pm");
        command.setExecutor(this);
        command.setAliases(Arrays.asList(this.aliases.clone()));
        command.setTabCompleter(this);
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

/*        if (!sender.hasPermission(permission)) {
            noPermission(sender);
            return false; TODO DO PERMISSION
        }*/
        if (args.length < 1) {
            invalidArguments(sender);
            return false;
        }
        if (args.length == 1) {
            sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.PM_NO_MESSAGE));
            return false;
        }
        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage( plugin.getLangManager().get((Player) sender,MESSAGE.PM_SELF));
            return false;
        }
        if (Bukkit.getPlayer(args[0]) != null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLangManager().get((Player) sender,MESSAGE.PM_SEND).replace("{message}",
                    Arrays.stream(args).skip(1).collect(Collectors.joining())).replace("{name}",
                    plugin.getDbManager().getLikeString(TableType.NAME.table, "NAME", args[0], "NAME"))));
            Bukkit.getPlayer(args[0]).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLangManager().get((Player) sender,MESSAGE.PM_RECEIVE)
                    .replace("{message}", Arrays.stream(args).skip(1).collect(Collectors.joining(" "))).replace("{name}", sender.getName())));
            return true;
        }
        try {

            PM pm = new PM(((Player) sender).getUniqueId().toString(), plugin.getPlayerManager().getUUIDByName(args[0]), ChatColor.translateAlternateColorCodes('&', plugin.getLangManager().get((Player) sender,MESSAGE.PM_RECEIVE)
                    .replace("{message}", Arrays.stream(args).skip(1).collect(Collectors.joining(" "))).replace("{name}", ((Player) sender).getDisplayName())));
            PMSender.sendMessage(plugin, (Player) sender, pm);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLangManager().get((Player) sender,MESSAGE.PM_SEND)
                    .replace("{message}", Arrays.stream(args).skip(1).collect(Collectors.joining(" "))).replace("{name}",
                            plugin.getDbManager().getLikeString(TableType.NAME.table, "NAME", args[0], "NAME"))));

            return true;
        } catch (PlayerNoExistException e) {
            sender.sendMessage(plugin.getLangManager().get((Player) sender,MESSAGE.PLAYER_NO_EXISTS));
            return false;
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
        subCommands.put(command.toLowerCase(Locale.ROOT), subCommand);
    }

    public HashMap<String, SubCommand> getSubCommands() {

        return subCommands;
    }

    @Override
    public OneBlock getPlugin() {
        return plugin;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return doTabComplete(sender, new ArrayList<>(Arrays.asList(args)));
    }
}
