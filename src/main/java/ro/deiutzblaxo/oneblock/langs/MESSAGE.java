package ro.deiutzblaxo.oneblock.langs;

public enum MESSAGE {


    GUI_TITLE_PHASES("gui.phases.title", "&ePhases"),
    TELEPORTED_TO_ISLAND("teleported.island", "&fYou have been teleported to your island!"),
    ISLAND_TEAM_MENU_TITLE("team.menu.title", "&e&lTEAM MENU"),
    ISLAND_INVITE_ALREADY_IN_ISLAND("team.invite.isInIsland", "&cThis player is already in your island!"),
    ISLAND_INVITE_SENDER("team.invite.sender", "&7You invited {name} in your island!"),
    ISLAND_INVITE_RECEIVER("team.invite.receiver", "anbc"),
    ISLAND_INVITE_ACCEPT("team.invite.accept", "&e{name} &7accepted your invite!"),
    ISLAND_INVITE_ACCEPTED("team.invite.accepted", "&eYou accepted the invitation!"),
    ISLAND_INVITE_REJECT("team.invite.reject", "&e{name} &7rejected your invitation!"),
    ISLAND_INVITE_REJECTED("team.invite.rejected", "&eYou rejected the invitation!"),
    ISLAND_INVITE_SAME_PLAYER("team.invite.same", "&cYou can`t invite yourself!"),
    ISLAND_NOT_LOADED("island.not_loaded", "&7Please use '/is go' firstly!"),
    ISLAND_INVITE_OFFLINE("team.invite.offline", "&cPlayer is offline!"),
    ISLAND_INVITE_EXISTS("team.invite.noExist", "&cPlayer don`t exist!"),
    PM_RECEIVE("chat.pm.receive", "&7(&6PM&7) &eEU&f de la &e{name} &8> &f{message}"),
    PM_SEND("chat.pm.send", "&7(&6PM&7) &e{name}&f de la &emine &8> &f{message}"),
    PM_SELF("chat.pm.self", "&CYou can`t send yourself a message"),
    PM_NO_MESSAGE("chat.pm.noMessage", "&cPlease enter a message!"),
    PLAYER_OFFLINE("error.player.offline", "&cPlayer offline!"),
    PLAYER_NO_EXISTS("error.player.noExist", "&cPlayer don`t exist!"),
    CHAT_GLOBAL_PREFIX("chat.global.prefix", "&7[&eGLOBAL&7] {name} > "),
    CHAT_SERVER_PREFIX("chat.server.prefix", "&7{name} >"),
    PHASE_LOCK_ITEM_DISPLAY("phases.lock.item.display", "&bThe power of island locking"),
    PHASE_LOCK_MENU_TITLE("phases.lock.menu.title", "&e&lLock your phase!"),
    PHASE_LOCK_MENU_ITEM_LOCK_DISPLAY("phases.item.lock.menu.display", "&cLock your island!"),
    PHASE_LOCK_MENU_ITEM_UNLOCK_DISPLAY("phases.item.unlock.menu.display", "&aUnLock your island!"),
    PHASES_ISLAND_ALREADY_LOCKED("phases.island.already.locked", "&cYour island is already locked!"),
    PHASES_ISLAND_ALREADY_UNLOCKED("phases.island.already.unlocked", "&cYour island is already unlocked!"),
    PHASES_LOCK_MENU_MESSAGE_LOCK("phases.island.lock", "&7You have been lock the phase of your island"),
    PHASES_LOCK_MENU_MESSAGE_UNLOCK("phases.island.lock", "&7You have been unlock the phase of your island"),
    ISLAND_BREAK_NOT_ALLOW("error.island.break", "&4You are not allowed to break on this island."),
    ISLAND_PLACE_NOT_ALLOW("error.island.place", "&4You are not allowed to place on this island."),
    ISLAND_ARE_BANNED("error.island.banned", "&eYou are banned from this island!"),
    ISLAND_ENTER_NOT_ALLOW("error.island.enter", "&eYou are not allowed to enter!"),
    ISLAND_PVE_NOT_ALLOW("error.island.pve", "&ePVE is not allowed on this island!"),
    ISLAND_PVP_NOT_ALLOW("error.island.pvp", "&ePVP is not allowed on this island!"),
    ISLAND_INTERACT_NOT_ALLOW("error.island.interact", "&eYou can`t interact on this island!"),
    ISLAND_BAN_NOT_ALLOW("error.island.ban.allow", "&eYou are not allowed to ban!"),
    ISLAND_BAN_OWNER("error.island.ban.owner", "&eYou can`t ban the owner!"),
    ISLAND_IS_BANNED("error.island.ban.banned", "&eIs already banned!"),
    ISLAND_BANNED_OTHER("ban", "&eYou banned {name}"),
    ISLAND_BANNED("banned", "&eYou have been banned!"),
    ISLAND_BANLIST_NOT_ALLOW("error.island.banlist", "&eYou can`t see the list of banned players!"),
    ISLAND_BANLIST("banlist", "&eThe banned players are: "),
    ISLAND_COUNT("count", "&eThe block count: "),
    ISLAND_NOT_ENTERED("error.island.enterd", "&eThe player is not on your island!"),
    ISLAND_EXPEL_MEMBER("error.island.expel.member", "&eYou can`t expel a member of island!"),
    ISLAND_EXPELED("expel.other", "&eYou have been expel!"),
    ISLAND_EXPEL("expel.sender", "&eYou expeled {name}!"),
    ISLAND_ERROR_EXPEL("error.island.expel", "&eYou are not allowed to expel someone."),
    ISLAND_ERROR_KICK("error.island.kick.allow", "&eYou can`t kick someone!"),
    ISLAND_ERROR_KICK_PART("error.island.kick.part", "&e{name} is not part of your island!"),
    ISLAND_ERROR_KICK_SELF("error.island.kick.self", "&eYou can`t kick yourself!"),
    ISLAND_KICK_OTHER("island.kick.other", "&eYou kick {name} from your island!"),
    ISLAND_KICKED("island.kick.me", "&eYou have been kicked from the island!"),
    ISLAND_ERROR_LEAVE("error.island.leave", "&eYou need to remove all the members first!"),
    ISLAND_LEAVE("leave", "&eYou leaved the island!"),
    ISLAND_LEVEL_ALREADY_CALCULATE("error.island.level.already", "&eThe level of your island is already calculating. Please wait!"),
    ISLAND_LEVEL_CALCULATING("island.level.calculating", "&eThe level of your island will be calculated! Please wait!"),
    ISLAND_LEVEL_RESULT("island.level.result", "&eThe level of the island is {level} ({points-left} points left for next level)"),
    ISLAND_NAME_INFO("island.name.info", "&eThe name of island is: "),
    ISLAND_RESET("island.reset", "&eYou reseted your island!"),
    ISLAND_ERROR_NAME_ALLOW("error.island.name.allow", "&eYou can`t change the name of the island!"),
    ISLAND_ERROR_NAME_SAME("error.island.name.same", "&eIsland already have this name!"),
    ISLAND_NAME("island.name", "&eYou changed the name of island in: "),
    ISLAND_ERROR_SETSPAWN_NOT_ISLAND("error.island.setspawn.not_on_it", "&eYou are not on your island. Please go on your island."),
    ISLAND_ERROR_SETSPAWN_ALLOW("error.island.setspawn.allow", "&eYou are not allowed to setspawn!"),
    ISLAND_ERROR_SETSPAWN_LOCATION_SAFE("error.island.setspawn.location", "&eThis location is not safe!"),
    ISLAND_SETSPAWN_SUCCES("setspawn", "&eYou have set a new spawn point for the island."),
    ISLAND_ERROR_TEAM_CONFIRM("error.island.team.confirm",
            "&eYou can`t accept the invite because there are other members on island . you need to remove them"),
    ISLAND_ERROR_UNBAN_ALLOW("error.island.unban.allow", "&eYou are not allowed to unban!"),
    ISLAND_ERROR_UNBAN_BAN("error.island.unban.ban", "&eThis player is not banned!"),
    ISLAND_UNBAN("unban", "&eYou have unbanned {name}"),
    ISLAND_UNBANNED("unbanned", "&eYou have been unbanned!"),
    LOCATION_NOT_SAFE("error.location_not_safe", "That location is not safe. You have been teleported to spawn!"),
    ISLAND_TOP_MENU("top.title", "&eTOP ISLANDS"),
    INVITE_MAX_TEAM("error.island.invite.max_members", "&eYou have reach max members.You can`t invite anymore!"),
    INFO_NO_ISLAND("error.island.info.invalid","&cYou need to be on a island to work!");
    String path, Default;

    MESSAGE(String _path, String _default) {
        path = _path;
        Default = _default;
    }
}
