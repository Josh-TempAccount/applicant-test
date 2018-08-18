package com.good_gaming.task.query;

import com.good_gaming.task.Task;
import com.good_gaming.task.util.DatabaseQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Query class to be run when a balance needs to be updated.
 * Should only be done on logout (and even then only when the balance has changed) in the current implementation.
 *
 * Created by Josh (MacBook).
 */
public class UpdateBalanceQuery extends DatabaseQuery {

    private final UUID uuid;
    private final int newBalance;

    public UpdateBalanceQuery(UUID uuid, int newBalance) {
        this.uuid = uuid;
        this.newBalance = newBalance;
    }

    @Override
    protected void runQuery() {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE `" + Task.PLAYER_TABLE + "` SET `Balance`=? WHERE `Uuid`=?;");
            preparedStatement.setInt(1, newBalance);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
