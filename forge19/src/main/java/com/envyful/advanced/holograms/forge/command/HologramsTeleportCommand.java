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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector3d;

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
    public void nearbyCommand(@Sender ServerPlayer sender, @Argument Hologram hologram, String[] args) {
        sender.sendSystemMessage(Component.literal("§eTeleporting to " + ((ForgeHologram)hologram).getId() + "..."));
        UtilTeleport.teleportPlayer(sender, ((ForgeHologram)hologram).getWorld(),
                new Vector3d(((ForgeHologram)hologram).getPosition().x,
                        ((ForgeHologram)hologram).getPosition().y,
                        ((ForgeHologram)hologram).getPosition().z), 0f, 0f);
    }
}
