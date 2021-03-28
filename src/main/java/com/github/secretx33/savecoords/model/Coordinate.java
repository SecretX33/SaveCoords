package com.github.secretx33.savecoords.model;

import org.bukkit.Location;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class Coordinate {

    private final String name;
    private final String description;
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public Coordinate(String name, String description, String world, int x, int y, int z) {
        checkNotNull(name, "name cannot be null");
        checkNotNull(description, "description cannot be null");
        checkNotNull(world, "world cannot be null");

        this.name = name;
        this.description = description;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate(String name, String description, Location location) {
        checkNotNull(name, "name cannot be null");
        checkNotNull(description, "description cannot be null");
        checkNotNull(location, "location cannot be null");
        checkNotNull(location.getWorld(), "location cannot have world attribute null");

        this.name = name;
        this.description = description;
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isAt(Location location) {
        if(location.getWorld() == null) return false;

        return location.getBlockX() == x
                && location.getBlockY() == y
                && location.getBlockZ() == z
                && location.getWorld().getName().equalsIgnoreCase(world);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && z == that.z && name.equalsIgnoreCase(that.name) && world.equalsIgnoreCase(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }
}
