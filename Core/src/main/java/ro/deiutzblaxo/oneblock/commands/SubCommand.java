package ro.deiutzblaxo.oneblock.commands;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public interface SubCommand extends Command {


    Command getParent();

    void execute(CommandSender sender , List<String> args);

}
