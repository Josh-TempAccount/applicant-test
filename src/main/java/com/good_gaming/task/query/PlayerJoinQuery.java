package com.good_gaming.task.query;

import com.good_gaming.task.Task;
import com.good_gaming.task.util.Callback;
import com.good_gaming.task.util.DatabaseQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Query class to be run when a player joins.
 * This makes sure that the user is in the database (with an initial 0 balance). It will also query the current balance.
 *
 * Created by Josh (MacBook).
 */
public class PlayerJoinQuery extends DatabaseQuery {

    private final UUID uuid;
    private int balance;

    public PlayerJoinQuery(UUID uuid, Callback<PlayerJoinQuery> callback) {
        super(callback);

        this.uuid = uuid;
        this.balance = 0; // Default value, just in case something goes wrong.
    }

    @Override
    protected void runQuery() {
        // Ensure that the user is in the database.
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT IGNORE INTO `" + Task.PLAYER_TABLE + "` (ID, Uuid, Balance) VALUES (?, ?, ?);");
            preparedStatement.setInt(1, 0); // Auto incrementing value.
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Query as to how much of the balance the player has.
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT Balance FROM `" + Task.PLAYER_TABLE + "` WHERE `Uuid`=?;");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                // This should always be the case, but might as well be safe.
                this.balance = resultSet.getInt("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the balance that was loaded from the database.
     *
     * @return the player's balance.
     */
    public int getBalance() {
        return balance;
    }

}
