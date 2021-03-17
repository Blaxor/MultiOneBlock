package ro.deiutzblaxo.schematicsave;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Schematicsave extends JavaPlugin {

    public static Schematicsave instance;
    int period = 0;
    @Override
    public void onEnable() {
        instance = this;

// ------ spigot things
        getCommand("saveschematics").setExecutor(new SaveSchematicCommand());
        // ------ data
        ConfigHandler.setupSettings();
        ConfigHandler.setupData();
        period = ConfigHandler.getSetting("period");
        SchematicHandler.skipped = ConfigHandler.getSetting("skipped");
        SchematicHandler.i = ConfigHandler.getSetting("position");
        SchematicHandler.loop = ConfigHandler.getSetting("loop");
        //-------- schematic things...
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() >= 10){
                    SchematicHandler.toggleStop(true);
                }else{
                    SchematicHandler.toggleStop(false);
                }
                if(SchematicHandler.isStopped())
                    return;
                ConfigHandler.setSettings("skipped", SchematicHandler.skipped);
                ConfigHandler.setSettings("position" , SchematicHandler.i);
                ConfigHandler.setSettings("loop" , SchematicHandler.loop);
                SchematicHandler.doSchematic();
            }
        },60*20,period*20);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
