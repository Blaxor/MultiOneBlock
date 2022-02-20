package ro.deiutzblax.expendeditems.item.rarity;

import lombok.Getter;

public enum RARITY {

    COMMON("&f&l"), UNCOMMON("&a&l"), RARE("&b&l"), EPIC("&5&l"), LEGENDARY("&6&l");
    @Getter
    private String color;

    RARITY(String color) {
        this.color = color;
    }
}
