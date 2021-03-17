package ro.deiutzblaxo.schematicsave;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveSchematicCommand implements CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("ss.permisie"))
            return false;
        if(args.length == 0){

            sender.sendMessage("S-a salvat " + SchematicHandler.getProcentageDone()
                    + "%"+"(LOOP " +SchematicHandler.loop+ ", "+SchematicHandler.i+"/"+SchematicHandler.filesNr+")"+" din ele. Si salvarea este " + (SchematicHandler.isStopped() ? "oprita":"pornita")+ ", au fost skipped " + SchematicHandler.getSkipped()+"%");
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("toggle")){
                sender.sendMessage(SchematicHandler.isStopped() ? "Salvarea a inceput." : "Salvarea a fost oprita.");
                SchematicHandler.toggleStop();
            }
        }
        return false;
    }
}
