package ro.deiutzblaxo.oneblock.slimemanager;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;

public class WorldUtil {

    /**
     * Copies the file structure of a world and imports it to another file.
     *
     * @param source the original world which we have to copy
     * @param target the target destination to which files have to go
     */
    /*private static ExecutorService pool = Executors.newCachedThreadPool();*/
    public static void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes every file under a certain path
     *
     * @param path parent file
     * @return
     */
    public static void deleteFile(File path) {
        try {
            FileUtils.deleteDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveSlimeWorld(OneBlock plugin, SlimeWorld world) {
        /*pool.submit(() -> {*/
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("mysql");
        plugin.getLogger().log(Level.INFO, "saving THE SLIME WORLD : " + world.getName());

            Bukkit.getWorld(world.getName()).save();



        /*});*/
    }

    public static void deleteSlimeWorld(OneBlock plugin, SlimeWorld world) {
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("mysql");
        plugin.getLogger().log(Level.INFO, "deleting THE SLIME WORLD : " + world.getName());

        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm delete " + world.getName()), 3);

    }

    public static void unloadSlimeWorld(OneBlock plugin, World world) {
        world.save();
        try {
            plugin.getLoader().unlockWorld(world.getName());
        } catch (UnknownWorldException e) {

        } catch (IOException e) {

        }
        plugin.getLogger().log(Level.INFO, world.getName() + "has been unloaded : " + Bukkit.unloadWorld(world, true));

    }

    /**
     * Loading in cache the slimeWorld
     *
     * @param name UUID world
     * @return the slime world
     */
    public static SlimeWorld loadSlimeWorld(OneBlock plugin, String name, Island island) {

        try {
            SlimeWorld world = plugin.getSlimePlugin().loadWorld(plugin.getLoader(), name, false, getSlimePropertyMap(island));
            plugin.getSlimePlugin().generateWorld(world);
            return world;

        } catch (UnknownWorldException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CorruptedWorldException e) {
            e.printStackTrace();
        } catch (NewerFormatException e) {
            e.printStackTrace();
        } catch (WorldInUseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the property map for a Slime World.
     * Change it as you wish.
     *
     * @return {@link SlimePropertyMap}
     */
    public static SlimePropertyMap getSlimePropertyMap(Island island) {
        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, true);
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, true);
        slimePropertyMap.setString(SlimeProperties.DIFFICULTY, "normal");

        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, (int) island.getMeta().getSpawn().getX());
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, (int) island.getMeta().getSpawn().getY());
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, (int) island.getMeta().getSpawn().getZ());

        slimePropertyMap.setString(SlimeProperties.ENVIRONMENT, "normal");
        slimePropertyMap.setString(SlimeProperties.DEFAULT_BIOME, island.getPhase().getPhaseBiome().name().toLowerCase(Locale.ROOT));


        return slimePropertyMap;
    }

 /*   public static byte[] convertToByte(SlimeWorld world) {

        CraftSlimeWorld worlds = (CraftSlimeWorld) world;
        return worlds.serialize();
    }

    public static CraftSlimeWorld convertToSlimeworld(byte[] world) throws IOException {


        ByteArrayInputStream out = new ByteArrayInputStream(world);
        ObjectInputStream obj = new ObjectInputStream(out); // aici <-----------
        try {
            return (CraftSlimeWorld) obj.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }*/

/*    public static void generateNewWorld(String world_name){
        initAsyncGeneration(generateEmptyWorld(world_name));
    }

    public static World generateEmptyWorld(String world_name) {
        WorldCreator worldCreator = getWorldCreator(world_name);
       return worldCreator.createWorld();

    }

    private static WorldCreator getWorldCreator(String world_name) {
        WorldCreator worldCreator = new WorldCreator(world_name);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("2;0;1");
        return worldCreator;
    }

    private static void initAsyncGeneration( World world) {
        CompletableFuture.runAsync(() -> WorldUtil.importSlimeWorld(world.getName()))
                .thenRun(() -> {
                    try {
                        WorldUtil.loadSlimeWorld(world.getName());
                    } catch (UnknownWorldException exception) {
                        exception.printStackTrace();
                    } catch (WorldInUseException e) {
                        e.printStackTrace();
                    }
                })
                .thenRun(() -> world.setSpawnLocation( 0,100,0));
    }*/


    /**
     * Pastes a schematic at a given location.
     * Operation might be laggy, but we've made it
     * async via FAWE.
     *
     * @param file     File which contains the schematic.
     * @param location Location at which the schematic should be pasted.
     */
/*    public static boolean pasteSchematic(File file, Location location) {
        TaskManager.IMP.async(() -> {
            try {
                Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1);
                MCEditSchematicFormat.getFormat(file).load(file).paste(editSession, vector, false);
            } catch (MaxChangedBlocksException | IOException | DataException | NullPointerException e) {
                e.printStackTrace();
                EFTLobby.getInstance().logSevere("A NullPointerException has occurred while attempting to paste in the schematic for file " + file.getName() + ".");
                EFTLobby.getInstance().logSevere("Chunks are being re-allocated.");
            }
        });
        return true;
    }*/


}
