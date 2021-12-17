package com.envyful.holograms.forge.hologram.database;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.hologram.HologramBuilder;
import com.envyful.holograms.api.manager.HologramFactory;
import com.envyful.holograms.api.manager.database.HologramSaver;
import com.envyful.holograms.forge.hologram.ForgeHologram;
import com.envyful.holograms.forge.hologram.ForgeHologramTypeAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
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
            InputStreamReader jsonReader = new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8);
            List<LinkedTreeMap<String, Object>> forgeHolograms = GSON.fromJson(jsonReader, ArrayList.class);

            if (forgeHolograms == null) {
                return holograms;
            }

            forgeHolograms.forEach(data -> {
                ForgeHologram hologram = this.fromTreeMap(data);
                holograms.put(hologram.getId().toLowerCase(), hologram);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return holograms;
    }

    private ForgeHologram fromTreeMap(LinkedTreeMap<String, Object> map) {
        LinkedTreeMap<String, Object> loc = (LinkedTreeMap<String, Object>) map.get("loc");
        List<String> lines = (List<String>) map.get("lines");

        HologramBuilder builder = HologramFactory.builder()
                .id(map.get("id").toString())
                .position((double) loc.get("x"), (double) loc.get("y"), (double) loc.get("z"))
                .world(loc.get("world").toString())
                .lines(lines.toArray(new String[0]));

        int range = 64;

        if (map.containsKey("range")) {
            range = (int) ((double) map.get("range"));

            if (range <= 0) {
                range = 64;
            }
        }

        return (ForgeHologram) builder.range(range).build(false);
    }

    @Override
    public void save(List<Hologram> holograms) {
        try {
            OutputStreamWriter jsonWriter = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8);
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
