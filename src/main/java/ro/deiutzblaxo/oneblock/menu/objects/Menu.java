package ro.deiutzblaxo.oneblock.menu.objects;

import org.bukkit.entity.Player;

public interface Menu {

    String getID();

    String getPermission();

    default boolean hasPermission(Player player) { return player.hasPermission(getPermission()); }

    Button getButton(int slot);
}
