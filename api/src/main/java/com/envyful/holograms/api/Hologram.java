package com.envyful.holograms.api;

/**
 *
 * An interface representing a server hologram
 *
 */
public interface Hologram {

    /**
     *
     * Moves the hologram to the given coordinates in the world with the given name.
     *
     * @param world Name of the new world
     * @param x X pos to move it to
     * @param y Y pos to move it to
     * @param z Z pos to move it to
     */
    void move(String world, double x, double y, double z);

    /**
     *
     * Sets the line at the given index to the given text
     *
     * @param index The line to set
     * @param text The text to set the line to
     */
    void setLine(int index, String text);

    /**
     *
     * Adds a line to the hologram with the given text
     *
     * @param line The new line for the hologram
     */
    void addLine(String line);

    /**
     *
     * Adds the given lines to the hologram
     *
     * @param lines The lines to add
     */
    void addLines(String... lines);

    /**
     *
     * Removes a line from the hologram
     *
     * @param index The index of the line to remove
     */
    void removeLine(int index);

    /**
     *
     * Removes the lines from the hologram
     *
     * @param indexes The lines to remove
     */
    void removeLines(int... indexes);

    /**
     *
     * Deletes the hologram from the world
     *
     */
    void delete();

    /**
     *
     * Copies the hologram's lines to a new hologram at the specified position
     *
     * @param world The world to copy it to
     * @param x The new X to copy it to
     * @param y The new Y to copy it to
     * @param z The new Z to copy it to
     * @return The new hologram instance
     */
    Hologram copy(String world, double x, double y, double z);

}
