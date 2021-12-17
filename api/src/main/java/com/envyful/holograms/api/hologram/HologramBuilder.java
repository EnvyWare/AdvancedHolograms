package com.envyful.holograms.api.hologram;

/**
 *
 * Interface representing all builders for the {@link Hologram} interface
 *
 */
public interface HologramBuilder {

    /**
     *
     * Sets the id for the {@link Hologram}
     *
     * @param id The id of the hologram
     * @return The builder
     */
    HologramBuilder id(String id);

    /**
     *
     * Sets the world stored in the builder
     *
     * @param worldName The name of the world
     * @return The builder
     */
    HologramBuilder world(String worldName);

    /**
     *
     * Sets the position stored in the builder
     *
     * @param x The x position stored
     * @param y The y position stored
     * @param z The z position stored
     * @return The builder
     */
    HologramBuilder position(double x, double y, double z);

    /**
     *
     * Sets the view radius of the hologram
     *
     * @param range The view range
     * @return The builder
     */
    HologramBuilder range(int range);

    /**
     *
     * Adds a single line to the hologram
     *
     * @param line The line to add
     * @return The builder
     */
    HologramBuilder line(String line);

    /**
     *
     * Adds multiple lines to the hologram
     *
     * @param lines The lines to add
     * @return The builder
     */
    HologramBuilder lines(String... lines);

    /**
     *
     * Builds the hologram from the specified values
     *
     * @return The hologram created
     */
    default Hologram build() {
        return build(true);
    }

    /**
     *
     * Builds the hologram from the specified values
     *
     * @param save Determines if the hologram should be saved when built
     * @return The hologram created
     */
    Hologram build(boolean save);

}
