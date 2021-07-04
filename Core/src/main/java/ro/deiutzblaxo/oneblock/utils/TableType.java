package ro.deiutzblaxo.oneblock.utils;

public enum TableType {

    PLAYERS("PLAYERS"), ISLANDS("ISLANDS"), LEVEL("LEVEL");

    public final String table;

    TableType(String table) {
        this.table = table;
    }
}
