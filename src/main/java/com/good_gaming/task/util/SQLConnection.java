package com.good_gaming.task.util;

import com.good_gaming.task.Task;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that manages the SQL connection pool (HikariCP).
 *
 * Created by Josh (MacBook).
 */
public class SQLConnection {

    private static final String DRIVER_FORMAT = "jdbc:mysql://%s:%s";

    private final String ip, username;
    private final int port;
    private String database;

    private HikariDataSource dataSource;

    public SQLConnection(String ip, String username, String password, int port) {
        this.ip = ip;
        this.username = username;
        this.port = port;
        this.database = "mysql";

        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl(String.format(DRIVER_FORMAT, ip, port));
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
    }

    /**
     * Get the IP of the database we're connecting to.
     *
     * @return database IP.
     */
    public String getIP() {
        return ip;
    }

    /**
     * Get the name of the database we're connecting to.
     *
     * @return name of database.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Get the username used to authenticate to the database we're connecting to.
     * I have not made the password available for security reasons - it is used only in database login (constructor).
     *
     * @return database username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the port used for connecting to the database.
     *
     * @return port used. Default would be 3306.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the active database.
     * This is to be used when the database is created for the first time.
     *
     * @param database name of new database to use.
     */
    public void setDatabase(String database) {
        this.dataSource.setCatalog(database);
        this.database = database;

        Task.getInstance().getLogger().info("Active database switched to '" + database + "'!");
    }

    /**
     * Get a connection from the connection pool.
     *
     * @return ready SQL Connection from the connection pool.
     * @throws SQLException thrown if something goes wrong (e.g. data source closed).
     */
    public Connection getConnection() throws SQLException {
        Connection newConnection = dataSource.getConnection();

        // Ensure new database is used. The global setCatalog method seems a bit iffy from testing.
        newConnection.setCatalog(database);

        // NOTE: I'm intentionally not logging here as this would simply spam the console.

        return newConnection;
    }

    public void disconnect() {
        this.dataSource.close();
    }

}