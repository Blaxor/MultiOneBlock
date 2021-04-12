package ro.deiutzblaxo.oneblock.island.level;

import lombok.Getter;
import lombok.Setter;
import ro.deiutzblaxo.oneblock.island.Island;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


@Getter
@Setter
public class Results {
    private AtomicReference<Island> island = new AtomicReference<>();
    private AtomicLong points = new AtomicLong(0);
    private AtomicLong start = new AtomicLong();

    public Results(Island island) {
        this.island.set(island);
        start.set(System.currentTimeMillis());
    }

    protected void add(int nr) {
        points.addAndGet(nr);

    }

    public long getLevel() {
        long p = points.get();
        System.out.println(points);
        long delta = 50 * (50 + 4 * p);
        return (-50 + squareRoot(delta)) / 100;
    }

    public long getRemainPoints() {
        long level = getLevel() + 1;
        return (level * (level + 1) / 2 * 100) - points.get();
    }

    public static long squareRoot(long number) {
        long temp;

        long sr = number / 2;

        do {
            temp = sr;
            sr = (temp + (number / temp)) / 2;
        } while ((temp - sr) != 0);

        return sr;
    }
}
