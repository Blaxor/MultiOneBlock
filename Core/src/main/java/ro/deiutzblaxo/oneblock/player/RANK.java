package ro.deiutzblaxo.oneblock.player;

import java.util.ArrayList;
import java.util.Arrays;

public enum RANK {
    GUEST(0), COOP(1), TRUSTED(2), MEMBER(3), CO_OWNER(4), OWNER(5);
    private  int a;

    RANK(int a) {
        this.a = a;
    }

    public RANK getHigherRank() {
        return RANK.values()[a+1 > 5? 5:a+1];

    }

    public RANK getLowerRank(RANK rank) {
        return RANK.values()[a-1 < 0? 0:a-1];
    }
}
