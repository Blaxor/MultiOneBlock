package ro.deiutzblaxo.oneblock.langs;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagesManager {

    private final OneBlock plugin;
    private final File file;
    private FileConfiguration config;
    @Getter
    private final HashMap<MESSAGE, String> messageStringHashMap = new HashMap<>();
    @Getter
    private final HashMap<MESSAGELIST, List<String>> messagelistListHashMap = new HashMap<>();


    public MessagesManager(OneBlock plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        file = new File(plugin.getDataFolder(), "lang.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            config = YamlConfiguration.loadConfiguration(file);
            for (MESSAGE message : MESSAGE.values()) {
                setDefault(message);
            }
            for (MESSAGELIST message : MESSAGELIST.values()) {
                setDefaultList(message);
            }
        }

    }

    public String get(Player player, MESSAGE message) {
        config = YamlConfiguration.loadConfiguration(file);

        if (config.contains(message.path)) {
            if (messageStringHashMap.containsKey(message))
                if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                    return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, messageStringHashMap.get(message)));
                else return ChatColor.translateAlternateColorCodes('&', messageStringHashMap.get(message));
            else {
                messageStringHashMap.put(message, config.getString(message.path));
                return get(player, message);
            }
        } else {
            setDefault(message);
            return get(player, message);
        }
    }

    public String get(MESSAGE message) {
        config = YamlConfiguration.loadConfiguration(file);

        if (config.contains(message.path)) {
            if (messageStringHashMap.containsKey(message))
                return ChatColor.translateAlternateColorCodes('&', messageStringHashMap.get(message));
            else {
                messageStringHashMap.put(message, config.getString(message.path));
                return get(message);
            }
        } else {
            setDefault(message);
            return ChatColor.translateAlternateColorCodes('&', get(message));
        }
    }

    public List<String> getList(MESSAGELIST list) {
        config = YamlConfiguration.loadConfiguration(file);

        if (config.contains(list.path)) {
            if (messagelistListHashMap.containsKey(list)) {
                return translate(messagelistListHashMap.get(list));
            } else
                messagelistListHashMap.put(list, config.getStringList(list.path));
            return getList(list);
        } else {
            setDefaultList(list);
            return translate(getList(list));
        }
    }

    private void setDefaultList(MESSAGELIST message) {
        config.set(message.path, message._default);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefault(MESSAGE message) {
        config.set(message.path, message.Default);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> strings) {
        List<String> translated = new ArrayList<>();
        for (String str : strings) {
            translated.add(translate(str));
        }
        return translated;
    }

    public static List<String> replace(List<String> strings, String regex, String value) {
        List<String> translated = new ArrayList<>();
        for (String str : strings) {
            translated.add(str.replace(regex, value));
        }
        return translated;
    }
}
