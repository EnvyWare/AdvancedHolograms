package com.envyful.holograms.forge.hologram;

import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.entity.HologramArmorStand;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * Forge implementation of the {@link Hologram} interface
 *
 */
public class ForgeHologram implements Hologram {

    private static final double HOLOGRAM_LINE_GAP = 0.25;

    private String id;
    private World world;
    private Vec3d position;
    private List<HologramArmorStand> lines = Lists.newArrayList();

    private final List<UUID> nearbyPlayers = Lists.newArrayList();

    public ForgeHologram(World world, Vec3d position, String... lines) {
        this.world = world;
        this.position = position;

        this.addLines(lines);
        HologramManager.addHologram(this);
    }

    @Override
    public void addLines(String... lines) {
        for (String line : lines) {
            this.addLine(line);
        }
    }

    @Override
    public void addLine(String line) {
        HologramArmorStand armorStand = new HologramArmorStand(this.world, this.position.x,
                this.position.y - (HOLOGRAM_LINE_GAP * this.lines.size()), this.position.z);

        this.lines.add(armorStand);
        armorStand.setText(line);
        this.spawnLine(armorStand);
    }

    @Override
    public void move(String world, double x, double y, double z) {
        World foundWorld = UtilWorld.findWorld(world);

        if (foundWorld == null) {
            return;
        }

        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for (HologramArmorStand line : this.lines) {
            for (EntityPlayerMP player : playerList.getPlayers()) {
                line.despawnForPlayer(player);
            }
        }

        for (HologramArmorStand line : this.lines) {
            line.setWorld(foundWorld);
            line.setPosition(x, y, z);
        }
    }

    @Override
    public void setLine(int index, String text) {
        if (index >= this.lines.size()) {
            return;
        }

        this.lines.get(index).setText(text);
    }

    @Override
    public void removeLines(int... indexes) {
        for (int index : indexes) {
            this.removeLine(index);
        }
    }

    @Override
    public void removeLine(int index) {
        if (index >= this.lines.size()) {
            return;
        }

        HologramArmorStand remove = this.lines.remove(index);

        for (int i = index; i < this.lines.size(); ++i) {
            HologramArmorStand armorStand = this.lines.get(i);
            armorStand.setPosition(armorStand.posX, this.position.y - (HOLOGRAM_LINE_GAP * i), armorStand.posY);
        }
    }

    @Override
    public void delete() {
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for (HologramArmorStand line : this.lines) {
            for (EntityPlayerMP player : playerList.getPlayers()) {
                line.despawnForPlayer(player);
            }
        }

        HologramManager.removeHologram(this);
    }

    @Override
    public Hologram copy(String world, double x, double y, double z) {
        return new ForgeHologramBuilder().world(world).position(x, y, z)
                .lines(this.lines.stream().map(e -> e.getDisplayName().getFormattedText()).toArray(String[]::new))
                .build();
    }

    private void spawnLine(HologramArmorStand armorStand) {
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for (UUID nearbyPlayer : this.nearbyPlayers) {
            EntityPlayerMP player = playerList.getPlayerByUUID(nearbyPlayer);
            armorStand.spawnForPlayer(player);
        }
    }

    String getId() {
        return this.id;
    }

    World getWorld() {
        return this.world;
    }

    Vec3d getPosition() {
        return this.position;
    }

    List<HologramArmorStand> getLines() {
        return this.lines;
    }

    List<UUID> getNearbyPlayers() {
        return this.nearbyPlayers;
    }
}
