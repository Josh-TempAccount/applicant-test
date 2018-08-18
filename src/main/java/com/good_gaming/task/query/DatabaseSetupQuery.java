package com.good_gaming.task.query;

import com.good_gaming.task.Task;
import com.good_gaming.task.util.DatabaseQuery;
import org.bukkit.Bukkit;

import java.sql.SQLException;

/**
 * Query class to setup database. This should always be run before any other query.
 *
 * Created by Josh (MacBook).
 */
public class DatabaseSetupQuery extends DatabaseQuery {

    @Override
    protected void runQuery() {
        try {
            // Prepare database.
            Task.getInstance().getLogger().info("Creating database and table (if not created already).");
            getConnection().prepareStatement("CREATE DATABASE IF NOT EXISTS `Economy`;").execute();
            Task.getInstance().getDatabaseManager().getSQLConnection().setDatabase("Economy");

            // Prepare table.
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " +
                        "`" + Task.DATABASE + "`." +
                        "`" + Task.PLAYER_TABLE + "` " +
                        "(" +
                        "`ID` INT NOT NULL AUTO_INCREMENT, " +
                        "`Uuid` VARCHAR(36) UNIQUE, " +
                        "`Balance` INT UNSIGNED," +
                        "PRIMARY KEY (`ID`)" +
                        ") ENGINE = InnoDB;").execute();
            Task.getInstance().getLogger().info("Database & table initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            Task.getInstance().getLogger().severe("Unable to create table!");
            Task.getInstance().getLogger().severe("Shutting down plugin, please check your MySQL details then try again.");
            Bukkit.getPluginManager().disablePlugin(Task.getInstance());
        }
    }

}