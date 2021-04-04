package ro.deiutzblaxo.oneblock.phase.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;

import java.util.*;
import java.util.logging.Level;
@Getter
public class Phase {
    protected static final SortedMap<Double, RARITY> CHEST_CHANCES = new TreeMap<>();
    static {
        CHEST_CHANCES.put(0.62D, RARITY.COMMON);
        CHEST_CHANCES.put(0.87D, RARITY.UNCOMMON);
        CHEST_CHANCES.put(0.96D, RARITY.RARE);
        CHEST_CHANCES.put(1D, RARITY.EPIC);
    }
    /**
     * Tree map of all materials and their probabilities as a ratio to the sum of all probabilities
     */
    private final TreeMap<Integer, PhaseObject> probMap = new TreeMap<>();
    /**
     * Sum of all probabilities
     */
    private int total = 0;
    @Setter
    private String phaseName;
    @Setter
    private Biome phaseBiome;
    @Setter
    private World.Environment environment;
    @Setter
    private PhaseObject firstBlock;
    private final Map<RARITY, List<PhaseObject>> chests = new EnumMap<>(RARITY.class);
    private final Random random = new Random();
    private final int blockNumber;

    private int blockTotal = 0;
    private int entityTotal = 0;
    private List<String> startCommands;//not implemented
    private List<String> endCommands;// not implemented
    private boolean reset = false;
   //todo private List<Requirement> requirements;

   public Phase(int blockNumber,PhaseObject firstBlock) {
       this.blockNumber = blockNumber;
       startCommands = new ArrayList<>();
       endCommands = new ArrayList<>();
       this.firstBlock = firstBlock;
       //requirements = new ArrayList<>();
   }
    /**
     * Adds a material and associated probability
     * @param material - Material
     * @param prob - probability
     */
    public void addBlock(Material material, int prob) {
        total += prob;
        blockTotal += prob;
        probMap.put(total, new PhaseObject(material, prob));
    }

    /**
     * Adds an entity type and associated probability
     * @param entityType - entityType
     * @param prob - probability
     */
    public void addMob(EntityType entityType, int prob) {
        total += prob;
        entityTotal += prob;
        probMap.put(total, new PhaseObject(entityType, prob));
    }

    public void addChest(Map<Integer, ItemStack> items, RARITY rarity) {
        chests.computeIfAbsent(rarity, k -> new ArrayList<>()).add(new PhaseObject(items, rarity));
    }
    public PhaseObject getNextBlock(OneBlock addon) {
        if (total <1) {
            addon.getLogger().log(Level.SEVERE,"Phase " + this.getPhaseName() + " has zero probability of generating blocks. Check config file. Is the block section missing?");
            return this.getFirstBlock() != null ? getFirstBlock() : new PhaseObject(Material.GRASS_BLOCK,1);
        }
        PhaseObject block = getRandomBlock(probMap, total);
        if (block.isEntity()) return block;
        return block.getMaterial().equals(Material.CHEST) && !chests.isEmpty() ? getRandomChest() : block;
    }

    private PhaseObject getRandomChest() {
        // Get the right type of chest
        RARITY r = CHEST_CHANCES.getOrDefault(((TreeMap<Double, RARITY>) CHEST_CHANCES).ceilingKey(random.nextDouble()), RARITY.COMMON);
        // If the chest lists have no common fallback, then return empty chest
        if (!chests.containsKey(r) && !chests.containsKey(RARITY.COMMON)) return new PhaseObject(Material.CHEST, 0);
        // Get the rare chest or worse case the common one
        List<PhaseObject> list = chests.containsKey(r) ? chests.get(r) : chests.get(RARITY.COMMON);
        // Pick one from the list or return an empty chest. Note list.get() can return nothing
        return list.isEmpty() ? new PhaseObject(Material.CHEST, 0) : list.get(random.nextInt(list.size()));
    }

    private PhaseObject getRandomBlock(TreeMap<Integer, PhaseObject> probMap2, int total2) {
        // Use +1 on the bound because the random choice is exclusive
        PhaseObject temp = probMap2.get(random.nextInt(total2+1));
        if (temp == null) {
            temp = probMap2.ceilingEntry(random.nextInt(total2+1)).getValue();
        }
        if (temp == null) {
            temp = probMap2.firstEntry().getValue();
        }
        return new PhaseObject(temp);
    }

    public boolean isReset() {
        return reset;
    }
    public void setReset(boolean bol){
        reset = bol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phase phase = (Phase) o;
        return phaseName.equals(phase.phaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phaseName);
    }
}
