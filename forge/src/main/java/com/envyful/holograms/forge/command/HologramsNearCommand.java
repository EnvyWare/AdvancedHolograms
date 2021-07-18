package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.ForgeHologram;
import com.envyful.holograms.forge.hologram.HologramManager;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

@Command(
        value = "near",
        description = "Gets nearby holograms",
        aliases = {
                "nearby"
        }
)
@Permissible("holograms.command.nearby")
@Child
public class HologramsNearCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender EntityPlayerMP sender, @Argument int radius) {
        if (radius == 0) {
            sender.sendMessage(new TextComponentString("§4Error! Radius must be greater than 0. §7/hd nearby <radius>"));
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
            sender.sendMessage(new TextComponentString("§4No holograms in that radius. §7/hd nearby <radius>"));
            return;
        }

        sender.sendMessage(new TextComponentString("§eNearby holograms (" + radius + "): "));

        for (Hologram hologram : holograms) {
            sender.sendMessage(new TextComponentString("§e  * §b" + ((ForgeHologram) hologram).getId()
                    + " §e - " + String.format("%.0f", ((ForgeHologram) hologram).getDistance(sender)) + "m"));
        }
    }
}
