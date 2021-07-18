package com.envyful.holograms.forge.hologram.database;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.database.HologramSaver;
import com.envyful.holograms.forge.hologram.ForgeHologram;
import com.envyful.holograms.forge.hologram.ForgeHologramTypeAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Json implementation of the {@link HologramSaver} interface
 *
 */
public class JsonHologramSaver implements HologramSaver {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ForgeHologram.class, new ForgeHologramTypeAdapter())
            .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .create();

    private File file;

    public JsonHologramSaver(String file) {
        UtilConcurrency.runAsync(() -> {
            this.file = Paths.get(file).toFile();

            try {
                if (!this.file.exists()) {
                    if (!this.file.getParentFile().exists()) {
                        this.file.getParentFile().mkdirs();
                    }

                    this.file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, Hologram> load() {
        Map<String, Hologram> holograms = Maps.newHashMap();

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(this.file));
            List<ForgeHologram> forgeHolograms = GSON.fromJson(jsonReader, ArrayList.class);

            if (forgeHolograms == null) {
                return holograms;
            }

            forgeHolograms.forEach(hologram -> holograms.put(hologram.getId().toLowerCase(), hologram));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return holograms;
    }

    @Override
    public void save(List<Hologram> holograms) {
        try {
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(this.file));
            List<ForgeHologram> savedHolograms = Lists.newArrayList();

            for (Hologram hologram : holograms) {
                if (!(hologram instanceof ForgeHologram)) {
                    continue;
                }

                savedHolograms.add((ForgeHologram) hologram);
            }

            GSON.toJson(savedHolograms, ArrayList.class, jsonWriter);
            jsonWriter.flush();
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
