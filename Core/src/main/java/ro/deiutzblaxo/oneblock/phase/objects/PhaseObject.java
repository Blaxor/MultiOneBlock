package ro.deiutzblaxo.oneblock.phase.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class PhaseObject {

    private EntityType entityType;
    private Material material;
    private Map<Integer, ItemStack> chest;
    private RARITY rarity;
    @Setter
    private int prob;

    public PhaseObject(EntityType type , int prob){
        this.entityType = type;
        this.prob=prob;
    }
    public PhaseObject(Material material , int prob){
        this.material = material;
        this.prob = prob;
    }
    public PhaseObject(Map<Integer,ItemStack> chest , RARITY rarity){
        this.chest = chest;
        this.rarity = rarity;
    }
    public PhaseObject(PhaseObject phase){
        this.prob = phase.prob;
        this.chest = phase.chest;
        this.rarity = phase.rarity;
        this.material = phase.material;
        this.entityType = phase.entityType;

    }
    public boolean isEntity(){
        return entityType != null;
    }
    public boolean isChest(){
        return chest != null;
    }
    public boolean isBlock(){
        return material != null;
    }

}
