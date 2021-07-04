package ro.deiutzblaxo.oneblock.island.permissions;

import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.player.RANK;

public class IslandSettingsManager {

    public static void upgradePermission(Island island, PERMISSIONS permissions) {
        RANK value = island.getMeta().getPermissions().get(permissions) == null ? permissions.getLowestRankDefault() : island.getMeta().getPermissions().get(permissions);
        island.getMeta().getPermissions().put(permissions, value.getHigherRank(value));
    }

    public static void downgradePermission(Island island, PERMISSIONS permissions) {
        RANK value = island.getMeta().getPermissions().get(permissions) == null ? permissions.getLowestRankDefault() : island.getMeta().getPermissions().get(permissions);
        island.getMeta().getPermissions().put(permissions, value.getHigherRank(value));
    }

    public static void toggleProtection(Island island, ISLANDSETTINGS setting) {
        boolean value = island.getMeta().getSettings().get(setting) == null ? setting.isAllowDefault() : island.getMeta().getSettings().get(setting);
        island.getMeta().getSettings().put(setting, !value);
    }

}
