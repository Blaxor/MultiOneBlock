package ro.deiutzblaxo.redissocial.pm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ro.deiutzblaxo.redissocial.RedisSocial;
import ro.deiutzblaxo.redissocial.configmanagers.language.MESSAGE;

public class SocialSpyCommand implements CommandExecutor {
    private RedisSocial plugin;

    public SocialSpyCommand(RedisSocial plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to do that!");
            return true;
        }
        if (!sender.hasPermission("redissocial.spy")) {
            sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_PERMISSION));
            return true;
        }
        Player player = ((Player) sender).getPlayer();
        if (plugin.getSpions().contains(player.getUniqueId())) {
            plugin.getSpions().remove(player.getUniqueId());
            player.sendMessage(plugin.getMessageManager().get(MESSAGE.SOCIALSPY_DISABLE));
        } else {
            plugin.getSpions().add(player.getUniqueId());
            player.sendMessage(plugin.getMessageManager().get(MESSAGE.SOCIALSPY_ENABLED));
        }
        return true;
    }
}
