package ro.deiutzblaxo.schematicsave;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

import java.io.File;
import java.util.logging.Level;

public class Island {

    public Location center;
    public Location min;
    public Location max;
    public String uuidIsland;
    public String[] uuidPlayers;
    public String owner;
    public World world;


    public Island(org.bukkit.Location center, String uuidIsland , String[] uuidPlayers , String owner){

        this.center = BukkitAdapter.adapt(center);
        this.uuidIsland = uuidIsland;
        try {
            this.owner = owner;
            this.uuidPlayers = new String[uuidPlayers.length];
            for (int i = 0; i < uuidPlayers.length; i++) {
                this.uuidPlayers[i] = uuidPlayers[i].split(":")[0];
            }

        }catch (NullPointerException exception){
            Schematicsave.instance.getLogger().log(Level.WARNING , "S-a prins un NullPointerException. Nu exista membrii/owner pe aceasta insula");
        this.owner = "Nothing here";
        this.uuidPlayers = new String[0];
        }

        world = BukkitAdapter.adapt(center.getWorld());


        org.bukkit.Location min = center.clone().subtract(200,0,200);
        min.setY(0);
        this.min = BukkitAdapter.adapt(min);

        org.bukkit.Location max = center.clone().add(200,0,200);
        max.setY(255);
        this.max = BukkitAdapter.adapt(max);

    }

}
