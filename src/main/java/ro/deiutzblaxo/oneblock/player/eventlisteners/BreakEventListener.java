package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.phase.events.ChangePhaseEvent;
import ro.deiutzblaxo.oneblock.phase.objects.PhaseObject;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.Arrays;
import java.util.Collection;

public class BreakEventListener implements Listener {

    private final OneBlock plugin;

    public BreakEventListener(OneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
        if (island == null) {
            return;
        }
        if (!island.isAllow(event.getPlayer().getUniqueId(), PERMISSIONS.BREAK) && !event.getPlayer().hasPermission("oneblock.bypass.break")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(),MESSAGE.ISLAND_BREAK_NOT_ALLOW));
            return;
        }
        if (island.getBlock(event.getBlock().getLocation()) == null) {
            return;
        }
        process(island, event);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFallingBlockSpawn(EntitySpawnEvent event) {
        if (!event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            return;
        }
        Island island = plugin.getIslandManager().getIsland(event.getLocation().getWorld().getName());

        if (island == null)
            return;
        if (island.getBlock(UTILS.roundLocation(event.getLocation())) == null) {
            return;
        }
        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent event) {
        Island island = plugin.getIslandManager().getIsland(event.getBlock().getLocation().getWorld().getName());
        if (island == null)
            return;
        Block block = island.getBlock(event.getBlock().getLocation());
        if (block == null)
            return;
        event.setCancelled(true);
    }

    public void process(Island island, BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!island.isLocked())
            island.getMeta().setCount(island.getMeta().getCount() + 1);

        if (plugin.getPhaseManager().isReady(island))
            Bukkit.getPluginManager().callEvent(new ChangePhaseEvent(plugin, island));

        Collection<ItemStack> list = event.getBlock().getDrops(event.getPlayer().getItemInHand());
        if (event.getBlock().getType() == Material.CHEST) {
            list.addAll(Arrays.asList(((Chest) event.getBlock().getState()).getBlockInventory().getContents()));
            ((Chest) event.getBlock().getState()).getBlockInventory().clear();
        }
        for (ItemStack itemStack : list) {
            if (itemStack == null)
                continue;
            if (itemStack.getType() == Material.AIR)
                continue;
            block.getWorld().dropItem(block.getLocation().add(.5, 1, .5), itemStack);

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
        if (object.isEntity()) {
            block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), object.getEntityType());
            return;
        }


    }


}
