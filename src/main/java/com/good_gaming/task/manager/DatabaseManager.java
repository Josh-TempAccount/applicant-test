package com.good_gaming.task.manager;

import com.good_gaming.task.Task;
import com.good_gaming.task.query.DatabaseSetupQuery;
import com.good_gaming.task.util.DatabaseQuery;
import com.good_gaming.task.util.SQLConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class to manage all database queries and connections.
 * Written in such a way that it would be simple to introduce more than one database implementation (i.e. MongoDB as
 * well as MySQL).
 *
 * Created by Josh (MacBook).
 */
public class DatabaseManager {

    private final ExecutorService threadPool;
    private SQLConnection sqlConnection;

    public DatabaseManager(int threads) {
        this.threadPool = Executors.newFixedThreadPool(threads);

        setupConnection();
        schedule(new DatabaseSetupQuery());
    }

    /**
     * Schedule a DatabaseQuery for execution when a thread becomes available.
     *
     * @param databaseQuery query to schedule.
     */
    public void schedule(DatabaseQuery databaseQuery) {
        if (this.threadPool.isShutdown() || this.threadPool.isTerminated()) {
            Task.getInstance().getLogger().severe("A new database query was scheduled whilst the thread pool was " +
                    "terminated/closed!");

            throw new UnsupportedOperationException("You are unable to schedule any more database queries after the " +
                    "thread pool has been terminated / closed.");
        }
        this.threadPool.submit(databaseQuery);

        // NOTE: Intentionally haven't logged here as this would only spam the console and serve no purpose.
    }

    /**
     * Finish up with all pending threads (call this when the server stops).
     * You won't be able to schedule any more threads after you call this.
     */
    public void finish() {
        Task.getInstance().getLogger().info("Finishing all queued database queries..");
        try {
            this.threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Task.getInstance().getLogger().severe("Issue finishing all database queries!");
            Task.getInstance().getLogger().severe("Some data may be lost.");
        }
        this.threadPool.shutdown();
        this.sqlConnection.disconnect();
    }

    /**
     * Manually get a new connection from the connection pool.
     *
     * @return connection from the connection pool. Shouldn't be null.
     */
    public Connection getNewConnection() throws SQLException {
        return this.sqlConnection.getConnection();
    }

    /**
     * Setup the MySQL database connection.
     */
    private void setupConnection() {
        this.sqlConnection = new SQLConnection(Task.getInstance().getConfig().getString("database.ip"),
                Task.getInstance().getConfig().getString("database.username"),
                Task.getInstance().getConfig().getString("database.password"),
                Task.getInstance().getConfig().getInt("database.port"));
        Task.getInstance().getLogger().info("A new SQL connection has been initialized!");
    }

    /**
     * Get the current SQL connection.
     * This holds things like connection settings (not including password).
     *
     * @return sql connection - null if something went wrong.
     */
    public SQLConnection getSQLConnection() {
        return this.sqlConnection;
    }

}