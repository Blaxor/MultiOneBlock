package ro.deiutzblaxo.oneblock.phase;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.phase.events.ChangePhaseEvent;
import ro.deiutzblaxo.oneblock.phase.objects.PhaseObject;

import java.util.Collection;
import java.util.logging.Level;

public class BreakEvent implements Listener {

    private OneBlock plugin;

    public BreakEvent(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
        if(island == null){
            return;
        }
        if (!island.getMiddleBlock().getLocation().equals(event.getBlock().getLocation())) {
            return;
        }
        process(island,event.getBlock());
        event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFallingBlockSpawn(EntitySpawnEvent event) {
        if (!event.getEntityType().equals(EntityType.FALLING_BLOCK) ) {
            return;
        }
        Location l = event.getLocation();
        Island isl = plugin.getIslandManager().getIsland(event.getLocation().getWorld().getName());
        if(isl == null)
            return;
        Location island = isl.getMiddleBlock().getLocation();
        if(l.getBlockX() == island.getBlockX() && l.getBlockY() == island.getBlockY() && l.getBlockZ() == island.getBlockZ() ){
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent event){
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
        if(island == null)
            return;
        if(!island.getMiddleBlock().getLocation().equals(event.getBlock().getLocation()) || island == null){
            return;
        }
        event.setCancelled(true);
    }

    public void process(Island island, Block block) {
        island.getMeta().setCount(island.getMeta().getCount()+1);

        if (plugin.getPhaseManager().isReady(island))
        Bukkit.getPluginManager().callEvent(new ChangePhaseEvent(plugin,island));

        Collection<ItemStack> list = block.getDrops();
        for(ItemStack itemStack : list){
            if(itemStack.getType() == Material.AIR)
                continue;
            block.getWorld().dropItem(block.getLocation().add(.5,1,.5),itemStack);

        }
        block.getDrops().clear();


        PhaseObject object = island.getPhase().getNextBlock(plugin);
        if (object.isBlock()) {
            block.setType(object.getMaterial());
            return;
        }
        if (object.isChest()) {
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();
            chest.getBlockInventory().clear();
            for (Integer i : object.getChest().keySet())
                chest.getBlockInventory().setItem(i, object.getChest().get(i));
            return;
        }
        if(object.isEntity()){
            block.getWorld().spawnEntity(block.getLocation().add(0,1,0),object.getEntityType());
            return;
        }



    }


}
