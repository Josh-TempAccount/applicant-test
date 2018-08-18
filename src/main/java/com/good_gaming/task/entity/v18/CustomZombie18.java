package com.good_gaming.task.entity.v18;

import com.good_gaming.task.entity.CustomEntity;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.EntityType;

/**
 * A class to show my ability to extend a native minecraft entity.
 * This is for 1.8 - multi version support is made possible by having multiple server versions as dependencies in
 * the pom.xml.
 *
 * Created by Josh (MacBook).
 */
public class CustomZombie18 extends EntityZombie implements CustomEntity {

    public CustomZombie18(World world) {
        super(world);
    }

    // Do custom things with mob here.

    @Override
    public String targetVersion() {
        return "v1_8";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE;
    }

}
