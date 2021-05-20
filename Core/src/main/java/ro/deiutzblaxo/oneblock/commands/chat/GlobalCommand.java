package ro.deiutzblaxo.oneblock.commands.chat;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.eventlisteners.ChatListener;

import java.util.*;

public class GlobalCommand implements Command, CommandExecutor, TabCompleter {

    protected String[] aliases;
    protected String permission;
    protected HashMap<String, SubCommand> subCommands = new HashMap<>();
    protected OneBlock plugin;

    public GlobalCommand(OneBlock plugin, String[] aliases, String permission) {
        this.aliases = aliases;
        this.permission = "oneblock." + permission;
        PluginCommand command = plugin.getCommand("global");
        command.setExecutor(this);
        command.setAliases(Arrays.asList(this.aliases.clone()));
        command.setTabCompleter(this);
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!sender.hasPermission(permission)) {
            noPermission(sender);
            return false;

        }

        PlayerOB player = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
        player.setGlobalChat(!player.isGlobalChat());
        if (player.isGlobalChat())
            ChatListener.globalRecipients.add(((Player) sender));
        else
            ChatListener.globalRecipients.remove(((Player) sender));

        Bukkit.getPlayer(player.getPlayer()).sendMessage("Global Chat TOGGLE.");
        return true;
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
