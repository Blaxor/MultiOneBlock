package ro.deiutzblaxo.oneblock.island.level.calculate;

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

    public int getLevel() {
        long p = points.get();
        System.out.println(points);
        long delta = 50 * (50 + 4 * p);
        return (int) ((-50 + squareRoot(delta, 0.1)) / 100);
    }

    public long getRemainPoints() {
        long level = getLevel() + 1;
        return (level * (level + 1) / 2 * 100) - points.get();
    }

    public static long squareRoot(long number, double eps) {
        long sr = number / 2;
        long temp;
        double error = 1; ///eroarea e in cazul cel mai rau
        while (error > eps) {
            temp = sr;
            sr = (temp + (number / temp)) / 2;
            error = (double) (temp - sr);
        }
        return sr;
    }
}
