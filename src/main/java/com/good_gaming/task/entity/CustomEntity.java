package com.good_gaming.task.entity;

import org.bukkit.entity.EntityType;

/**
 * Class which could be useful in the future for the management of custom entities.
 *
 * Created by Josh (MacBook).
 */
public interface CustomEntity {

    /**
     * Get the Minecraft version this custom entity is aimed at.
     *
     * @return version string.
     */
    String targetVersion();

    /**
     * Get the Bukkit EntityType of the custom entity.
     *
     * @return enum EntityType value.
     */
    EntityType getEntityType();

}