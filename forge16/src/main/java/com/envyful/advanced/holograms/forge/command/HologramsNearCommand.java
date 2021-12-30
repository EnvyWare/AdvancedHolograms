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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void nearbyCommand(@Sender ServerPlayerEntity sender, @Argument int radius) {
        if (radius == 0) {
            sender.sendMessage(new StringTextComponent("§4Error! Radius must be greater than 0. §7/hd nearby <radius>"), Util.DUMMY_UUID);
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
            sender.sendMessage(new StringTextComponent("§4No holograms in that radius. §7/hd nearby <radius>"), Util.DUMMY_UUID);
            return;
        }

        sender.sendMessage(new StringTextComponent("§eNearby holograms (" + radius + "m): "), Util.DUMMY_UUID);

        for (Hologram hologram : holograms) {
            sender.sendMessage(new StringTextComponent("§e  * §b" + ((ForgeHologram) hologram).getId()
                    + " §e - " + String.format("%.0f", ((ForgeHologram) hologram).getDistance(sender)) + "m"), Util.DUMMY_UUID);
        }
    }
}
