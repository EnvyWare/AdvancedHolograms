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
import com.envyful.holograms.forge.hologram.HologramManager;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

@Command(
        value = "teleport",
        description = "Teleports the player to the nearby hologram",
        aliases = {
                "tp"
        }
)
@Permissible("holograms.command.teleport")
@Child
public class HologramsTeleportCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender EntityPlayerMP sender, @Argument String id) {
        Hologram byId = HologramManager.getById(id);

        if (byId == null) {
            sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id. §7/hd nearby <radius>"));
            return;
        }

        sender.sendMessage(new TextComponentString("§eTeleporting... "));
        UtilTeleport.teleportPlayer(sender, ((ForgeHologram) byId).getWorld(), ((ForgeHologram) byId).getPosition(), 0f, 0f);
    }
}
