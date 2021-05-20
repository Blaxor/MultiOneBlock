package ro.deiutzblaxo.oneblock.langs;

public enum MESSAGELIST {

    PHASES_NOW_LORE("phases.lore.now", new String[]{"&7Here are you now!", "&7This phase started at &e{blocks} &7blocks braked"}),
    PHASES_LORE("phases.lore.default", new String[]{"&7It will start at &e{blocks} &7blocks braked"}),
    ITEM_LOCK_LORE("phases.item.lock.lore", new String[]{"&1The power of &c&lGOD&1 of islands!", " ", "&7With this item you can block the phase change!"}),
    PHASE_LOCK_MENU_ITEM_UNLOCK_LORE("phases.item.lock.menu.lore", new String[]{"&7If you want to unlock your phase.", "", "&fClick Me!"}),
    PHASE_LOCK_MENU_ITEM_LOCK_LORE("phases.item.lock.menu.lore", new String[]{"&7If you want to lock your phase.", "", "&fClick Me!"}),
    UPGRADE_TIER_ITEM_LORE("upgrade.tier.item.lore", new String[]{"&7Upgrade tier of island.", "&7Increase it by 1.", "", "&eClick on your island."});
    String path;
    String[] _default;

    MESSAGELIST(String path, String[] _default) {
        this.path = path;
        this._default = _default;
    }
}
