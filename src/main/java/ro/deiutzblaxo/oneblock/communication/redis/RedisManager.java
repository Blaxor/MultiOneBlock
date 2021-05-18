package ro.deiutzblaxo.oneblock.communication.redis;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.util.ArrayList;
import java.util.logging.Level;

public class RedisManager {
    private final String START_REDIS_URL = "redis://";
    private OneBlock plugin;
    private String host;
    private String password;
    private int port;
    private Jedis subscribe;
    private Jedis publish;

    private ArrayList<BukkitTask> listeners = new ArrayList<>();

    public RedisManager(OneBlock plugin, String host, int port, String password) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.plugin = plugin;
        subscribe = new Jedis(host, port);

        publish = new Jedis(host, port);
        if (password != "") {
            subscribe.auth(password);
            publish.auth(password);
        }
    }

    public void send(String channel, String message) {
        publish.publish(channel, message);


    }

    @SneakyThrows
    public void registerListener(String[] channels, JedisPubSub listener) {
        BukkitTask task;
        try {
            task = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                subscribe.subscribe(listener, channels);
            });
        } catch (Exception e) {
            e.printStackTrace();
/*            if (subscribe != null)
               if (subscribe.isConnected()) {
                    subscribe.close();
                    subscribe.disconnect();
                }*/

            plugin.getLogger().log(Level.INFO, "The error has been handled by instantiate a new Jedis and listen again.");
            subscribe = new Jedis(host, port);
            registerListener(channels, listener);

        }
    }

    public void onDisable() {

        publish.close();
        publish.disconnect();
        subscribe.close();
        subscribe.disconnect();
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        return poolConfig;
    }
}
