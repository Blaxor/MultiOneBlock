package ro.deiutzblaxo.oneblock.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.util.logging.Level;

public class ChunkUtils {

    public static void changeBiome(World world,Biome biome,int radius_){
        Bukkit.getScheduler().runTaskAsynchronously(OneBlock.getInstance(), () -> {
            OneBlock.getInstance().getLogger().log(Level.INFO,"Changing the biome for world : " + world.getName());
            int radius = radius_;
            world.setBiome(0,0,biome);
            for(int i = radius*-1 ; i < radius ; i ++) {
                for (int j = radius*-1; j < radius; j++) {
                    world.setBiome(i,j,biome);
                }
            }
            OneBlock.getInstance().getLogger().log(Level.INFO,"Changed the biome for world : " + world.getName());
        });

    }
}
