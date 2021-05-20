package ro.deiutzblaxo.oneblock.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.phase.objects.RARITY;

import java.util.HashMap;
import java.util.List;

public class SaveChestCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public SaveChestCommand(OneBlock plugin, String[] aliases, String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;

        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(permission)) {
            noPermission(sender);
            return;
        }
        if (args.size() < 3) {
            invalidArguments(sender);
            return;
        }
        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(5);
        if (block == null) {
            sender.sendMessage("Go closer to the chest");//TODO MESSAGE
            return;
        }
        if (block.getType() != Material.CHEST) {
            sender.sendMessage("Please look at designed chest!");//TODO MESSAGE
            return;
        }

        Chest chest = (Chest) block.getState();
        try {//phase identfchest rarity
            plugin.getPhaseManager().saveChest(args.get(0), args.get(1), RARITY.valueOf(args.get(2).toUpperCase()), chest);
        } catch (Exception exception) {
            sender.sendMessage(ChatColor.RED + "Exception : " + exception.getMessage());
            return;
        }
        sender.sendMessage("You have been added a new chest!");//TODO


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
