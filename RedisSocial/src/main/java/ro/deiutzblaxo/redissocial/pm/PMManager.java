package ro.deiutzblaxo.redissocial.pm;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import ro.deiutzblaxo.cloud.expcetions.NoFoundException;
import ro.deiutzblaxo.redissocial.RedisSocial;
import ro.deiutzblaxo.redissocial.configmanagers.language.MESSAGE;
import ro.deiutzblaxo.redissocial.object.PlayerSocial;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class PMManager {
    private RedisSocial plugin;

    private Thread reciveThread;
    private Gson gson = new Gson();

    public PMManager(RedisSocial social) {
        this.plugin = social;
        AtomicReference<Boolean> failed = new AtomicReference<>(false);
        reciveThread = new Thread(() -> {
            plugin.getLogger().log(Level.INFO, "A new PMManager started");
            try (Jedis jedis = plugin.getConnection().getJedisPool().getResource()) {
                try {
                    jedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            if (!channel.equalsIgnoreCase("redissocial:pm"))
                                return;


                            PM pm = gson.fromJson(message, PM.class);
                            sendToAdmins(plugin, pm);
                            Player player = Bukkit.getPlayer(UUID.fromString(pm.uuidTarget));
                            if (player == null)
                                return;
                            PlayerSocial playerSocial = plugin.getPlayer().get(UUID.fromString(pm.uuidTarget));
                            playerSocial.setLastPlayerPMUUID(UUID.fromString(pm.uuidSender));

                            try {
                                TextComponent prefix = new TextComponent(plugin.getMessageManager().get(MESSAGE.PREFIX_MESSAGE_RECEIVE)
                                        .replace("{sender}", plugin.getNameUUIDManager().getNameByUUID(playerSocial.getLastPlayerPMUUID()))
                                        .replace("{target}", player.getName()));
                                prefix.addExtra(ComponentSerializer.parse(pm.getMessage())[0]);
                                player.spigot().sendMessage(prefix);
                            } catch (NoFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }, "redissocial:pm");
                } catch (Exception e) {
                    e.printStackTrace();
                    failed.set(true);
                }
            }
            plugin.getLogger().log(Level.WARNING, "A new PMManager will be created.");
            PMManager pm = new PMManager(social);
            social.setPMManager(pm);
        });
        reciveThread.start();
    }

    public void sendMessage(PM pm) {
        try (Jedis jedis = plugin.getConnection().getJedisPool().getResource()) {
            jedis.publish("redissocial:pm", gson.toJson(pm));
        }
    }

    public boolean isOnline(String name) {
        return plugin.getConnection().exist("socialredis:online:" + name.toLowerCase(Locale.ROOT));
    }

    public boolean isOnline(UUID uuid) {
        try {
            return isOnline(plugin.getNameUUIDManager().getNameByUUID(uuid));
        } catch (NoFoundException e) {
            return false;
        }
    }


    @SneakyThrows
    public static void sendToAdmins(RedisSocial plugin, PM pm) {
        if (plugin.getSpions().isEmpty())
            return;
        TextComponent prefix = new TextComponent(plugin.getMessageManager().get(MESSAGE.PREFIX_SOCIAL_SPY).
                replace("{target}", plugin.getNameUUIDManager().getNameByUUID(UUID.fromString(pm.uuidTarget))).replace("{sender}", plugin.getNameUUIDManager().getNameByUUID(UUID.fromString(pm.getUuidSender()))));
        prefix.addExtra(ComponentSerializer.parse(pm.message)[0]);
        plugin.getSpions().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.spigot().sendMessage(prefix);
        });
    }


}
