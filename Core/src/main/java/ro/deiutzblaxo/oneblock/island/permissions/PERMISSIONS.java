package ro.deiutzblaxo.oneblock.island.permissions;

import lombok.Getter;
import ro.deiutzblaxo.oneblock.player.Rank.RankEnum;

public enum PERMISSIONS {

    BAN(RankEnum.CO_OWNER),
    KICK(RankEnum.CO_OWNER),
    UNBAN(RankEnum.CO_OWNER),
    BANLIST(RankEnum.MEMBER),
    EXPEL(RankEnum.CO_OWNER),
    CHANGENAME(RankEnum.CO_OWNER),
    TELEPORT(RankEnum.GUEST),
    SETSPAWN(RankEnum.CO_OWNER),
    BREAK(RankEnum.MEMBER),
    PLACE(RankEnum.MEMBER),
    PVE(RankEnum.MEMBER),
    INTERACT(RankEnum.MEMBER),
    UPGRADE_RADIUS(RankEnum.MEMBER);

    @Getter
    private final RankEnum lowestRankDefault;

    PERMISSIONS(RankEnum lowestRankDefault) {
        this.lowestRankDefault = lowestRankDefault;
    }

    public boolean isAllow(RankEnum rank) {
        return PERMISSIONS.isAllow(rank, lowestRankDefault);
    }

    public static boolean isAllow(RankEnum rank1, RankEnum rank2) {
        if (rank1 == null || rank2 == null) {
            return false;
        }
        return rank1.ordinal() >= rank2.ordinal();
    }


}
