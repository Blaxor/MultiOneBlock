package ro.deiutzblaxo.oneblock.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public interface Command {

    String[] getAliases();

    String getPermission();

    void addSubCommand(String command, SubCommand subCommand);

    default void noPermission(CommandSender sender){
        sender.sendMessage("You don`t have perm");
    }
    default void invalidArguments(CommandSender sender){
        sender.sendMessage("Invalid Arguments");
    }

    HashMap<String, SubCommand> getSubCommands();

    default boolean doCommand(CommandSender sender, List<String> args){

        if(args.size() > 0)
            if(getSubCommands().containsKey(args.get(0))){
                getSubCommands().get(args.get(0)).execute(sender,args.stream().skip(1).collect(Collectors.toList()));
                return false;
            }else{
                invalidArguments(sender);
                return false;
            }
        if(!sender.hasPermission(getPermission())) {
            noPermission(sender);
            return false;
        }
        return true;

    }
    default List<String> doTabComplete(CommandSender sender, List<String> args){
        if(args.size() > 0){

        }
        return (List<String>) getSubCommands().keySet();
    }
}
