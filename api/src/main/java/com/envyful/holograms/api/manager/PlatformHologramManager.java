package com.envyful.holograms.api.manager;

import com.envyful.holograms.api.hologram.HologramBuilder;

/**
 *
 * Interface implementation for creating instances of API interfaces
 *
 */
public interface PlatformHologramManager {

    /**
     *
     * Creates an empty builder
     *
     * @return The new builder
     */
    HologramBuilder builder();

    /**
     *
     * Creates an empty builder with given id
     *
     * @param id The id of the new builder
     * @return The new builder
     */
    HologramBuilder builder(String id);

    /**
     *
     * Creates an empty builder with given lines
     *
     * @return The new builder
     */
    HologramBuilder builder(String... lines);

    /**
     *
     * Creates an empty builder at given pos
     *
     * @return The new builder
     */
    HologramBuilder builder(String world, int x, int y, int z);

}
