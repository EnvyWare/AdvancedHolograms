package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.ForgeHologram;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.player.util.UtilTeleport;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "teleport",
        description = "§e/hd tp <hologram> §7 - Teleports to hologram",
        aliases = {
                "tp"
        }
)
@Permissible("advancedholograms.command.teleport")
@Child
public class HologramsTeleportCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender ServerPlayerEntity sender, @Argument Hologram hologram, String[] args) {
        sender.sendMessage(new StringTextComponent("§eTeleporting to " + ((ForgeHologram) hologram).getId() + "..."), Util.DUMMY_UUID);
        UtilTeleport.teleportPlayer(sender, ((ForgeHologram) hologram).getWorld(), ((ForgeHologram) hologram).getPosition(), 0f, 0f);
    }
}
