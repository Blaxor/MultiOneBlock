package ro.deiutzblaxo.oneblock.player;

import java.util.ArrayList;
import java.util.Arrays;

public enum RANK {


    GUEST, COOP, TRUSTED, MEMBER, CO_OWNER, OWNER;


    private final ArrayList<RANK> ranksOrder = (ArrayList<RANK>) Arrays.asList(values());

    public RANK getHigherRank(RANK rank) {
        return ranksOrder.get(Math.min(ranksOrder.size()-1,ranksOrder.indexOf(rank)+1));

    }

    public RANK getLowerRank(RANK rank) {
        return ranksOrder.get(Math.max(ranksOrder.indexOf(rank) - 1,0));

    }
}
