package ro.deiutzblaxo.oneblock.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.IslandMeta;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ChunkUtils {

    private static final List<World> changingWorlds = new ArrayList<>();

    public static void changeBiome(OneBlock plugin, World world, Biome biome, int radius_) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            changingWorlds.add(world);
            plugin.getLogger().log(Level.INFO, "Changing the biome for world : " + world.getName());
            int radius = radius_;
            //world.setBiome(0,0,biome);
            for (int i = radius * -1; i < radius; i++) {
                for (int j = radius * -1; j < radius; j++) {

                    world.setBiome(i, j, biome);

                }
            }
            plugin.getLogger().log(Level.INFO, "Changed the biome for world : " + world.getName());
            changingWorlds.remove(world);
        });

    }

    public static void changeBiome(OneBlock plugin, Island island) {
        IslandMeta meta = island.getMeta();
        if (!(isSameBiome(island, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()), BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()))
                && isSameBiome(island, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()) * -1, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()))
                && isSameBiome(island, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()), BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()) * -1)
                && isSameBiome(island, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()) * -1, BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()) * -1)) && !changingWorlds.contains(island.getBukkitWorld())) {
            ChunkUtils.changeBiome(plugin, island.getBukkitWorld(), island.getPhase().getPhaseBiome(), BorderHandler.getRadius(meta.getRadiusType(), meta.getRadiusTire()) + 10);
        }
    }

    private static boolean isSameBiome(Island island, int x, int z) {
        return island.getBukkitWorld().getBiome(x, z).equals(island.getPhase().getPhaseBiome());

    }
}
