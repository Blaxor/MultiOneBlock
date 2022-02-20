package ro.deiutzblaxo.psi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.cloud.data.mysql.MySQLConnection;
import ro.deiutzblaxo.cloud.data.mysql.MySQLManager;
import ro.deiutzblaxo.cloud.data.mysql.classic.MySQLManagerNormal;

import java.sql.Blob;
import java.util.HashMap;

public class PlayerRepo {
    private MySQLConnection connection;
    private MySQLManager manager;
    private String table;

    public PlayerRepo(MySQLConnection connection, int threads, String table) {
        manager = new MySQLManagerNormal(connection, threads);
        this.table = table;
        this.connection = connection;
        manager.createTable(table, "ID varchar(255)", "INVENTORY blob", "ENDERCHEST blob", "POTIONEFFECT blob"
                , "CONSTRAINT ID_PK PRIMARY KEY (ID)");
    }


    public void savePlayer(Player player) {
        try {
            Blob inventory = connection.getConnection().
                    createBlob();
            inventory.setBytes(1, ItemStackConvertor.serialize(player.getInventory().getContents()));
            Blob enderChest = connection.getConnection().
                    createBlob();
            enderChest.setBytes(1, ItemStackConvertor.serializeEnderChest(
                    new ItemStack[][]{player.getEnderChest().getContents()}));
            //TODO REPAIR POTION EFFECTS SAVE AND LOAD
            Blob potionEffects = connection.getConnection().createBlob();
            potionEffects.setBytes(1, EffectsConvertor.serialize(player.getActivePotionEffects()));
            Bukkit.broadcastMessage(player.getActivePotionEffects().toString());
            if (manager.exists(table, "ID", player.getUniqueId().toString()))
                manager.update(table, "ID", player.getUniqueId().toString(), new String[]{"INVENTORY", "ENDERCHEST", "POTIONEFFECT"},
                        new Object[]{inventory, enderChest, potionEffects});
            else
                manager.insert(table, new String[]{"ID", "INVENTORY", "ENDERCHEST", "POTIONEFFECT"},
                        new Object[]{player.getUniqueId().toString(), inventory, enderChest, potionEffects});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPlayer(Player player) {
        try {

            HashMap<String, Object> data = manager.gets(table, new String[]{"INVENTORY", "ENDERCHEST", "POTIONEFFECT"}, "ID", player.getUniqueId().toString(), new Class[]{Blob.class, Blob.class, Blob.class});

            Blob inventory = (Blob) data.get("INVENTORY");
            player.getInventory().setContents(ItemStackConvertor.deserialize(inventory.getBinaryStream().readAllBytes()));

            Blob enderchest = (Blob) data.get("ENDERCHEST");
            player.getEnderChest().setContents(ItemStackConvertor.deserializeEnderChest(enderchest.getBinaryStream().readAllBytes())[0]);

            Blob effects = (Blob) data.get("POTIONEFFECT");
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            Bukkit.broadcastMessage(EffectsConvertor.deserialize(effects.getBinaryStream().readAllBytes()).toString());
            Bukkit.broadcastMessage(String.valueOf(player.addPotionEffects(EffectsConvertor.deserialize(effects.getBinaryStream().readAllBytes()))));

        } catch (Exception e) {
            System.out.println("PSI data not found for player " + player.getDisplayName());
            return;
        }
    }

    public void removePlayer(Player player) {
        manager.deleteRow(table, "ID", player.getUniqueId().toString());
    }
}