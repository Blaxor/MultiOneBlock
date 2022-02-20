package ro.deiutzblax.expendeditems;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblax.expendeditems.item.ItemSerializer;
import ro.deiutzblaxo.cloud.utils.objects.Pair;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ItemManager {

    public HashMap<String, ItemStack> items = new HashMap<>();
    private File itemsFolder;

    public ItemManager(JavaPlugin plugin) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();
        File data = plugin.getDataFolder();
        itemsFolder = new File(data, "/items/");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdirs();
            plugin.saveResource("items/item0.yaml", true);
        }
        loadItems();
        System.out.println(items);
    }

    @SneakyThrows
    public void loadItems() {
        items.clear();
        File[] items = itemsFolder.listFiles();
        for (File file : items) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Pair<String, ItemStack> item = ItemSerializer.toItem(reader.lines().collect(Collectors.joining()));
                this.items.put(item.getFirst(), item.getLast());
            }
        }


    }

}
