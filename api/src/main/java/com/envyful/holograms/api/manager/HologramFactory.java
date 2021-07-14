package com.envyful.holograms.api.manager;

import com.envyful.holograms.api.hologram.HologramBuilder;

/**
 *
 * Static factory for hologram methods
 *
 */
public class HologramFactory {

    private static PlatformHologramManager hologramManager;

    /**
     *
     * Sets the platform implementation for the Hologram Manager
     *
     * @param hologramManager The platform impl of the hologram manager
     */
    public static void setHologramManager(PlatformHologramManager hologramManager) {
        HologramFactory.hologramManager = hologramManager;
    }

    /**
     *
     * Creates an empty builder
     *
     * @return The new builder
     */
    public static HologramBuilder builder() {
        return hologramManager.builder();
    }

    /**
     *
     * Creates an empty builder with given id
     *
     * @param id The id of the new builder
     * @return The new builder
     */
    public static HologramBuilder builder(String id) {
        return hologramManager.builder(id);
    }

    /**
     *
     * Creates an empty builder with given lines
     *
     * @return The new builder
     */
    public static HologramBuilder builder(String... lines) {
        return hologramManager.builder(lines);
    }

    /**
     *
     * Creates an empty builder at given pos
     *
     * @return The new builder
     */
    public static HologramBuilder builder(String world, int x, int y, int z) {
        return hologramManager.builder(world, x, y, z);
    }

}
