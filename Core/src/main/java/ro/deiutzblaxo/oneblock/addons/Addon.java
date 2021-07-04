package ro.deiutzblaxo.oneblock.addons;

import com.leonardobishop.quests.Quests;
import fr.utarwyn.endercontainers.EnderContainers;
import fr.utarwyn.endercontainers.Managers;
import fr.utarwyn.endercontainers.enderchest.EnderChestManager;
import me.qKing12.RoyaleEconomy.RoyaleEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.player.events.PlayerLeaveIslandEvent;

public class Addon implements Listener {
    private OneBlock plugin;
    private Quests quests;
    private EnderContainers enderContainers;

    public Addon(OneBlock plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("Quests")) {
            quests = (Quests) plugin.getServer().getPluginManager().getPlugin("Quests");
        }

        if (plugin.getServer().getPluginManager().isPluginEnabled("EnderContainers")) {
            enderContainers = (EnderContainers) plugin.getServer().getPluginManager().getPlugin("EnderContainers");
        }
    }

    @EventHandler
    public void onLeave(PlayerLeaveIslandEvent event) {
        if (quests != null) {
            quests.getPlayerManager().getPlayer(event.getPlayerOBUUID()).getQuestProgressFile().clear();
        }
        if (plugin.getServer().getPluginManager().isPluginEnabled("RoyaleEconomy")) {
            try {
                RoyaleEconomy.apiHandler.balance.setBalance(event.getPlayerOBUUID().toString(), 0);
                RoyaleEconomy.apiHandler.balance.setBankBalance(event.getPlayerOBUUID().toString(), 0);
            } catch (Exception e) {

            }
/*            plugin.getDbManager().deleteRow("PersonalBank", "id", event.getPlayerOBUUID().toString());
            plugin.getDbManager().deleteRow("PlayerPurse", "id", event.getPlayerOBUUID().toString());*/


        }
        if (enderContainers != null) {
            EnderChestManager manager = Managers.get(EnderChestManager.class);
            manager.savePlayerContext(event.getPlayerOBUUID(), true);
        }


    }


}
