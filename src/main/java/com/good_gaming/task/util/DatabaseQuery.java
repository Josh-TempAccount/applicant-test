package com.good_gaming.task.util;

import com.good_gaming.task.Task;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database query class.
 * Currently geared for SQL however could easily be adapted to other database implementations.
 *
 * Created by Josh (MacBook).
 */
public abstract class DatabaseQuery implements Runnable {

    private final Callback callback;
    private Connection connection;

    public DatabaseQuery() {
        this(null);
    }

    public DatabaseQuery(Callback callback) {
        this.callback = callback;
    }

    protected abstract void runQuery();

    /**
     * Get the assigned connection from the connection pool.
     *
     * @return Connection instance. Shouldn't be null (unless the query hasn't started running).
     */
    protected final Connection getConnection() {
        return connection;
    }

    @Override
    public final void run() {
        // First, get a new connection from the connection pool.
        try {
            this.connection = Task.getInstance().getDatabaseManager().getNewConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Next, run the query.
        runQuery();

        // After the query is finished, close & null the connection.
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Task.getInstance().getLogger().severe("An issue occurred when closing a database connection!");
            Task.getInstance().getLogger().severe("This shouldn't be fatal, however please be cautious and restart" +
                    " the server if you notice any irregularities.");
        }
        this.connection = null;

        // Finally, if there is a callback present, call it.
        if(this.callback != null) {
            callback.call(this);
        }
    }

}