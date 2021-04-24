package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;

import java.util.*;

public class IslandCommand implements Command, CommandExecutor, TabCompleter {

    protected String[] aliases;
    protected String permission;
    protected HashMap<String, SubCommand> subCommands = new HashMap<>();
    protected OneBlock plugin;

    public IslandCommand(OneBlock plugin, String[] aliases, String permission) {
        this.aliases = aliases;
        this.permission = "oneblock." + permission;
        PluginCommand command = plugin.getCommand("island");
        command.setExecutor(this);
        command.setAliases(Arrays.asList(this.aliases.clone()));
        command.setTabCompleter(this);
        subCommands.put("teleport", new IslandTeleportCommand(plugin, new String[]{"tp", "go"}, "teleport", this));
        subCommands.put("phases", new IslandPhase(plugin, new String[]{"phase", "etapa"}, "phases", this));
        subCommands.put("team", new IslandTeam(plugin, new String[]{"echipa"}, "team", this));
        subCommands.put("kick", new IslandKick(plugin, new String[]{}, "kick", this));
        subCommands.put("tier", new IslandTier(plugin, new String[]{}, "tier", this));
        subCommands.put("level", new IslandLevel(plugin, new String[]{"nivel"}, "level", this));
        subCommands.put("top", new IslandTop(plugin, new String[]{}, "top", this));
        subCommands.put("leave", new IslandLeave(plugin, new String[]{"paraseste"}, "leave", this));
        subCommands.put("reset", new IslandReset(plugin, new String[]{}, "reset", this));
        subCommands.put("ban", new IslandBan(plugin, new String[]{}, "ban", this));
        subCommands.put("unban", new IslandUnBan(plugin, new String[]{}, "unban", this));
        subCommands.put("banlist", new IslandBanList(plugin, new String[]{}, "banlist", this));
        subCommands.put("expel", new IslandExpel(plugin, new String[]{}, "expel", this));
        subCommands.put("name", new IslandName(plugin, new String[]{}, "name", this));
        subCommands.put("setname", new IslandSetName(plugin, new String[]{}, "setname", this));
        subCommands.put("setspawn", new IslandSetSpawn(plugin, new String[]{}, "setspawn", this));
        subCommands.put("count", new IslandCount(plugin, new String[]{}, "count", this));

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!doCommand(sender, new ArrayList<>(Arrays.asList(args))))
            return false;
        //what command can do ->
        sender.sendMessage("this is the island command");
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
