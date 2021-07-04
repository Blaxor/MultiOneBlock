package ro.deiutzblaxo.oneblock.menu.objects.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;
import ro.deiutzblaxo.oneblock.island.permissions.IslandSettingsManager;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.menu.objects.Button;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;

import java.util.List;
import java.util.logging.Level;

public class SettingButton implements Button {
    private Island island;
    private Object permissionOrSetting;

    @Override
    public void onClick(Player player, ClickType type) {


        if (permissionOrSetting instanceof PERMISSIONS) {
            switch (type) {
                case RIGHT:
                    IslandSettingsManager.downgradePermission(island, (PERMISSIONS) permissionOrSetting);
                    return;
                case LEFT:
                    IslandSettingsManager.upgradePermission(island, (PERMISSIONS) permissionOrSetting);
                    return;
                default:
                    return;
            }//TODO IMPLEMENTING THIS TYPE OF BUTTON


        }
        if (permissionOrSetting instanceof ISLANDSETTINGS) {
            IslandSettingsManager.toggleProtection(island, (ISLANDSETTINGS) permissionOrSetting);
            return;
        }
        OneBlock.getInstance().getLogger().log(Level.INFO, "No Permission or Setting type object, click ignored. (Button: " + getName() + ")");
        /*Button.super.onClick(player);*/

    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public ItemStack getItemDefault() {
        return null;
    }

    @Override
    public void setMaterial(Material material) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public List<String> getLore() {
        return null;
    }

    @Override
    public void setLore(List<String> lore) {

    }

    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public ButtonObject getButtonObject() {
        return null;
    }

    @Override
    public Menu getParent() {
        return null;
    }

    @Override
    public boolean isGlow() {
        return false;
    }
}
