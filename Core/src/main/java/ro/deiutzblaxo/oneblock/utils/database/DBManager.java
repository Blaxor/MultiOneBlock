package ro.deiutzblaxo.oneblock.utils.database;


import ro.nexs.db.manager.connection.DBConnection;
import ro.nexs.db.manager.exception.NoDataFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBManager extends ro.nexs.db.manager.manager.DBManager {

    /*private ExecutorService pool;*/
    private DBConnection dbConnection;


    public DBManager(DBConnection connection) {
        super(connection);
        this.dbConnection = connection;
        /*pool = Executors.newCachedThreadPool();*/
    }


    public void deleteRow(String table, String ByField, String field) {

        executeUpdate(getPreparedStatement("DELETE FROM " + table + " WHERE " + ByField + " = '" + field + "';"));


    }

    public String getLikeString(String table, String byField, String field, String get) throws NoDataFoundException {

        PreparedStatement statement = getPreparedStatement("SELECT * FROM " + table + " WHERE " + byField + " LIKE '" + field + "'");
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
        /*pool.submit(() -> {*/
        PreparedStatement statement = getPreparedStatement("UPDATE " + Table + " SET " + fieldToSet + " = NULL WHERE " + ByField + " = '" + search + "'");
        execute(statement);
        /*});*/


    }

/*    @Override
    public <T> void set(String tableName, String setField, String keyField, Object set, T key) {
        pool.submit(() -> {
            String query = "UPDATE " + tableName + " SET " + setField + " = ? WHERE " + keyField + " = ?";
            PreparedStatement preparedStatement = this.getPreparedStatement(query);
            try {
                preparedStatement.setObject(1, set);
                preparedStatement.setObject(2, key);
                this.executeUpdate(preparedStatement);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

        });
    }

    @Override
    public <T> void insert(String tableName, String[] fields, T[] args) throws DifferentArgLengthException {
        if (fields.length != args.length) {
            throw new DifferentArgLengthException("Arguments in string array `fields` have to match arguments in T array `args`!");
        }
        pool.submit(() -> {
            AtomicReference<String> query = new AtomicReference<>("INSERT INTO `" + tableName + "` (");
            Arrays.stream(fields).forEach(field -> {
                query.set(query.get() + field + ", ");
            });
            query.set(this.fixLastIndex(query.get()));
            query.set(query.get() + " VALUE (");
            for (int i = 0; i < args.length; i++) {
                query.set(query.get() + "?, ");
            }
            query.set(this.fixLastIndex(query.get()));
            PreparedStatement preparedStatement = this.getPreparedStatement(query.get());
            for (int i = 1; i <= args.length; i++) {
                try {
                    preparedStatement.setObject(i, args[i - 1]);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
            this.execute(preparedStatement);
        });
    }

    private String fixLastIndex(String string) {
        String ret = "";
        ret = string.substring(0, string.length() - 2) + ")";
        return ret;
    }

    @Override
    public <T> T get(String tableName, String getField, String keyField, Object key, Class<T> clazz) throws NoDataFoundException {
        try {
            return pool.submit(() -> {
                String query = "SELECT * FROM `" + tableName + "` WHERE " + keyField + " = ?";
                PreparedStatement preparedStatement = this.getPreparedStatement(query);
                Query queryObject = new Query(preparedStatement.toString(), this.dbConnection.getDatabase());
                if (this.getQueryListener() != null) {
                    this.getQueryListener().onQueryComplete(queryObject);
                }
                try {
                    preparedStatement.setObject(1, key);
                    ResultSet resultSet = this.executeQuery(preparedStatement);
                    if (!(resultSet.next())) {
                        throw new NoDataFoundException("No results were found wile attempting lookup for " + key.toString() + " in field " + keyField + " in database " + getField);
                    }
                    return resultSet.getObject(getField, clazz);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                    return null;
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
