package com.envyful.holograms.forge.hologram.manager;

import com.envyful.holograms.api.hologram.HologramBuilder;
import com.envyful.holograms.api.manager.PlatformHologramManager;
import com.envyful.holograms.forge.hologram.ForgeHologramBuilder;

/**
 *
 * The forge implementation of the {@link PlatformHologramManager} interface
 *
 */
public class ForgeHologramManager implements PlatformHologramManager {

    @Override
    public HologramBuilder builder() {
        return new ForgeHologramBuilder();
    }

    @Override
    public HologramBuilder builder(String id) {
        return new ForgeHologramBuilder().id(id);
    }

    @Override
    public HologramBuilder builder(String... lines) {
        HologramBuilder builder = new ForgeHologramBuilder();
        builder.lines(lines);
        return builder;
    }

    @Override
    public HologramBuilder builder(String world, int x, int y, int z) {
        HologramBuilder builder = new ForgeHologramBuilder();
        builder.position(x, y, z);
        return builder;
    }
}
