package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.command.CommandSender;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class IslandTop implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTop(OneBlock plugin, String aliases[], String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;


        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        plugin.getIslandLevelManager().getTopIslands().forEach(triplet -> {
            AtomicReference<String> abc = new AtomicReference<>("");
            triplet.getLast().getMembers().forEach((uuid, rank) -> {
                if (rank == RANK.OWNER) {
                    abc.set(plugin.getPlayerManager().getNameByUUID(uuid));
                    return;
                }
            });
            abc.set(abc.get() + " level " + triplet.getMiddle());//TODO MESSAGE
            sender.sendMessage(abc.get());
        });


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
