package ro.deiutzblaxo.oneblock.langs;

public enum MESSAGE {


    GUI_TITLE_PHASES("gui.phases.title", "&ePhases"),
    DECORATIVE_GLASS("decorative_glass", "&e"),
    TELEPORTED_TO_ISLAND("teleported.island", "&fYou have been teleported to your island!"),
    ISLAND_TEAM_MENU_TITLE("team.menu.title", "&e&lTEAM MENU"),
    ISLAND_TEAM_LIST_TITLE("team.menu.list.title", "&e&lLista cu membrii insulei"),
    ISLAND_TEAM_LIST_ITEM_TITLE("team.menu.list.item.title", "&e&lLista cu membrii insulei"),
    ISLAND_INVITE_ALEARDY_IN_ISLAND("team.invite.isInIsland", "&cThis player is already in your island!"),
    ISLAND_INVITE_SENDER("team.invite.sender", "&7You invited {name} in your island!"),
    ISLAND_INVITE_RECIVER("team.invite.reciver", "&7You have been invited {name}'s island. \nYou have 10 seconds to accept invite using /is accept"),
    ISLAND_INVITE_ACCEPT("team.invite.accept", "&e{name} &7accepted your invite!"),
    ISLAND_INVITE_REJECT("team.invite.reject", "&e{name} &7rejected your invitation!"),
    ISLAND_NOT_LOADED("island.not_loaded", "&7Please use \"/is go\" firstly!"),
    ISLAND_INVITE_OFFLINE("team.invite.offline", "&cPlayer is offline!"),
    ISLAND_INVITE_EXISTS("team.invite.noExist", "&cPlayer don`t exist!"),
    PM_RECEIVE("chat.pm.receive", "&7(&6PM&7) &eEU&f de la &e{name} &8> &f{message}"),
    PM_SEND("chat.pm.send", "&7(&6PM&7) &e{name}&f de la &emine &8> &f{message}"),
    PM_SELF("chat.pm.self", "&CYou can`t send yourself a message"),
    PM_NO_MESSAGE("chat.pm.noMessage", "&cPlease enter a message!"),
    PM_PLAYER_OFFLINE("chat.pm.playerOffline", "&cPlayer offline!"),
    PM_PLAYER_NO_EXISTS("chat.pm.playerNoExist", "&cPlayer don`t exist!"),
    CHAT_GLOBAL_PREFIX("chat.global.prefix", "&7[&eGLOBAL&7] {name} > "),
    CHAT_SERVER_PREFIX("chat.server.prefix", "&7{name} >"),
    PHASE_LOCK_ITEM_DISPLAY("phases.lock.item.display", "&bThe power of island locking"),
    PHASE_LOCK_MENU_TITLE("phases.lock.menu.title", "&e&lLock your phase!"),
    PHASE_LOCK_MENU_ITEM_LOCK_DISPLAY("phases.item.lock.menu.display", "&cLock your island!"),
    PHASE_LOCK_MENU_ITEM_UNLOCK_DISPLAY("phases.item.unlock.menu.display", "&aUnLock your island!"),
    PHASES_ISLAND_ALREADY_LOCKED("phases.island.aleardy.locked", "&cYour island is already locked!"),
    PHASES_ISLAND_ALREADY_UNLOCKED("phases.island.aleardy.unlocked", "&cYour island is already unlocked!"),
    PHASES_LOCK_MENU_MESSAGE_LOCK("phases.island.lock", "&7You have been lock the phase of your island"),
    PHASES_LOCK_MENU_MESSAGE_UNLOCK("phases.island.lock", "&7You have been unlock the phase of your island");


    String path, Default;

    MESSAGE(String _path, String _default) {
        path = _path;
        Default = _default;
    }
}
