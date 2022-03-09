package com.github.secretx33.savecoords.utils;

import com.github.secretx33.savecoords.SaveCoords;
import com.github.secretx33.savecoords.model.Coordinate;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("UnstableApiUsage")
public class FileUtils {

    private static final Logger logger = JavaPlugin.getPlugin(SaveCoords.class).getLogger();
    private static final Type mapToken = new TypeToken<Map<String, Collection<Coordinate>>>() {}.getType();
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();

    public static void save(File file, Multimap<String, Coordinate> list) {
        checkNotNull(file);
        checkNotNull(list);
        try {
            com.google.common.io.Files.createParentDirs(file);
            if (!file.exists()) file.createNewFile();
            try(FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(gson.toJson(list.asMap(), mapToken));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save file: " + file.getName(), e);
        }
    }

    public static SetMultimap<String, Coordinate> load(File file) {
        checkNotNull(file);
        if (!file.exists()) return newMultimap();

        try(FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
            Map<String, Collection<Coordinate>> map = gson.fromJson(reader, mapToken);
            if (map == null) return newMultimap();

            SetMultimap<String, Coordinate> multimap = newMultimap();
            for (String key : map.keySet()) {
                multimap.putAll(key, map.get(key));
            }
            return multimap;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load file: " + file.getName(), e);
        }
        return newMultimap();
    }

    private static SetMultimap<String, Coordinate> newMultimap() {
        return MultimapBuilder.hashKeys().hashSetValues().build();
    }
}
