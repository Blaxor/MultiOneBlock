package ro.deiutzblaxo.oneblock.utils.database;


import ro.nexs.db.manager.connection.DBConnection;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBManager extends ro.nexs.db.manager.manager.DBManager {
    public DBManager(DBConnection connection) {
        super(connection);
    }


    public void deleteRow(String table, String ByField, String field) {
        executeUpdate(getPreparedStatement("DELETE FROM " + table + " WHERE " + ByField + " = '" + field + "';"));
    }

    public String getLikeString(String table, String byField, String field, String get) throws NoDataFoundException {
        PreparedStatement statement = getPreparedStatement("SELECT * FROM " + table + " WHERE " + byField + " LIKE '" + field + "'");
        // SELECT * FROM name WHERE NAME LIKE 'jdeiutz'
        ResultSet set = executeQuery(statement);
        try {
            if (!(set.next())) {
                throw new NoDataFoundException("No data found");
            }
            return set.getString(get);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }

    public void setNull(String Table, String ByField, String search, String fieldToSet) {
        PreparedStatement statement = getPreparedStatement("UPDATE " + Table + " SET " + fieldToSet + " = NULL WHERE " + ByField + " = '" + search + "'");
        execute(statement);
    }

}
