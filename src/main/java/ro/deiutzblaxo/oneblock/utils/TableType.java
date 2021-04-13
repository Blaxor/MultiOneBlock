package ro.deiutzblaxo.oneblock.utils;

public enum TableType {

    PLAYERS("PLAYERS"), ISLANDS("ISLANDS"), NAME("NAME"), LEVEL("LEVEL");

    public final String table;

    TableType(String table) {
        this.table = table;
    }
}
