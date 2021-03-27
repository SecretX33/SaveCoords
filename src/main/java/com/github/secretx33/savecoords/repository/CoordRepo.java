package com.github.secretx33.savecoords.repository;

import com.github.secretx33.savecoords.model.Coordinate;
import com.github.secretx33.savecoords.utils.FileUtils;
import com.google.common.collect.ListMultimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class CoordRepo {

    private final ListMultimap<String, Coordinate> coords;
    private final File coordFile;

    public CoordRepo(File coordinatesFile) {
        checkNotNull(coordinatesFile);
        this.coordFile = coordinatesFile;
        coords = FileUtils.load(coordinatesFile);
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
        if(!coords.containsKey(player.getName())) return;

        if(coords.get(player.getName()).removeIf(coord -> coord.getName().equalsIgnoreCase(coordinateName))) {
            System.out.println("Deleting " + coordinateName);
            FileUtils.save(coordFile, coords);
        }
    }

    public List<String> getAllOf(Player player) {
        if(!coords.containsKey(player.getName())) return new ArrayList<>();
        return coords.get(player.getName()).stream().map(Coordinate::getName).collect(Collectors.toList());
    }
}
