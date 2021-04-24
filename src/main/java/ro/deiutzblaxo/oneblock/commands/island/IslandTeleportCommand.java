package ro.deiutzblaxo.oneblock.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.commands.Command;
import ro.deiutzblaxo.oneblock.commands.SubCommand;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.player.PlayerOB;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.util.HashMap;
import java.util.List;

public class IslandTeleportCommand implements SubCommand {

    String[] aliases;
    String permission;
    HashMap<String, SubCommand> subCommands = new HashMap<>();
    Command parent;
    OneBlock plugin;

    public IslandTeleportCommand(OneBlock plugin, String aliases[], String permission, Command parent) {
        this.aliases = aliases;
        this.permission = parent.getPermission() + "." + permission;
        this.parent = parent;
        this.plugin = plugin;

        for (String aliase : aliases)
            this.parent.addSubCommand(aliase, this);

    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        if (sender instanceof Player) {


            Player player = (Player) sender;
            PlayerOB playerOB = plugin.getPlayerManager().getPlayer(player.getUniqueId());
            Island island = plugin.getIslandManager().getIsland(playerOB.getIsland());
            if (island == null) {
                island = plugin.getIslandManager().loadIsland(playerOB.getIsland() == null ? "WORLD_" + playerOB.getPlayer() : playerOB.getIsland());
                IslandMeta meta = island.getMeta();
                if (!meta.getMembers().containsKey(player.getUniqueId())) {
                    meta.getMembers().put(player.getUniqueId(), RANK.OWNER);

                    island.setMeta(meta);
                }
                if (Bukkit.getPlayer(island.getOwner()) != null)
                    island.getMeta().setRadiusType(BorderHandler.getTypeByPermission(Bukkit.getPlayer(island.getOwner())));
                island.changeBorder();

            }

            playerOB.setIsland(island.getUuidIsland());
            island.teleportHere(((Player) sender).getPlayer());
            sender.sendMessage(plugin.getLangManager().get(MESSAGE.TELEPORTED_TO_ISLAND));
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
