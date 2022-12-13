package com.envyful.advanced.holograms.forge.hologram;

import com.envyful.advanced.holograms.forge.hologram.entity.HologramLine;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.exception.HologramException;
import com.envyful.holograms.api.hologram.Hologram;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;

/**
 *
 * Forge implementation of the {@link Hologram} interface
 *
 */
public class ForgeHologram implements Hologram {

    private static final double HOLOGRAM_LINE_GAP = 0.25;

    private final String id;

    private Level world;
    private Vec3 position;
    private int range;

    private final List<HologramLine> lines = Lists.newArrayList();
    private final List<UUID> nearbyPlayers = Lists.newArrayList();

    public ForgeHologram(String id, Level world, Vec3 position, int range, boolean save, String... lines) {
        this.id = id;
        this.world = world;
        this.position = position;
        this.range = range;

        this.addLines(save, lines);
        HologramManager.addHologram(this);

        if (save) {
            HologramManager.save();
        }
    }

    @Override
    public void addLines(String... lines) {
        this.addLines(true, lines);
    }

    private void addLines(boolean save, String... lines) {
        if (!ServerLifecycleHooks.getCurrentServer().isSameThread()) {
            UtilForgeConcurrency.runSync(() -> this.addLines(lines));
            return;
        }

        for (String line : lines) {
            this.addLine(line, save);
        }
    }

    @Override
    public void addLine(String line) {
        this.addLine(line, true);
    }

    private void addLine(String line, boolean save) {
        if (!ServerLifecycleHooks.getCurrentServer().isSameThread()) {
            UtilForgeConcurrency.runSync(() -> this.addLine(line));
            return;
        }

        HologramLine armorStand = new HologramLine(new ArmorStand(this.world, this.position.x,
                this.position.y - (HOLOGRAM_LINE_GAP * this.lines.size()), this.position.z));

        this.lines.add(armorStand);
        armorStand.setText(line);
        this.spawnLine(armorStand);

        if (save) {
            HologramManager.save();
        }
    }

    @Override
    public void move(String world, double x, double y, double z) {
        Level foundWorld = UtilWorld.findWorld(world);

        if (foundWorld == null) {
            return;
        }

        PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for (HologramLine line : this.lines) {
            for (ServerPlayer player : playerList.getPlayers()) {
                line.despawnForPlayer(player);
            }
        }

        for (HologramLine line : this.lines) {
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
            if (!ServerLifecycleHooks.getCurrentServer().isSameThread()) {
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
            HologramLine armorStand = this.lines.get(i);
            armorStand.setPosition(this.position.x, this.position.y - (HOLOGRAM_LINE_GAP * (i + 1)), this.position.z);
        }

        UtilForgeConcurrency.runSync(() -> {
            HologramLine newLine = new HologramLine(new ArmorStand(this.world, this.position.x,
                    this.position.y - (HOLOGRAM_LINE_GAP * (index - 1)), this.position.z));
            newLine.setText(line);
            this.lines.add(index - 1, newLine);

            for (UUID nearbyPlayer : this.nearbyPlayers) {
                ServerPlayer player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                if (player == null) {
                    continue;
                }

                newLine.spawnForPlayer(player);

                for (int i = (index - 1); i < this.lines.size(); ++i) {
                    HologramLine armorStand = this.lines.get(i);
                    armorStand.sendTeleportPacket(player);
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

        HologramLine remove = this.lines.remove(index - 1);

        for (int i = (index - 1); i < this.lines.size(); ++i) {
            HologramLine armorStand = this.lines.get(i);
            armorStand.setPosition(this.position.x, this.position.y - (HOLOGRAM_LINE_GAP * i), this.position.y);
        }

        UtilForgeConcurrency.runSync(() -> {
            for (UUID nearbyPlayer : this.nearbyPlayers) {
                ServerPlayer player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                if (player == null) {
                    continue;
                }

                remove.despawnForPlayer(player);

                for (HologramLine line : this.lines) {
                    line.sendTeleportPacket(player);
                }
            }
        });

        HologramManager.save();
    }

    @Override
    public void delete() {
        this.despawn();
        HologramManager.removeHologram(this);
        HologramManager.save();
    }

    @Override
    public void despawn() {
        PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for (HologramLine line : this.lines) {
            for (ServerPlayer player : playerList.getPlayers()) {
                line.despawnForPlayer(player);
            }
        }
    }

    @Override
    public void teleport(String worldName, double x, double y, double z) {
        Level world = UtilWorld.findWorld(worldName);

        if (world == null) {
            return;
        }

        this.world = world;
        this.position = new Vec3(x, y, z);

        int i = 0;

        for (HologramLine line : this.lines) {
            line.setWorld(world);
            line.setPosition(x, y - (i * HOLOGRAM_LINE_GAP), z);
            ++i;

            UtilForgeConcurrency.runSync(() -> {
                for (UUID nearbyPlayer : this.nearbyPlayers) {
                    ServerPlayer player = UtilPlayer.getOnlinePlayer(nearbyPlayer);

                    if (player == null) {
                        continue;
                    }

                    line.sendTeleportPacket(player);
                }
            });
        }

        HologramManager.save();
    }

    @Override
    public Hologram copy(String newId, String world, double x, double y, double z) {
        return new ForgeHologramBuilder().id(newId).world(world).position(x, y, z)
                .lines(this.lines.stream().map(HologramLine::getText).toArray(String[]::new))
                .range(64)
                .build();
    }

    private void spawnLine(HologramLine armorStand) {
        PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for (UUID nearbyPlayer : this.nearbyPlayers) {
            ServerPlayer player = playerList.getPlayer(nearbyPlayer);
            armorStand.spawnForPlayer(player);
        }
    }

    public String getId() {
        return this.id;
    }

    public Level getWorld() {
        return this.world;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    List<HologramLine> getLines() {
        return this.lines;
    }

    public int getRange() {
        return this.range;
    }

    List<UUID> getNearbyPlayers() {
        return this.nearbyPlayers;
    }

    public double getDistance(ServerPlayer player) {
        return this.position.distanceTo(player.getPosition(10));
    }

    public boolean inRadius(ServerPlayer player, int radius) {
        return this.position.distanceToSqr(player.getX(), player.getY(), player.getZ()) <= Math.pow(radius, 2);
    }
}
