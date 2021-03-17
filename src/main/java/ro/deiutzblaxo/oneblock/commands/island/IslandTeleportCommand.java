package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.player.PlayerManager;

import java.util.HashMap;
import java.util.List;

public class IslandTeleportCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;

    public IslandTeleportCommand(String aliases[],String permission,Command parent){
        this.aliases = aliases;
        this.permission = parent.getPermission()+"."+permission;
        this.parent = parent;
        Block data;

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(OneBlock.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).getOverworld(false).getSpawnLocation());
            sender.sendMessage("YEEEE SUB COMANDA MERGEEEEEEE!!!!!!!!!!!!!");
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
        subCommands.put(command,subCommand);
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
