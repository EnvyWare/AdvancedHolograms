package com.envyful.advanced.holograms.forge.hologram;

import com.envyful.advanced.holograms.forge.ForgeHolograms;
import com.envyful.advanced.holograms.forge.hologram.database.JsonHologramSaver;
import com.envyful.advanced.holograms.forge.hologram.entity.HologramLine;
import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.database.HologramSaver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * Static factory manager for all {@link Hologram}s on the server
 *
 */
public class HologramManager implements Runnable {

    public static void preInit() {
        new ForgeTaskBuilder()
                .async(true)
                .interval(10L)
                .delay(10L)
                .task(new HologramManager())
                .start();

        new QuitListener();
        saver = new JsonHologramSaver(ForgeHolograms.getInstance().getConfig().getStorageLocation());
    }

    private static final Map<String, ForgeHologram> HOLOGRAMS = Maps.newConcurrentMap();

    private static HologramSaver saver;

    public static void clear() {
        for (ForgeHologram value : HOLOGRAMS.values()) {
            value.despawn();
        }
        HOLOGRAMS.clear();
    }

    public static void load() {
        saver.load();
    }

    public static void save() {
        UtilConcurrency.runAsync(() -> saver.save(Lists.newArrayList(HOLOGRAMS.values())));
    }

    public static void addHologram(Hologram hologram) {
        if (!(hologram instanceof ForgeHologram)) {
            return;
        }

        addHologram((ForgeHologram) hologram);
    }

    public static void addHologram(ForgeHologram hologram) {
        HOLOGRAMS.put(hologram.getId().toLowerCase(), hologram);
    }

    public static void removeHologram(Hologram hologram) {
        if (!(hologram instanceof ForgeHologram)) {
            return;
        }

        removeHologram((ForgeHologram) hologram);
    }

    public static void removeHologram(ForgeHologram hologram) {
        HOLOGRAMS.remove(hologram.getId().toLowerCase());
    }

    public static Hologram getById(String id) {
        return HOLOGRAMS.get(id.toLowerCase());
    }

    public static List<Hologram> getAllHolograms() {
        return Collections.unmodifiableList(Lists.newArrayList(HOLOGRAMS.values()));
    }

    @Override
    public void run() {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            for (ForgeHologram hologram : HOLOGRAMS.values()) {
                if (!hologram.getWorld().equals(player.level)) {
                    if (hologram.getNearbyPlayers().contains(player.getUUID())) {
                        hologram.getNearbyPlayers().remove(player.getUUID());

                        for (HologramLine line : hologram.getLines()) {
                            UtilForgeConcurrency.runSync(() -> line.despawnForPlayer(player));
                        }
                    }

                    continue;
                }

                if (player.getPosition(10).distanceToSqr(hologram.getPosition()) > (Math.pow(hologram.getRange(), 2))) {
                    if (hologram.getNearbyPlayers().contains(player.getUUID())) {
                        hologram.getNearbyPlayers().remove(player.getUUID());

                        for (HologramLine line : hologram.getLines()) {
                            UtilForgeConcurrency.runSync(() -> line.despawnForPlayer(player));
                        }
                    }

                    continue;
                }

                if (!hologram.getNearbyPlayers().contains(player.getUUID())) {
                    for (HologramLine line : hologram.getLines()) {
                        UtilForgeConcurrency.runSync(() -> line.spawnForPlayer(player));
                    }

                    hologram.getNearbyPlayers().add(player.getUUID());
                } else {
                    for (HologramLine line : hologram.getLines()) {
                        UtilForgeConcurrency.runSync(() -> line.updateForPlayer(player));
                    }
                }
            }
        }
    }

    private static final class QuitListener extends LazyListener {
        @SubscribeEvent
        public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
            for (ForgeHologram value : HologramManager.HOLOGRAMS.values()) {
                value.getNearbyPlayers().remove(event.getEntity().getUUID());
            }
        }
    }
}
