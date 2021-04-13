package ro.deiutzblaxo.oneblock.island.level.calculate;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CalculateRunnable implements Runnable {
    private AtomicReference<IslandLevelCalculator> calculator = new AtomicReference<>();
    protected AtomicInteger id = new AtomicInteger();

    public CalculateRunnable(IslandLevelCalculator calculator, int i) {
        this.calculator.set(calculator);
        this.id.set(i);
    }

    @Override
    public void run() {
        while (!calculator.get().getChunks().isEmpty()) {
            Chunk chunk = calculator.get().getChunks().poll();
            ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
            int value = 0;
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < chunkSnapshot.getHighestBlockYAt(x, z); y++) {
                        Material material = chunk.getBlock(x, y, z).getType();
                        if (material != null)
                            if (IslandLevelCalculator.blockValue.containsKey(material)) {
                                if (IslandLevelCalculator.blockValue.get(material) != 0)
                                    value += IslandLevelCalculator.blockValue.get(material);
                            }

                    }
                }

            if (value > 0)
                calculator.get().getResults().get().add(value);
        }

        calculator.get().getThreadsStatus().put(Thread.currentThread(), 1);

    }


//TODO DONE
}
