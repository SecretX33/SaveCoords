package com.github.secretx33.savecoords.repository;

import com.github.secretx33.savecoords.model.Coordinate;
import com.github.secretx33.savecoords.utils.FileUtils;
import com.google.common.collect.SetMultimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class CoordRepo {

    private final SetMultimap<String, Coordinate> coords;
    private final File coordFile;

    public CoordRepo(File coordinatesFile) {
        this.coordFile = checkNotNull(coordinatesFile);
        this.coords = FileUtils.load(coordinatesFile);
    }

    public boolean hasCoord(Player player, Location location) {
        return coords.containsKey(player.getName()) && coords.get(player.getName()).stream().anyMatch(coord -> coord.isAt(location));
    }

    public boolean hasCoord(Player player, String coordinateName) {
        return coords.containsKey(player.getName()) && coords.get(player.getName()).stream().anyMatch(coord -> coord.getName().equalsIgnoreCase(coordinateName));
    }

    public synchronized void addCoord(Player player, Coordinate coordinate) {
        coords.put(player.getName(), coordinate);
        FileUtils.save(coordFile, coords);
    }

    public synchronized void removeCoord(Player player, String coordinateName) {
        Set<Coordinate> playerCoords = coords.get(player.getName());
        boolean removed = playerCoords.removeIf(coord -> coord.getName().equalsIgnoreCase(coordinateName));
        if (removed) FileUtils.save(coordFile, coords);
    }

    public Set<Coordinate> getAllOf(Player player) {
        return coords.get(player.getName());
    }

    public List<String> getNamesOfAll(Player player) {
        return getAllOf(player).stream()
                .map(Coordinate::getName)
                .collect(Collectors.toList());
    }
}
