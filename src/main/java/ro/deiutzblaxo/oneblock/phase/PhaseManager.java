package ro.deiutzblaxo.oneblock.phase;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.phase.objects.Phase;
import ro.deiutzblaxo.oneblock.phase.objects.RARITY;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;

public class PhaseManager {

    @Getter
    private TreeMap<Integer, Phase> phaseHashMap = new TreeMap<Integer, Phase>();

    public void populatePhases(){
        File file = new File(OneBlock.getInstance().getDataFolder() , "/phases/");
        if(!file.exists()){
            return;
        }
        File[] files = file.listFiles();
        OneBlock.getInstance().getLogger().log(Level.INFO,"before for for files");
        for(File phaseFile : files){

            FileConfiguration phaseConfig = YamlConfiguration.loadConfiguration(phaseFile);
            OneBlock.getInstance().getLogger().log(Level.INFO,file.getName());
            int count = phaseConfig.getInt("start");
            Phase phase = new Phase(count);
            OneBlock.getInstance().getLogger().log(Level.INFO,count+"");
            phaseHashMap.put(count,phase);
            for(String key : phaseConfig.getConfigurationSection("blocks").getKeys(false)){
                phase.addBlock(Material.getMaterial(key),phaseConfig.getInt("blocks."+key));
                OneBlock.getInstance().getLogger().log(Level.INFO,key);
            }
            if(phaseConfig.contains("mobs"))
            for(String key : phaseConfig.getConfigurationSection("mobs").getKeys(false)){
                phase.addMob(EntityType.valueOf(key),phaseConfig.getInt("mobs."+key));
            }
            if(phaseConfig.contains("chest")){
                for(String str : phaseConfig.getConfigurationSection("chest").getKeys(false)){//lista cu chesturi
                    HashMap<Integer, ItemStack> items = new HashMap<>();
                    for(String nr : phaseConfig.getConfigurationSection("chest."+str+".items.").getKeys(false)) // lista cu iteme
                    items.put(Integer.valueOf(nr),UTILS.deserial(phaseConfig.getString("chest."+str+".items."+nr)));
                    phase.addChest(items,RARITY.COMMON);
                }

            }
            phase.setPhaseName(phaseConfig.getString("name"));
            phase.setPhaseBiome(Biome.valueOf(phaseConfig.getString("biome")));
        }
    }
    public Phase getPhase(Integer block){
        return phaseHashMap.floorEntry(block).getValue();
        }
    public Phase getNexsPhase(Phase phase){
        return phaseHashMap.higherEntry(phase.getBlockNumber()).getValue();
    }
    public boolean isReady(Island island){
        if(getPhase(island.getCount()) != island.getPhase()){
            return true;
        }else {
            return false;
        }

    }

}
