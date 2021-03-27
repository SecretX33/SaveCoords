package com.github.secretx33.savecoords.utils;

import com.github.secretx33.savecoords.model.Coordinate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class FileUtils {

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
            if(!file.exists()) file.createNewFile();
            System.out.println("New array is " + gson.toJson(list.asMap(), mapToken));
            try(FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(gson.toJson(list.asMap(), mapToken));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ListMultimap<String, Coordinate> load(File file) {
        checkNotNull(file);
        if(!file.exists()) return MultimapBuilder.hashKeys().arrayListValues().build();

        try(Reader reader = new BufferedReader(new FileReader(file))) {
            Map<String, Collection<Coordinate>> map = gson.fromJson(reader, mapToken);
            if(map == null) return MultimapBuilder.hashKeys().arrayListValues().build();

            ListMultimap<String, Coordinate> multimap = ArrayListMultimap.create();
            for (String key : map.keySet()) {
                multimap.putAll(key, map.get(key));
            }
            return multimap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return MultimapBuilder.hashKeys().arrayListValues().build();
    }
}
