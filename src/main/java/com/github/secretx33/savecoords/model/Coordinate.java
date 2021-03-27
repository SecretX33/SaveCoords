package com.github.secretx33.savecoords.model;

import org.bukkit.Location;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class Coordinate {

    private String name;
    private String description;
    private String world;
    private int x;
    private int y;
    private int z;

    public Coordinate(String name, String description, String world, int x, int y, int z) {
        this.name = name;
        this.description = description;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate(String name, String description, Location location) {
        checkNotNull(location.getWorld(), "location cannot have world attribute null");
        checkNotNull(location.getWorld().getName(), "world name cannot be null");

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
        return name.equalsIgnoreCase(that.name)
                || x == that.x && y == that.y && z == that.z && world.equalsIgnoreCase(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, world, x, y, z);
    }
}
