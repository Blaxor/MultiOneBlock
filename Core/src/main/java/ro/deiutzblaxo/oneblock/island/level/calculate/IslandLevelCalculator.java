package ro.deiutzblaxo.oneblock.island.level.calculate;


import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.island.radius.BorderHandler;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class IslandLevelCalculator {
    public static TreeMap<Material, Integer> blockValue = new TreeMap<>();
    private final ConcurrentHashMap<Thread, Integer> threadsStatus = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Chunk> chunks = new ConcurrentLinkedQueue<>();
    private final AtomicReference<Island> island = new AtomicReference<>();
    private final AtomicReference<Results> results = new AtomicReference<>();


    public IslandLevelCalculator(Island island) {

        this.island.set(island);
        results.set(new Results(island));
        System.out.println("Started calculating with " + OneBlock.THREADS_NUMBER + " threads for island " + island.getUuidIsland());
        results.get().getIsland().set(island);
        int radius = BorderHandler.getRadius(island.getMeta().getRadiusType(), island.getMeta().getRadiusTire());
        for (int i = ((radius / 16) + 2) * -1; i <= (radius / 16) + 2; i++) {
            for (int j = ((radius / 16) + 2) * -1; j <= (radius / 16) + 2; j++)
                chunks.add(island.getBukkitWorld().getChunkAt(i, j));

        }

        for (int i = 0; i < OneBlock.THREADS_NUMBER; i++) {
            Thread thread = new Thread(new CalculateRunnable(this, i));
            threadsStatus.put(thread, 0);
            thread.start();
        }

    }

    public boolean isDone() {
        return !threadsStatus.containsValue(0);

    }

}
