package ro.deiutzblaxo.oneblock.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rank {

    public enum RankEnum {
        GUEST, COOP, TRUSTED, MEMBER, CO_OWNER, OWNER;
    }


    public static RankEnum getHigherRank(RankEnum rank) {
        List<RankEnum> list = Arrays.asList(RankEnum.values());
        return list.get(Math.min(list.size() - 1, list.indexOf(rank) + 1));

    }

    public static RankEnum getLowerRank(RankEnum rank) {
        List<RankEnum> list = Arrays.asList(RankEnum.values());
        return list.get(Math.max(list.indexOf(rank) - 1, 0));

    }

}
