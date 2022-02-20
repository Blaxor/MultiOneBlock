package ro.deiutzblax.expendeditems;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExpendedItems extends JavaPlugin {

    ItemManager manager;

    @Override
    public void onEnable() {
        manager = new ItemManager(this);
        getCommand("test").setExecutor((sender, command, label, args) -> {
            manager.items.values().forEach(itemStack -> ((Player) sender).getInventory().addItem(itemStack));

            return false;
        });


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
