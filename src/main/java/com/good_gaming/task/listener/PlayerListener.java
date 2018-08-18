package com.good_gaming.task.listener;

import com.good_gaming.task.Task;
import com.good_gaming.task.query.PlayerJoinQuery;
import com.good_gaming.task.query.UpdateBalanceQuery;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Class to handle all player related events.
 *
 * Created by Josh (MacBook).
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Task.getInstance().getDatabaseManager().schedule(new PlayerJoinQuery(e.getPlayer().getUniqueId(), result -> {
            Task.getInstance().getBalanceManager().cacheBalance(e.getPlayer().getUniqueId(), result.getBalance());

            Task.getInstance().getLogger().info("Balance of player '" + e.getPlayer().getName() + " loaded and cached!");
        }));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Task brief states to only update the database if necessary (ie if it has
        // changed to a different value since login).

        if(Task.getInstance().getBalanceManager().hasChangedBalance(e.getPlayer().getUniqueId())) {
            // Balance is different now to what it was when the player logged in, update in database.
            Task.getInstance().getDatabaseManager().schedule(new UpdateBalanceQuery(e.getPlayer().getUniqueId(), Task.getInstance().getBalanceManager().getCachedBalance(e.getPlayer().getUniqueId())));
            Task.getInstance().getLogger().info("New balance of player '" + e.getPlayer().getName() + " updated!");
        }

        // Remove local cache.
        Task.getInstance().getBalanceManager().removeCachedBalance(e.getPlayer().getUniqueId());
        Task.getInstance().getLogger().info("Balance of player '" + e.getPlayer().getName() + " removed from cache.");
    }

}
