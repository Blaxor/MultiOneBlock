package ro.deiutzblaxo.oneblock.commands.island;

import com.google.common.base.Suppliers;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

import java.util.*;
import java.util.stream.Collectors;

public class IslandCommand implements Command, CommandExecutor, TabCompleter {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();

    public IslandCommand(OneBlock plugin ,String aliases[], String permission){
        this.aliases = aliases;
        this.permission = permission;
        PluginCommand command = plugin.getCommand("island");
        command.setExecutor(this);
        command.setAliases(Arrays.asList(this.aliases.clone()));
        command.setTabCompleter(this);
        subCommands.put("teleport",new IslandTeleportCommand(new String[]{""},"teleport",this));

    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(!doCommand(sender,new ArrayList<>(Arrays.asList(args))))
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
        subCommand.addSubCommand(command.toLowerCase(Locale.ROOT),subCommand);
    }
    public HashMap<String, SubCommand> getSubCommands(){
        return subCommands;
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
        return null;
    }
}