package ro.deiutzblaxo.schematicsave;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;


import java.io.*;
import java.net.MalformedURLException;
import java.util.logging.Level;

public class SchematicHandler {
    static int  i = 0;
    static int skipped = 0;
    static int filesNr;
    static int loop = 0;
    static boolean stopped = false;

    /**
     * se ocupa de tot procesul de a lua insule , face schematic si salva. doar foloseste aceasta metoda pentru Betobox/database/Island
     */

    public static float getProcentageDone(){

        return (float)i * 100/ (float) filesNr;

    }
    public static float getSkipped(){
        return (float)skipped * 100 / (float) filesNr ;

    }
    public static boolean isStopped(){
        return stopped;
    }
    public static void toggleStop(){
        stopped = stopped ? false : true;
    }
    public static void toggleStop(boolean stat){
        stopped = stat;
    }


    public static void doSchematic(){
        File FolderDB = new File(Schematicsave.instance.getServer().getPluginManager().getPlugin("BentoBox").getDataFolder()+"/database/Island");
        if(!FolderDB.exists()) {
            System.out.println("The DB don`t exist");
            return;
        }

        File[] files = FolderDB.listFiles();
        if(files.length <= i) {
            loop++;
            skipped = 0;
            i = 0;
            return;
        }
        Island is = JsonReadFile.centerFromDB(files[i]);
        while(JsonReadFile.read(files[i],"deleted").contains("true")
                || is.uuidPlayers.length <= 0
                || is.owner.equalsIgnoreCase("Nothing here")) {

            Schematicsave.instance.getLogger().log(Level.INFO,"Skipped the island: " + is.uuidIsland);
            i++;
           is = JsonReadFile.centerFromDB(files[i]);
        skipped++;
        }

        Schematicsave.instance.getLogger().log(Level.INFO,"Started to save : " + is.owner);
        ConfigHandler.setData(is);
        SchematicHandler.saveSchematic(is.owner + ".schem", is.world,
                BlockVector3.at(is.min.getX(), is.min.getY(), is.min.getZ()),
                BlockVector3.at(is.max.getX(), is.max.getY(), is.max.getZ()));
        Schematicsave.instance.getLogger().log(Level.INFO,"Finished saveing : " + is.owner);
        filesNr = files.length;
        i++;

    }

    /**
     * creating schematic
     */
    public static void saveSchematic(String filename,World world , BlockVector3 pos1, BlockVector3 pos2){

        CuboidRegion region = new CuboidRegion(world, pos1, pos2);
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            // configure here
            forwardExtentCopy.setCopyingEntities(true);

            Operations.complete(forwardExtentCopy);


            System.out.println(filename);

            File dataDirectory = new File(Schematicsave.instance.getDataFolder(), "maps");
            if (!dataDirectory.exists())
                dataDirectory.mkdirs();
            File file = new File(dataDirectory, filename); // The schematic file

            try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {

                writer.write(clipboard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clipboard = null;

    }

}