package ro.deiutzblaxo.oneblock.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<F, L> {
    //facts : FL is from fuck my life...

    private F first;

    private L last;


    public Pair(F f, L l) {
        first = f;

        last = l;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }
}
