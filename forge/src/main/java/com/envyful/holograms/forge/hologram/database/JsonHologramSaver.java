package com.envyful.holograms.forge.hologram.database;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.database.HologramSaver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
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

        return null;
    }

    @Override
    public void save(List<Hologram> holograms) {

    }
}
