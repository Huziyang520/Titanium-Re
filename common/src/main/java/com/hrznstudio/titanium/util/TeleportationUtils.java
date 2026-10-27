// Titanium Lite Teleportation Utils

package com.hrznstudio.titanium.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class TeleportationUtils {

    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        return teleportEntityTo(entity, new Vector3f((float) xCoord, (float) yCoord, (float) zCoord), dimension, yaw, pitch);
    }

    public static Entity teleportEntityTo(Entity entity, Vector3f target, ResourceKey<Level> destinationDimension, float yaw, float pitch) {
        if (entity.level().dimension() == destinationDimension) {
            entity.setYRot(yaw);
            entity.setXRot(pitch);
            entity.teleportTo(target.x() + 0.5, target.y(), target.z() + 0.5);

            return entity;
        } else {
            ServerLevel newWorld = ((ServerLevel) entity.level()).getServer().getLevel(destinationDimension);
            if (newWorld != null) {
                Vec3 destination = new Vec3(target.x() + 0.5, target.y(), target.z() + 0.5);
                var transition = new TeleportTransition(
                    newWorld, destination, entity.getDeltaMovement(), yaw, pitch, TeleportTransition.DO_NOTHING);
                return entity.teleport(transition);
            }
        }
        return null;
    }

    private static void teleportPassenger(ServerLevel destWorld, Entity repositionedEntity, Entity passenger) {

        List<Entity> passengers = passenger.getPassengers();
        passenger.teleport(new TeleportTransition(
            destWorld, repositionedEntity.position(), passenger.getDeltaMovement(), passenger.getYRot(), passenger.getXRot(), et -> {

            et.startRiding(repositionedEntity, true, true);

            for (Entity pas : passengers) {
                teleportPassenger(destWorld, et, pas);
            }
        }));
    }

}
