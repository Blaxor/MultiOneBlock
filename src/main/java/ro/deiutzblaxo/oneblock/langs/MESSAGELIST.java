package ro.deiutzblaxo.oneblock.langs;

public enum MESSAGELIST {

    PHASES_NOW_LORE("phases.now", new String[]{"&fHere are you now!", ""}),
    ITEM_LOCK_LORE("phases.item.lock.lore", new String[]{"&1The power of &c&lGOD&1 of islands!", " ", "&7With this item you can block the phase change!"}),
    PHASE_LOCK_MENU_ITEM_UNLOCK_LORE("phases.item.lock.menu.lore", new String[]{"&7If you want to unlock your phase.", "", "&fClick Me!"}),
    PHASE_LOCK_MENU_ITEM_LOCK_LORE("phases.item.lock.menu.lore", new String[]{"&7If you want to lock your phase.", "", "&fClick Me!"});
    String path;
    String[] _default;

    MESSAGELIST(String path, String[] _default) {
        this.path = path;
        this._default = _default;
    }
}
