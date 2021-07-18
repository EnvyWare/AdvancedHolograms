package com.envyful.holograms.forge.hologram;

import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.exception.HologramException;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.entity.HologramArmorStand;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;

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

    public ForgeHologram(String id, World world, Vec3d position, String... lines) {
        this.id = id;
        this.world = world;
        this.position = position;

        this.addLines(lines);
        HologramManager.addHologram(this);
        HologramManager.save();
    }

    @Override
    public void addLines(String... lines) {
        for (String line : lines) {
            this.addLine(line);
        }
    }

    @Override
    public void addLine(String line) {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isCallingFromMinecraftThread()) {
            UtilForgeConcurrency.runSync(() -> this.addLine(line));
            return;
        }

        HologramArmorStand armorStand = new HologramArmorStand(this.world, this.position.x,
                this.position.y - (HOLOGRAM_LINE_GAP * this.lines.size()), this.position.z);

        this.lines.add(armorStand);
        armorStand.setText(line);
        this.spawnLine(armorStand);

        HologramManager.save();
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

        HologramManager.save();
    }

    @Override
    public void setLine(int index, String text) {
        if (index > this.lines.size()) {
            this.addLine(text);
        } else {
            if (!FMLCommonHandler.instance().getMinecraftServerInstance().isCallingFromMinecraftThread()) {
                UtilForgeConcurrency.runSync(() -> {
                    this.lines.get(index - 1).setText(text);
                    HologramManager.save();
                });
            } else {
                this.lines.get(index - 1).setText(text);
                HologramManager.save();
            }
        }
    }

    @Override
    public void insertLine(int index, String line) {
        if (index > this.lines.size()) {
            this.addLine(line);
            return;
        }

        for (int i = (index - 1); i < this.lines.size(); ++i) {
            HologramArmorStand armorStand = this.lines.get(i);
            armorStand.setPosition(armorStand.posX, this.position.y - (HOLOGRAM_LINE_GAP * (i + 1)), armorStand.posZ);
        }

        UtilForgeConcurrency.runSync(() -> {
            HologramArmorStand newLine = new HologramArmorStand(this.world, this.position.x,
                    this.position.y - (HOLOGRAM_LINE_GAP * (index - 1)), this.position.z);
            newLine.setText(line);
            this.lines.add(index - 1, newLine);

            for (UUID nearbyPlayer : this.nearbyPlayers) {
                EntityPlayerMP player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                if (player == null) {
                    continue;
                }

                newLine.spawnForPlayer(player);

                for (int i = (index - 1); i < this.lines.size(); ++i) {
                    HologramArmorStand armorStand = this.lines.get(i);
                    player.connection.sendPacket(new SPacketEntityTeleport(armorStand));
                }
            }

            HologramManager.save();
        });
    }

    @Override
    public void removeLines(int... indexes) throws HologramException {
        for (int index : indexes) {
            this.removeLine(index);
        }
    }

    @Override
    public void removeLine(int index) throws HologramException {
        if (lines.size() == 1) {
            throw new HologramException("§4Cannot remove anymore lines as there's only one left! To delete use §7/hd delete " + this.id);
        }

        if (index > this.lines.size()) {
            throw new HologramException("§4Cannot remove that line as it's out of the bounds of this hologram.");
        }

        HologramArmorStand remove = this.lines.remove(index - 1);

        for (int i = (index - 1); i < this.lines.size(); ++i) {
            HologramArmorStand armorStand = this.lines.get(i);
            armorStand.setPosition(armorStand.posX, this.position.y - (HOLOGRAM_LINE_GAP * i), armorStand.posZ);
        }

        UtilForgeConcurrency.runSync(() -> {
            for (UUID nearbyPlayer : this.nearbyPlayers) {
                EntityPlayerMP player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                if (player == null) {
                    continue;
                }

                remove.despawnForPlayer(player);

                for (HologramArmorStand line : this.lines) {
                    player.connection.sendPacket(new SPacketEntityTeleport(line));
                }
            }
        });

        HologramManager.save();
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
        HologramManager.save();
    }

    @Override
    public void teleport(String worldName, double x, double y, double z) {
        World world = UtilWorld.findWorld(worldName);

        if (world == null) {
            return;
        }

        this.world = world;
        this.position = new Vec3d(x, y, z);

        int i = 0;

        for (HologramArmorStand line : this.lines) {
            line.setWorld(world);
            line.setPosition(x, y - (i * HOLOGRAM_LINE_GAP), z);
            ++i;

            UtilForgeConcurrency.runSync(() -> {
                for (UUID nearbyPlayer : this.nearbyPlayers) {
                    EntityPlayerMP player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                    if (player == null) {
                        continue;
                    }

                    player.connection.sendPacket(new SPacketEntityTeleport(line));
                }
            });
        }

        HologramManager.save();
    }

    @Override
    public Hologram copy(String newId, String world, double x, double y, double z) {
        return new ForgeHologramBuilder().id(newId).world(world).position(x, y, z)
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

    public String getId() {
        return this.id;
    }

    public World getWorld() {
        return this.world;
    }

    public Vec3d getPosition() {
        return this.position;
    }

    List<HologramArmorStand> getLines() {
        return this.lines;
    }

    List<UUID> getNearbyPlayers() {
        return this.nearbyPlayers;
    }

    public double getDistance(EntityPlayerMP player) {
        return this.position.distanceTo(player.getPositionVector());
    }

    public boolean inRadius(EntityPlayerMP player, int radius) {
        return this.position.squareDistanceTo(player.posX, player.posY, player.posZ) <= Math.pow(radius, 2);
    }
}
