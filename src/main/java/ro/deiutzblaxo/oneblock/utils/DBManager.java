package ro.deiutzblaxo.oneblock.utils;

import ro.nexs.db.manager.connection.DBConnection;


public class DBManager extends ro.nexs.db.manager.manager.DBManager {
    public DBManager(DBConnection con){
        super(con);

    }

    public void deleteRow(String table , String ByField , String field){
        executeStatement("DELETE FROM "+table+" WHERE "+ByField+" = " + field + ";");
    }
}
