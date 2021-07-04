package ro.deiutzblaxo.redissocial.pm.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.redissocial.RedisSocial;
import ro.deiutzblaxo.redissocial.configmanagers.language.MESSAGE;
import ro.deiutzblaxo.redissocial.object.PlayerSocial;
import ro.deiutzblaxo.redissocial.pm.PM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReplayCommand implements CommandExecutor {
    private RedisSocial plugin;

    public ReplayCommand(RedisSocial social) {
        plugin = social;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player!");
            return true;
        }
        Player player = (Player) sender;
        PlayerSocial social = plugin.getPlayer().get(player.getUniqueId());
        if (social.getLastPlayerPMUUID() == null || !plugin.getPMManager().isOnline(social.getLastPlayerPMUUID())) {
            sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_PLAYER_OFFLINE));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_INVALID_ARGUMENTS_REPLAY));
            return true;
        }
        try {
            String prefix = plugin.getMessageManager().get(MESSAGE.PREFIX_MESSAGE_SEND)
                    .replace("{sender}", player.getName()).replace("{target}", plugin.getNameUUIDManager().getNameByUUID(social.getLastPlayerPMUUID()));
            player.spigot().sendMessage(new TextComponent(prefix + generateMessage(0, args)));
            plugin.getPMManager().sendMessage(new PM(social.getLastPlayerPMUUID().toString()
                    , player.getUniqueId().toString(), new TextComponent(generateMessage(0, args))));
        } catch (NoFoundException e) {
            sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_PLAYER_OFFLINE));
        }

        return true;
    }

    private String generateMessage(int skip, String... args) {
        ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
        return a.stream().skip(skip).collect(Collectors.joining(" "));
    }
}
