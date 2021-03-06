package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.player.util.UtilTeleport;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.ForgeHologram;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

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
    public void nearbyCommand(@Sender EntityPlayerMP sender, @Argument Hologram hologram, String[] args) {
        sender.sendMessage(new TextComponentString("§eTeleporting to " + args[0] + "..."));
        UtilTeleport.teleportPlayer(sender, ((ForgeHologram) hologram).getWorld(), ((ForgeHologram) hologram).getPosition(), 0f, 0f);
    }
}
