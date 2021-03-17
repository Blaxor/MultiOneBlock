package ro.deiutzblaxo.schematicsave;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class ConfigHandler {

   private static File configFile;
   private static FileConfiguration configurationFile;
   private static File settingsFile;
   private static FileConfiguration configurationSettings;

   public static void setupSettings(){
       settingsFile = new File(Schematicsave.instance.getDataFolder(), "settings.yml");
       if(!Schematicsave.instance.getDataFolder().exists()) {
           Schematicsave.instance.getDataFolder().mkdir();
       }
       if(!settingsFile.exists()){

           try {
               settingsFile.createNewFile();
           } catch (IOException e) {
               e.printStackTrace();
           }
           setSettings("period",60);
       }

       configurationSettings = YamlConfiguration.loadConfiguration(settingsFile);

   }
   public static void setSettings(String path , int i){
       configurationSettings = YamlConfiguration.loadConfiguration(settingsFile);
       configurationSettings.set(path,i);
       try {
           configurationSettings.save(settingsFile);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   public static int getSetting(String path){
       configurationSettings = YamlConfiguration.loadConfiguration(settingsFile);
    return configurationSettings.contains(path) ? configurationSettings.getInt(path) : 0;
   }

    public static void setupData(){
        configFile = new File(Schematicsave.instance.getDataFolder(), "data.yml");
        if(!Schematicsave.instance.getDataFolder().exists()) {
            Schematicsave.instance.getDataFolder().mkdir();
        }
        if(!configFile.exists()){

            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configurationFile = YamlConfiguration.loadConfiguration(configFile);

    }
    public static FileConfiguration getData(){
        return configurationFile;
    }


    public static void setData(Island island){

        configurationFile.set(island.owner+".members",island.uuidPlayers);
        configurationFile.set(island.owner + ".oldUUIDIslandBB", island.uuidIsland);

        try {
            configurationFile.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configurationFile = YamlConfiguration.loadConfiguration(configFile);


    }


}
