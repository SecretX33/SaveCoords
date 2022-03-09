package com.github.secretx33.savecoords;

import com.github.secretx33.savecoords.commands.AddCommand;
import com.github.secretx33.savecoords.commands.ListCommand;
import com.github.secretx33.savecoords.commands.RemoveCommand;
import com.github.secretx33.savecoords.repository.CoordRepo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SaveCoords extends JavaPlugin {

    @Override
    public void onEnable() {
        File coordinatesFile = new File(getDataFolder(), "coordinates.json");
        CoordRepo coordRepo = new CoordRepo(coordinatesFile);
        new AddCommand(this, coordRepo);
        new ListCommand(this, coordRepo);
        new RemoveCommand(this, coordRepo);
        getServer().getConsoleSender().sendMessage("SaveCoords loaded");
    }
}
