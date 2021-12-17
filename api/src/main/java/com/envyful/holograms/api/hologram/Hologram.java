package com.envyful.holograms.api.hologram;

import com.envyful.holograms.api.exception.HologramException;

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
     * Inserts the new line above the line number given
     *
     * @param lineNum The line number to insert above
     * @param line The line to insert
     */
    void insertLine(int lineNum, String line);

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
     * @throws HologramException When index is out of bounds
     */
    void removeLine(int index) throws HologramException;

    /**
     *
     * Removes the lines from the hologram
     *
     * @param indexes The lines to remove
     * @throws HologramException When index is out of bounds
     */
    void removeLines(int... indexes) throws HologramException;

    /**
     *
     * Deletes the hologram from the world
     *
     */
    void delete();

    /**
     *
     * Teleports the hologram to the x, y, z coords in the given world
     *
     * @param worldName The new world
     * @param x The new x
     * @param y The new y
     * @param z The new z
     */
    void teleport(String worldName, double x, double y, double z);

    /**
     *
     * Copies the hologram's lines to a new hologram at the specified position
     *
     * @param newId the new Id
     * @param world The world to copy it to
     * @param x The new X to copy it to
     * @param y The new Y to copy it to
     * @param z The new Z to copy it to
     * @return The new hologram instance
     */
    Hologram copy(String newId, String world, double x, double y, double z);

    /**
     *
     * Despawns the hologram for all players
     *
     */
    void despawn();

}
