package ro.deiutzblaxo.oneblock.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public interface Command {

    String[] getAliases();

    String getPermission();

    void addSubCommand(String command, SubCommand subCommand);

    OneBlock getPlugin();

    default void noPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Nu ai permisia");
    }

    default void invalidArguments(CommandSender sender) {
        sender.sendMessage("Invalid Arguments");
    }

    HashMap<String, SubCommand> getSubCommands();

    default boolean doCommand(CommandSender sender, List<String> args) {

        if (args.size() > 0)
            if (getSubCommands().containsKey(args.get(0).toLowerCase())) {
                getSubCommands().get(args.get(0).toLowerCase()).execute(sender, args.stream().skip(1).collect(Collectors.toList()));
                return false;
            } else {
                invalidArguments(sender);
                return false;
            }
//        if (!sender.hasPermission(getPermission())) {
//            noPermission(sender);
//            return false; TODO ADD PERMISSION
//        }
        return true;

    }

    default List<String> doTabComplete(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(getPermission())) {
            return new ArrayList<>();
        }
        if (args.size() > 0)
            if (getSubCommands().containsKey(args.get(0))) {
                return getSubCommands().get(args.get(0)).doTabComplete(sender, args.stream().skip(1).collect(Collectors.toList()));
            }
        return getSubCommands().keySet().stream().filter(s -> s.startsWith(args.size() == 0 ? "" : args.get(0))).collect(Collectors.toList());
    }
}
