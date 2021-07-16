package com.envyful.holograms.forge.hologram;

import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.entity.HologramArmorStand;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * Static factory manager for all {@link Hologram}s on the server
 *
 */
public class HologramManager implements Runnable {

    static {
        new ForgeTaskBuilder()
                .async(true)
                .interval(10L)
                .delay(10L)
                .task(new HologramManager())
                .start();
    }

    private static final Map<String, ForgeHologram> HOLOGRAMS = Maps.newConcurrentMap();

    public static void load() {} //TODO:

    public static void save() {} //TODO:

    public static void addHologram(ForgeHologram hologram) {
        HOLOGRAMS.put(hologram.getId().toLowerCase(), hologram);
    }

    public static void removeHologram(ForgeHologram hologram) {
        HOLOGRAMS.remove(hologram.getId().toLowerCase());
    }

    public static List<Hologram> getAllHolograms() {
        return Collections.unmodifiableList(Lists.newArrayList(HOLOGRAMS.values()));
    }

    @Override
    public void run() {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            for (ForgeHologram hologram : HOLOGRAMS.values()) {
                if (!hologram.getWorld().equals(player.world)) {
                    continue;
                }

                if (player.getDistanceSq(new BlockPos(hologram.getPosition())) > 4096) {
                    if (hologram.getNearbyPlayers().contains(player.getUniqueID())) {
                        hologram.getNearbyPlayers().remove(player.getUniqueID());

                        for (HologramArmorStand line : hologram.getLines()) {
                            UtilForgeConcurrency.runSync(() -> line.despawnForPlayer(player));
                        }
                    }

                    continue;
                }

                if (!hologram.getNearbyPlayers().contains(player.getUniqueID())) {
                    for (HologramArmorStand line : hologram.getLines()) {
                        UtilForgeConcurrency.runSync(() -> line.spawnForPlayer(player));
                    }

                    hologram.getNearbyPlayers().add(player.getUniqueID());
                } else {
                    for (HologramArmorStand line : hologram.getLines()) {
                        UtilForgeConcurrency.runSync(() -> line.updateForPlayer(player));
                    }
                }
            }
        }
    }
}
