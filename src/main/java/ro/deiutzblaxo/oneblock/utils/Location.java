package ro.deiutzblaxo.oneblock.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Objects;

@Getter
@Setter
public class Location {
    double X;
    double Y;
    double Z;
    float pitch;
    float yaw;

    public Location(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }

    public Location(double x, double y, double z, float pitch, float yaw) {
        X = x;
        Y = y;
        Z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location(org.bukkit.Location location) {
        X = location.getX();
        Y = location.getY();
        Z = location.getZ();
        pitch = location.getPitch();
        yaw = location.getYaw();
    }

    public org.bukkit.Location toBukkitLocation(World world) {
        return Location.toBukkitLocation(world, this);
    }

    public static org.bukkit.Location toBukkitLocation(World world, Location location) {
        org.bukkit.Location loc = new org.bukkit.Location(world, location.X, location.Y, location.Z);
        loc.setPitch(location.pitch);
        loc.setYaw(location.yaw);
        return loc;
    }

    public static Location toLocation(org.bukkit.Location location) {
        return new Location(location);
    }

    public static boolean isSafeLocation(org.bukkit.Location location) {
        try {
            Block feet = location.getBlock();
            if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
                return false; // not transparent (will suffocate)
            }
            Block head = feet.getRelative(BlockFace.UP);
            if (!head.getType().isTransparent()) {
                return false; // not transparent (will suffocate)
            }
            Block ground = feet.getRelative(BlockFace.DOWN);
            // returns if the ground is solid or not.
            return ground.getType().isSolid();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.X, X) == 0 && Double.compare(location.Y, Y) == 0 && Double.compare(location.Z, Z) == 0 && Float.compare(location.pitch, pitch) == 0 && Float.compare(location.yaw, yaw) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y, Z, pitch, yaw);
    }

    @Override
    public String toString() {
        return "Location{" +
                "X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                '}';
    }
}
