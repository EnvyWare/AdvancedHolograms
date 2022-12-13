package com.envyful.advanced.holograms.forge.hologram;

import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.hologram.HologramBuilder;
import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 *
 * The forge implementation of the {@link HologramBuilder} interface
 *
 */
public class ForgeHologramBuilder implements HologramBuilder {

    private String id;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private int range;
    private List<String> lines = Lists.newArrayList();

    public ForgeHologramBuilder() {}

    @Override
    public HologramBuilder id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public HologramBuilder world(String worldName) {
        this.worldName = worldName;
        return this;
    }

    @Override
    public HologramBuilder position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public HologramBuilder range(int range) {
        this.range = range;
        return this;
    }

    @Override
    public HologramBuilder line(String line) {
        this.lines.add(line);
        return this;
    }

    @Override
    public HologramBuilder lines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    @Override
    public Hologram build(boolean save) {
        Level world = UtilWorld.findWorld(this.worldName);

        if (world == null) {
            System.out.println("ERROR THE WORLD CANNOT BE FOUND");
            return null;
        }

        Vec3 pos = new Vec3((float) this.x, (float) this.y, (float) this.z);
        return new ForgeHologram(this.id, world, pos, this.range, save, this.lines.toArray(new String[0]));
    }
}
