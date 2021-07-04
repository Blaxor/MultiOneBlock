package ro.deiutzblaxo.oneblock.effects;

import org.bukkit.Color;
import org.bukkit.Sound;

public class MobAspects {

    private final Sound sound;
    private final Color color;
    /**
     * @param sound
     * @param color
     */
    public MobAspects(Sound sound, Color color) {
        this.sound = sound;
        this.color = color;
    }
    /**
     * @return the sound
     */
    public Sound getSound() {
        return sound;
    }
    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }
}
