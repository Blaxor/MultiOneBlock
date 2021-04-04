package ro.deiutzblaxo.oneblock.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public interface Button {

    void click(Player player);

    String name();

    List<String> getDescription();

    Material getMaterial();
}
