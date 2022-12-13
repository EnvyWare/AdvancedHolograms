package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.ForgeHologram;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.hologram.Hologram;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

@Command(
        value = "near",
        description = "§e/hd near <radius> §7 - Finds nearby holograms",
        aliases = {
                "nearby"
        }
)
@Permissible("advancedholograms.command.nearby")
@Child
public class HologramsNearCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender ServerPlayer sender, @Argument int radius) {
        if (radius == 0) {
            sender.sendSystemMessage(Component.literal("§4Error! Radius must be greater than 0. §7/hd nearby <radius>"));
            return;
        }

        if (radius < 0) {
            radius = Math.abs(radius);
        }

        List<Hologram> holograms = Lists.newArrayList();

        for (Hologram allHologram : HologramManager.getAllHolograms()) {
            if (((ForgeHologram) allHologram).inRadius(sender, radius)) {
                holograms.add(allHologram);
            }
        }

        if (holograms.isEmpty()) {
            sender.sendSystemMessage(Component.literal("§4No holograms in that radius. §7/hd nearby <radius>"));
            return;
        }

        sender.sendSystemMessage(Component.literal("§eNearby holograms (" + radius + "m): "));

        for (Hologram hologram : holograms) {
            sender.sendSystemMessage(Component.literal("§e  * §b" + ((ForgeHologram) hologram).getId()
                    + " §e - " + String.format("%.0f", ((ForgeHologram) hologram).getDistance(sender)) + "m"));
        }
    }
}
