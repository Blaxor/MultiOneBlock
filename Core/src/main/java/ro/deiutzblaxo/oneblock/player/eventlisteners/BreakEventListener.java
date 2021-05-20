package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.phase.events.ChangePhaseEvent;
import ro.deiutzblaxo.oneblock.phase.objects.PhaseObject;
import ro.deiutzblaxo.oneblock.utils.UTILS;

import java.util.*;

public class BreakEventListener implements Listener {
    private static final Map<Material, Integer> TOOLS;

    static {
        Map<Material, Integer> t = new EnumMap<>(Material.class);
        t.put(Material.DIAMOND_AXE, 1);
        t.put(Material.DIAMOND_SHOVEL, 1);
        t.put(Material.DIAMOND_PICKAXE, 1);
        t.put(Material.IRON_AXE, 1);
        t.put(Material.IRON_SHOVEL, 1);
        t.put(Material.IRON_PICKAXE, 1);
        t.put(Material.WOODEN_AXE, 1);
        t.put(Material.WOODEN_SHOVEL, 1);
        t.put(Material.WOODEN_PICKAXE, 1);
        t.put(Material.GOLDEN_AXE, 1);
        t.put(Material.GOLDEN_SHOVEL, 1);
        t.put(Material.GOLDEN_PICKAXE, 1);
        t.put(Material.STONE_AXE, 1);
        t.put(Material.STONE_SHOVEL, 1);
        t.put(Material.STONE_PICKAXE, 1);
        t.put(Material.SHEARS, 1);
        t.put(Material.DIAMOND_SWORD, 2);
        t.put(Material.GOLDEN_SWORD, 2);
        t.put(Material.STONE_SWORD, 2);
        t.put(Material.IRON_SWORD, 2);
        t.put(Material.WOODEN_SWORD, 2);
        t.put(Material.TRIDENT, 2);
        TOOLS = Collections.unmodifiableMap(t);
    }

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
            event.getPlayer().sendMessage(plugin.getLangManager().get(event.getPlayer(), MESSAGE.ISLAND_BREAK_NOT_ALLOW));
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
            block.getWorld().dropItem(block.getLocation().add(.5, 1.5, .5), itemStack);

        }
        block.getDrops().clear();
        if (event.getPlayer().getInventory().getItemInMainHand() != null)
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR)
                damageTool(event.getPlayer());

        if (block.getType().equals(Material.CHEST))
            if (island.getBlock(event.getBlock().getLocation()) != null)
                event.getPlayer().teleport(event.getPlayer().getLocation().add(0, 0.2, 0));

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
            destoryNear(block);
            block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), object.getEntityType());
            return;
        }


        event.setCancelled(true);

    }

    private void destoryNear(Block block) {
        Location location = block.getLocation().add(0,1,0);
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 0; y <= 3; y++) {
                    Location loc = location.clone();
                    loc.add(x, y, z);
                    if (loc.getBlock() != null)
                        if (loc.getBlock().getType() != Material.AIR)
                            loc.getBlock().breakNaturally();
                }

    }

    private final Random random = new Random();

    private void damageTool(Player player) {
        ItemStack inHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = inHand.getItemMeta();

        if (itemMeta instanceof Damageable && !itemMeta.isUnbreakable() && TOOLS.containsKey(inHand.getType())) {
            Damageable meta = (Damageable) itemMeta;
            // Get the item's current durability
            Integer durability = meta.getDamage();
            // Get the damage this will do
            int damage = TOOLS.get(inHand.getType());
            if (durability != null) {
                // Check for DURABILITY
                if (itemMeta.hasEnchant(Enchantment.DURABILITY)) {
                    int level = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
                    if (random.nextInt(level + 1) == 0) {
                        meta.setDamage(durability + damage);
                    }
                } else {
                    meta.setDamage(durability + damage);
                }
                if (meta.getDamage() > inHand.getType().getMaxDurability()) {
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
                    player.getInventory().setItemInMainHand(null);
                } else {
                    inHand.setItemMeta(itemMeta);
                }
            }
        }

    }


}
