package ro.deiutzblaxo.oneblock.player.eventlisteners;

import org.bukkit.*;
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
import ro.deiutzblaxo.oneblock.effects.MobAspects;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;
import ro.deiutzblaxo.oneblock.langs.MESSAGE;
import ro.deiutzblaxo.oneblock.phase.events.ChangePhaseEvent;
import ro.deiutzblaxo.oneblock.phase.objects.PhaseObject;
import ro.deiutzblaxo.oneblock.phase.objects.RARITY;
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

    private static final Map<EntityType, MobAspects> MOB_ASPECTS;

    static {
        Map<EntityType, MobAspects> m = new EnumMap<>(EntityType.class);
        m.put(EntityType.ZOMBIE, new MobAspects(Sound.ENTITY_ZOMBIE_AMBIENT, Color.fromRGB(74, 99, 53)));
        m.put(EntityType.CREEPER, new MobAspects(Sound.ENTITY_CREEPER_PRIMED, Color.fromRGB(125, 255, 106)));
        m.put(EntityType.SKELETON, new MobAspects(Sound.ENTITY_SKELETON_AMBIENT, Color.fromRGB(211, 211, 211)));
        m.put(EntityType.DROWNED, new MobAspects(Sound.ENTITY_DROWNED_AMBIENT, Color.fromRGB(109, 152, 144)));
        m.put(EntityType.BLAZE, new MobAspects(Sound.ENTITY_BLAZE_AMBIENT, Color.fromRGB(238, 211, 91)));
        m.put(EntityType.CAVE_SPIDER, new MobAspects(Sound.ENTITY_SPIDER_AMBIENT, Color.fromRGB(63, 37, 31)));
        m.put(EntityType.SPIDER, new MobAspects(Sound.ENTITY_SPIDER_AMBIENT, Color.fromRGB(94, 84, 73)));
        m.put(EntityType.EVOKER, new MobAspects(Sound.ENTITY_EVOKER_AMBIENT, Color.fromRGB(144, 148, 148)));
        m.put(EntityType.GHAST, new MobAspects(Sound.ENTITY_GHAST_AMBIENT, Color.fromRGB(242, 242, 242)));
        m.put(EntityType.HUSK, new MobAspects(Sound.ENTITY_HUSK_AMBIENT, Color.fromRGB(111, 104, 90)));
        m.put(EntityType.ILLUSIONER, new MobAspects(Sound.ENTITY_ILLUSIONER_AMBIENT, Color.fromRGB(144, 149, 149)));
        m.put(EntityType.RAVAGER, new MobAspects(Sound.ENTITY_RAVAGER_AMBIENT, Color.fromRGB(85, 78, 73)));
        m.put(EntityType.SHULKER, new MobAspects(Sound.ENTITY_SHULKER_AMBIENT, Color.fromRGB(142, 106, 146)));
        m.put(EntityType.VEX, new MobAspects(Sound.ENTITY_VEX_AMBIENT, Color.fromRGB(137, 156, 176)));
        m.put(EntityType.WITCH, new MobAspects(Sound.ENTITY_WITCH_AMBIENT, Color.fromRGB(56, 39, 67)));
        m.put(EntityType.STRAY, new MobAspects(Sound.ENTITY_STRAY_AMBIENT, Color.fromRGB(118, 132, 135)));
        m.put(EntityType.GUARDIAN, new MobAspects(Sound.ENTITY_GUARDIAN_AMBIENT, Color.fromRGB(201, 143, 113)));
        m.put(EntityType.ELDER_GUARDIAN, new MobAspects(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, Color.fromRGB(201, 143, 113)));
        m.put(EntityType.BAT, new MobAspects(Sound.ENTITY_BAT_AMBIENT, Color.fromRGB(61, 30, 30)));
        m.put(EntityType.CHICKEN, new MobAspects(Sound.ENTITY_CHICKEN_AMBIENT, Color.fromRGB(249, 233, 176)));
        m.put(EntityType.PIG, new MobAspects(Sound.ENTITY_PIG_AMBIENT, Color.fromRGB(240, 122, 205)));
        m.put(EntityType.COW, new MobAspects(Sound.ENTITY_COW_AMBIENT, Color.fromRGB(71, 58, 2)));
        m.put(EntityType.CAT, new MobAspects(Sound.ENTITY_CAT_AMBIENT, Color.fromRGB(145, 120, 40)));
        m.put(EntityType.HORSE, new MobAspects(Sound.ENTITY_HORSE_AMBIENT, Color.fromRGB(159, 84, 65)));
        m.put(EntityType.DONKEY, new MobAspects(Sound.ENTITY_DONKEY_AMBIENT, Color.fromRGB(120, 120, 122)));
        m.put(EntityType.LLAMA, new MobAspects(Sound.ENTITY_LLAMA_AMBIENT, Color.fromRGB(149, 118, 71)));
        m.put(EntityType.ENDERMAN, new MobAspects(Sound.ENTITY_ENDERMAN_AMBIENT, Color.fromRGB(88, 19, 225)));
        m.put(EntityType.VILLAGER, new MobAspects(Sound.ENTITY_VILLAGER_AMBIENT, Color.fromRGB(132, 90, 75)));
        m.put(EntityType.ZOMBIE_VILLAGER, new MobAspects(Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, Color.fromRGB(30, 43, 16)));
        m.put(EntityType.SLIME, new MobAspects(Sound.ENTITY_SLIME_SQUISH, Color.fromRGB(91, 162, 60)));
        m.put(EntityType.STRIDER, new MobAspects(Sound.ENTITY_STRIDER_AMBIENT, Color.fromRGB(110, 36, 38)));
        m.put(EntityType.WITHER_SKELETON, new MobAspects(Sound.ENTITY_WITHER_SKELETON_AMBIENT, Color.fromRGB(64, 71, 71)));
        m.put(EntityType.PIGLIN, new MobAspects(Sound.ENTITY_PIGLIN_AMBIENT, Color.fromRGB(217, 150, 180)));
        m.put(EntityType.ZOMBIFIED_PIGLIN, new MobAspects(Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, Color.fromRGB(230, 121, 116)));
        m.put(EntityType.PIGLIN_BRUTE, new MobAspects(Sound.ENTITY_PIGLIN_BRUTE_AMBIENT, Color.fromRGB(217, 150, 108)));
        m.put(EntityType.PILLAGER, new MobAspects(Sound.ENTITY_PILLAGER_AMBIENT, Color.fromRGB(71, 74, 74)));
        m.put(EntityType.IRON_GOLEM, new MobAspects(Sound.ENTITY_IRON_GOLEM_STEP, Color.fromRGB(160, 145, 138)));
        m.put(EntityType.SNOWMAN, new MobAspects(Sound.ENTITY_SNOW_GOLEM_AMBIENT, Color.fromRGB(255, 255, 255)));
        m.put(EntityType.WOLF, new MobAspects(Sound.ENTITY_WOLF_AMBIENT, Color.fromRGB(84, 81, 80)));
        m.put(EntityType.FOX, new MobAspects(Sound.ENTITY_FOX_AMBIENT, Color.fromRGB(142, 55, 13)));
        m.put(EntityType.RABBIT, new MobAspects(Sound.ENTITY_RABBIT_AMBIENT, Color.fromRGB(142, 55, 13)));
        m.put(EntityType.MAGMA_CUBE, new MobAspects(Sound.ENTITY_MAGMA_CUBE_SQUISH, Color.fromRGB(24, 0, 0)));
        m.put(EntityType.BEE, new MobAspects(Sound.ENTITY_BEE_HURT, Color.fromRGB(147, 7, 219)));
        m.put(EntityType.OCELOT, new MobAspects(Sound.ENTITY_OCELOT_AMBIENT, Color.fromRGB(238, 177, 98)));
        m.put(EntityType.ZOMBIE_HORSE, new MobAspects(Sound.ENTITY_ZOMBIE_HORSE_AMBIENT, Color.fromRGB(118, 184, 96)));
        m.put(EntityType.PARROT, new MobAspects(Sound.ENTITY_PARROT_AMBIENT, Color.fromRGB(87, 170, 195)));
        m.put(EntityType.POLAR_BEAR, new MobAspects(Sound.ENTITY_POLAR_BEAR_AMBIENT, Color.fromRGB(255, 255, 255)));
        m.put(EntityType.SALMON, new MobAspects(Sound.ENTITY_SALMON_AMBIENT, Color.fromRGB(108, 37, 36)));
        m.put(EntityType.TROPICAL_FISH, new MobAspects(Sound.ENTITY_TROPICAL_FISH_AMBIENT, Color.fromRGB(115, 69, 94)));
        m.put(EntityType.PANDA, new MobAspects(Sound.ENTITY_PANDA_AMBIENT, Color.fromRGB(38, 38, 48)));
        m.put(EntityType.SQUID, new MobAspects(Sound.ENTITY_SQUID_AMBIENT, Color.fromRGB(84, 109, 128)));
        m.put(EntityType.TURTLE, new MobAspects(Sound.ENTITY_TURTLE_AMBIENT_LAND, Color.fromRGB(70, 188, 73)));
        m.put(EntityType.COD, new MobAspects(Sound.ENTITY_COD_AMBIENT, Color.fromRGB(130, 99, 80)));
        m.put(EntityType.DOLPHIN, new MobAspects(Sound.ENTITY_DOLPHIN_AMBIENT, Color.fromRGB(188, 200, 216)));
        MOB_ASPECTS = Collections.unmodifiableMap(m);
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

    private Random random = new Random();

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
        }                                               //      1               -   4                                                       +1 )  *-1
        if (random.nextInt(9000) <= (island.getMeta().getRadiusTire() - (BorderHandler.getMaxTier(island.getMeta().getRadiusType()) + 1)) * -1) {
            block.getWorld().dropItem(block.getLocation().add(.5, 1.5, .5), BorderHandler.getItem());
            Bukkit.broadcastMessage(plugin.getLangManager().get(event.getPlayer(), MESSAGE.UPGRADE_TIER_DROPPED));
        }

        block.getDrops().clear();
        if (event.getPlayer().getInventory().getItemInMainHand() != null)
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR)
                damageTool(event.getPlayer());

        if (block.getType().equals(Material.CHEST))
            if (island.getBlock(event.getBlock().getLocation()) != null)
                event.getPlayer().teleport(event.getPlayer().getLocation().add(0, 0.2, 0));

        if (island.getPhaseObjectsQueue().isEmpty() || island.getPhaseObjectsQueue().size() < 5) {
            for (int i = island.getPhaseObjectsQueue().size(); i <= 5; i++) {
                island.getPhaseObjectsQueue().add(island.getPhase().getNextBlock(plugin));
            }
        }
        EntityType type = island.getNearestMob();
        if (type != null) {
            playMobWarning(type, block);
        }


        PhaseObject object = island.getPhaseObjectsQueue().remove();

        if (object.isBlock()) {
            block.setType(object.getMaterial());
            return;
        }
        if (object.isChest()) {
            playChestWarning(object.getRarity(), block);
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();
            chest.getBlockInventory().clear();
            for (Integer i : object.getChest().keySet())
                chest.getBlockInventory().setItem(i, object.getChest().get(i));
            return;
        }
        if (object.isEntity()) {
            destoryNear(block);
            block.getWorld().spawnEntity(block.getLocation().add(0.5, 1, 0.5), object.getEntityType());
            return;
        }


        event.setCancelled(true);

    }

    private void destoryNear(Block block) {
        Location location = block.getLocation().add(0, 1, 0);
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 0; y <= 3; y++) {
                    Location loc = location.clone();
                    loc.add(x, y, z);
                    if (loc.getBlock() != null)
                        if (loc.getBlock().getType() != Material.AIR) {
                            loc.getBlock().breakNaturally();
                            block.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(0.5, 0, 0.5), 15, 0.5, 0.5, 0.5, 0, null, false);
                        }

                }

    }


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

    private void playMobWarning(EntityType entity, Block block) {

        if (!MOB_ASPECTS.containsKey(entity))
            return;
        block.getWorld().playSound(block.getLocation(), MOB_ASPECTS.get(entity).getSound(), 1, 1);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1, 0.5), 10, 0.5, 0, 0.5,
                new Particle.DustOptions(MOB_ASPECTS.get(entity).getColor(), 1));
    }

    private void playChestWarning(RARITY rarity, Block block) {
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1, 0.5), 30, 0.5, 0, 0.5,
                new Particle.DustOptions(rarity.color, 1));
    }


}
