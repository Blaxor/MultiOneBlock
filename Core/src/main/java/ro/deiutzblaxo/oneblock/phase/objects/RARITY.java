package ro.deiutzblaxo.oneblock.phase.objects;

import org.bukkit.Color;

public enum RARITY {
    COMMON(Color.fromBGR(0, 255, 67)),//verde
    UNCOMMON(Color.fromBGR(181, 181, 181)),//gri
    RARE(Color.fromBGR(255, 159, 0)),//albastru
    EPIC(Color.fromBGR(0, 225, 255));//galben

    public Color color;

    RARITY(Color fromBGR) {
        color = fromBGR;
    }
}
