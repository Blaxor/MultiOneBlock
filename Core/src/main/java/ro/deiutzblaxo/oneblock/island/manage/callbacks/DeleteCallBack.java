package ro.deiutzblaxo.oneblock.island.manage.callbacks;

import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.exceptions.IslandLoadedException;
import ro.deiutzblaxo.oneblock.island.manage.callbacks.interfaces.onFinish;
import ro.deiutzblaxo.oneblock.utils.TableType;

import java.util.concurrent.Callable;

public class DeleteCallBack implements Callable<Void> {

    OneBlock plugin;
    String island;
    onFinish onFinish;

    public DeleteCallBack(OneBlock plugin, String island, onFinish onFinish) {
        this.plugin = plugin;
        this.island = island;
        this.onFinish = onFinish;
    }


    @Override
    public Void call() {

        plugin.getDbManager().deleteRow(TableType.ISLANDS.table, "UUID", island);
        plugin.getDbManager().deleteRow("worlds", "name", island);
        plugin.getDbManager().deleteRow(TableType.LEVEL.table, "UUID", island);
        onFinish.run(null);

        return null;
    }
}