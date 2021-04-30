package ro.deiutzblaxo.oneblock.commands.admin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;

import java.util.*;

public class AdminCommand implements Command, CommandExecutor, TabCompleter {

    protected String[] aliases;
    protected String permission;
    protected HashMap<String, SubCommand> subCommands = new HashMap<>();
    protected OneBlock plugin;

    public AdminCommand(OneBlock plugin, String[] aliases, String permission) {
        this.aliases = aliases;
        this.permission = permission;
        PluginCommand command = plugin.getCommand("islandadmin");
        command.setExecutor(this);
        command.setAliases(Arrays.asList(this.aliases.clone()));
        command.setTabCompleter(this);
        subCommands.put("savechest", new SaveChestCommand(plugin, new String[]{"sc"}, "savechest", this));
        subCommands.put("setcount", new SetCountCommand(plugin, new String[]{}, "setcount", this));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!doCommand(sender, new ArrayList<>(Arrays.asList(args))))
            return false;
        if (!sender.hasPermission(permission))
            noPermission(sender);
        sender.sendMessage(plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId()).getIsland(false).getMeta().getBlock().toString());
        return false;
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
