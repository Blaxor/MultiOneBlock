package ro.deiutzblaxo.oneblock.phase;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.phase.objects.PhaseObject;
import ro.deiutzblaxo.oneblock.phase.objects.RARITY;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.Level;

public class PhaseManager {

    @Getter
    private final TreeMap<Integer, Phase> phaseHashMap = new TreeMap<Integer, Phase>();
    private final OneBlock plugin;

    public PhaseManager(OneBlock plugin) {
        this.plugin = plugin;

    }


    public void populatePhases() {
        phaseHashMap.clear();
        File file = new File(plugin.getDataFolder(), "/phases/");
        if (!file.exists()) {
            plugin.saveResource("phases/", true);
            plugin.saveResource("phases/plains0.yml", true);
        }
        File[] files = file.listFiles();
        for (File phaseFile : files) {

            FileConfiguration phaseConfig = YamlConfiguration.loadConfiguration(phaseFile);
            plugin.getLogger().log(Level.INFO, phaseFile.getName() + " loading!");
            int count = phaseConfig.getInt("start");

            PhaseObject firstBlock = new PhaseObject(Material.valueOf(phaseConfig.getString("firstblock").toUpperCase(Locale.ROOT)), 9999999);
            Phase phase = new Phase(count, firstBlock);


            phaseHashMap.put(count, phase);
            for (String key : phaseConfig.getConfigurationSection("blocks").getKeys(false)) {
                phase.addBlock(Material.getMaterial(key.toUpperCase(Locale.ROOT)), phaseConfig.getInt("blocks." + key));

            }
            if (phaseConfig.contains("reset"))
                phase.setReset(phaseConfig.getBoolean("reset"));

            if (phaseConfig.contains("mobs"))
                for (String key : phaseConfig.getConfigurationSection("mobs").getKeys(false)) {
                    phase.addMob(EntityType.valueOf(key.toUpperCase(Locale.ROOT)), phaseConfig.getInt("mobs." + key));
                }
            if (phaseConfig.contains("chest")) {
                for (String str : phaseConfig.getConfigurationSection("chest").getKeys(false)) {//lista cu chesturi
                    HashMap<Integer, ItemStack> items = new HashMap<>();
                    for (String nr : phaseConfig.getConfigurationSection("chest." + str + ".items.").getKeys(false)) // lista cu iteme
                        items.put(Integer.valueOf(nr),  phaseConfig.getItemStack("chest." + str + ".items." + nr));
                    phase.addChest(items, RARITY.valueOf(phaseConfig.contains("chest." + str + ".rarity") ? phaseConfig.getString("chest." + str + ".rarity") : RARITY.COMMON.name()));
                }

            }

            phase.setPhaseName(phaseConfig.getString("name"));
            phase.setPhaseBiome(Biome.valueOf(phaseConfig.getString("biome").toUpperCase(Locale.ROOT)));

        }
    }

    public void saveChest(String phase, String identifierChest, RARITY rarity, Chest chest) throws Exception {
        File file = new File(plugin.getDataFolder(), "/phases/" + phase + ".yml");
        if (!file.exists())
            throw new Exception("Phase file don`t exist!");
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i < chest.getBlockInventory().getContents().length; i++) {
            ItemStack item = chest.getBlockInventory().getContents()[i];
            if (item == null)
                continue;
            if (item.getType() == Material.AIR)
                continue;

            fileConfig.set("chest." + identifierChest + ".items." + i, item);
        }
        fileConfig.set("chest." + identifierChest + ".rarity", rarity.name());
        fileConfig.save(file);
        populatePhases();
    }

    public Phase getPhase(Integer block) {
        return phaseHashMap.floorEntry(block).getValue();
    }

    public Phase getNextPhase(Phase phase) {
        Phase phaseNext = phaseHashMap.higherEntry(phase.getBlockNumber()).getValue();
        if (phaseNext.isReset())
            return getPhase(0);
        else return phaseNext;
    }

    public boolean isReady(Island island) {
        return getPhase(island.getMeta().getCount()) != island.getPhase() && !island.isLocked();


    }

}
