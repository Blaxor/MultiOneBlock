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
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PMCommand implements CommandExecutor {
    private RedisSocial plugin;

    public PMCommand(RedisSocial social) {
        this.plugin = social;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to use this command!");
        }
        Player player = (Player) sender;
        if (args.length <= 1) {
            player.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_INVALID_ARGUMENTS_PM));
            return true;
        }
        PlayerSocial social = plugin.getPlayer().get(player.getUniqueId());
        if(player.getName().equalsIgnoreCase(args[0])){
            player.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_SELF));
            return true;
        }

        try {
            UUID uuid = plugin.getNameUUIDManager().getUUIDByName(args[0]);
            if (plugin.getConnection().exist("socialredis:online:" + args[0].toLowerCase(Locale.ROOT))) {
                social.setLastPlayerPMUUID(uuid);
                plugin.getPMManager().sendMessage(new PM(social.getLastPlayerPMUUID().toString()
                        , player.getUniqueId().toString(), new TextComponent(generateMessage(1, args))));

                String prefix = plugin.getMessageManager().get(MESSAGE.PREFIX_MESSAGE_SEND)
                        .replace("{sender}", player.getName()).replace("{target}", plugin.getNameUUIDManager().getNameByUUID(uuid));
                player.spigot().sendMessage(new TextComponent(prefix + generateMessage(1, args)));
            } else {
                sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_PLAYER_OFFLINE));
            }
        } catch (NoFoundException e) {
            sender.sendMessage(plugin.getMessageManager().get(MESSAGE.ERROR_PLAYER_NO_EXIST));
            return true;
        }


        return false;
    }

    private String generateMessage(int skip, String... args) {
        ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
        return a.stream().skip(skip).collect(Collectors.joining(" "));
    }
}
