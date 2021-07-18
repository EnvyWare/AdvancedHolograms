package com.envyful.holograms.api.manager.database;

import com.envyful.holograms.api.hologram.Hologram;

import java.util.List;
import java.util.Map;

/**
 *
 * Handles the saving and loading of all databases for the server from the storage
 *
 */
public interface HologramSaver {

    /**
     *
     * Loads all holograms
     *
     * @return The holograms
     */
    Map<String, Hologram> load();

    /**
     *
     * Saves all holograms
     *
     * @param holograms Cached holograms
     */
    void save(List<Hologram> holograms);

}
