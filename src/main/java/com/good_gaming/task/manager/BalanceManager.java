package com.good_gaming.task.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class to manage all balance caching.
 * This is done to maximise efficiency by reducing the amount of database queries required.
 *
 * Created by Josh (MacBook).
 */
public class BalanceManager {

    private final Map<UUID, Integer> originalBalanceMap, newBalanceMap;

    public BalanceManager() {
        this.originalBalanceMap = new HashMap<>();
        this.newBalanceMap = new HashMap<>();
    }

    /**
     * Initially cache a player's balance - usually on login.
     *
     * @param uuid uuid of the player.
     * @param balance current balance of the player.
     */
    public void cacheBalance(UUID uuid, int balance) {
        this.originalBalanceMap.put(uuid, balance);
        this.newBalanceMap.put(uuid, balance);
    }

    /**
     * Remove a player's cached balance - usually on quit.
     *
     * @param uuid uuid of the player.
     */
    public void removeCachedBalance(UUID uuid) {
        this.originalBalanceMap.remove(uuid);
        this.newBalanceMap.remove(uuid);
    }

    /**
     * Check whether the player's balance has changed between login and now.
     * This allows us to minimise the number of database queries required (as the balance doesn't need to be updated
     * in the database if it hasn't changed).
     *
     * @param uuid uuid of the player.
     * @return boolean of whether the player's balance has changed since login.
     */
    public boolean hasChangedBalance(UUID uuid) {
        if(!originalBalanceMap.containsKey(uuid) || !newBalanceMap.containsKey(uuid)) {
            // Data doesn't appear to be registered correctly. Assume an unchanged balance.
            return false;
        }
        return originalBalanceMap.get(uuid) != newBalanceMap.get(uuid);
    }

    /**
     * Get a player's current cached balance.
     *
     * @param uuid uuid of the player.
     * @return current balance.
     */
    public int getCachedBalance(UUID uuid) {
        if(newBalanceMap.containsKey(uuid)) {
            return newBalanceMap.get(uuid);
        }
        return -1;
    }

    /**
     * Update the cached balance of a player.
     *
     * @param uuid uuid of the player.
     * @param newBalance new balance of the player.
     */
    public void updateBalance(UUID uuid, int newBalance) {
        if(!newBalanceMap.containsKey(uuid)) {
            // Player isn't online (or something else went wrong), so balance won't be updated.
            return;
        }

        // Update balance in local cache.
        newBalanceMap.put(uuid, newBalance);
    }

}
