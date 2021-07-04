package ro.deiutzblaxo.redissocial.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.deiutzblaxo.redissocial.RedisSocial;
import ro.deiutzblaxo.redissocial.object.PlayerSocial;

import java.util.Locale;

public class JoinQuitListener implements Listener {
    RedisSocial plugin;

    public JoinQuitListener(RedisSocial social) {
        plugin = social;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getPlayer().put(event.getPlayer().getUniqueId(), new PlayerSocial());
            plugin.getNameUUIDManager().add(event.getPlayer().getName(), event.getPlayer().getUniqueId());
            plugin.getConnection().set("socialredis:online:" + event.getPlayer().getName().toLowerCase(Locale.ROOT), "online");
            if (event.getPlayer().hasPermission("redissocial.spy.auto")) {
                plugin.getSpions().add(event.getPlayer().getUniqueId());
            }
        }, 3);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayer().remove(event.getPlayer().getUniqueId());
        plugin.getConnection().delete("socialredis:online:" + event.getPlayer().getName().toLowerCase(Locale.ROOT));
        plugin.getSpions().remove(event.getPlayer().getUniqueId());
    }

}
