package ro.deiutzblaxo.oneblock.island.level;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

@Getter
public class IslandLevelManager {
    private OneBlock plugin;
    private Queue<Island> queue_islands = new ConcurrentLinkedQueue<>();
    private IslandLevelCalculator calculationNow;

    public IslandLevelManager(OneBlock plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (queue_islands.isEmpty() && calculationNow == null) {
                return;
            }
            if (calculationNow != null) {
                if (calculationNow.isDone()) {

                    calculationNow.getIsland().get().getMeta().getMembers().forEach((uuid, rank) -> {

                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            Results result = calculationNow.getResults().get();
                            result.getIsland().get().getMeta().setLevelPoints(result.getPoints().get());
                            player.sendMessage("Island level :" + result.getLevel() + " (" + result.getRemainPoints() + " points left)");
                        }
                    });
                    calculationNow = null;
                }
            } else {
                if (!getQueue_islands().isEmpty())
                    doNewCalculationFromQueue();
            }
        }, 1, 1);
    }


    public void addInQueue(Island island) {
        queue_islands.add(island);
    }

    public void doNewCalculationFromQueue() {
        IslandLevelCalculator islandLevelCalculator = new IslandLevelCalculator(queue_islands.poll());
        calculationNow = islandLevelCalculator;
    }

    public void cancelCalculation(Island island) {
        if (queue_islands.isEmpty() && calculationNow == null)
            return;
        if (calculationNow.getIsland().get().equals(island)) {
            calculationNow.getThreadsStatus().forEach((thread, integer) -> {
                thread.stop();
            });
            calculationNow = null;
            return;
        }
        if (queue_islands.contains(island))
            queue_islands.remove(island);
        else
            plugin.getLogger().log(Level.WARNING, "The calculation of island " + island.getUuidIsland() + " is not queued for calculation!");
    }

    public boolean isAlreadyCalculating(Island island) {
        if (queue_islands.isEmpty() && calculationNow == null)
            return false;
        if (queue_islands.contains(island)) {
            return true;
        }
        if (calculationNow.getIsland().get().equals(island)) {
            return true;
        }
        return false;

    }

}
