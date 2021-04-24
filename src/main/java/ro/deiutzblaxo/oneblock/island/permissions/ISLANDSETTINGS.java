package ro.deiutzblaxo.oneblock.island.permissions;

import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public enum ISLANDSETTINGS implements Listener {

    FIRE_SPREAD(false),
    PVP(false),
    BLOCK_BURN(false),
    DECAY(true),
    CREEPER_EXPLOSION(false),
    TNT_EXPLOSION(false),
    MOB_SPAWNING(true),
    IGNITE(true);

    private boolean allowDefault;

    ISLANDSETTINGS(boolean allowDefault) {
        this.allowDefault = allowDefault;
    }
}
