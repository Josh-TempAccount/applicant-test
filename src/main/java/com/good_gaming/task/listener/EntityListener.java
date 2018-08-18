package com.good_gaming.task.listener;

import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Class to handle all entity related (but non-player) events.
 *
 * Created by Josh (MacBook).
 */
public class EntityListener implements Listener {

    /**
     * Event handler to stop hostile mobs spawning.
     *
     * The brief says 'the highest possible priority', which is MONITOR - however this is known to be
     * bad practice. As this is what the brief requests, I've left it as MONITOR - but if this were to be
     * used in production, the priority should ideally be set to HIGHEST.
     *
     * @param e the CreatureSpawnEvent that's being called.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(CreatureSpawnEvent e) {
        if(e.getEntity() instanceof Monster) {
            e.setCancelled(true);
        }
    }

}
