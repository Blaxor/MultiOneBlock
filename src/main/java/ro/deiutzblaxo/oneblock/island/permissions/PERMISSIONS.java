package ro.deiutzblaxo.oneblock.island.permissions;

import lombok.Getter;
import ro.deiutzblaxo.oneblock.player.RANK;

public enum PERMISSIONS {

    BAN(RANK.CO_OWNER),
    KICK(RANK.CO_OWNER),
    UNBAN(RANK.CO_OWNER),
    BANLIST(RANK.MEMBER),
    EXPEL(RANK.CO_OWNER),
    CHANGENAME(RANK.CO_OWNER),
    TELEPORT(RANK.GUEST),
    SETSPAWN(RANK.CO_OWNER),
    BREAK(RANK.MEMBER),
    PLACE(RANK.MEMBER),
    PVE(RANK.MEMBER),
    INTERACT(RANK.MEMBER);
    @Getter
    private RANK lowestRankDefault;

    PERMISSIONS(RANK lowestRankDefault) {
        this.lowestRankDefault = lowestRankDefault;
    }

    public boolean isAllow(RANK rank) {
        return PERMISSIONS.isAllow(rank, lowestRankDefault);
    }

    public static boolean isAllow(RANK rank1, RANK rank2) {
        if (rank1 == null || rank2 == null) {
            return false;
        }
        return rank1.ordinal() >= rank2.ordinal();
    }
}
