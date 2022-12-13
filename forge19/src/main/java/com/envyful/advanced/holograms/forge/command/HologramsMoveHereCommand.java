package com.envyful.advanced.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "movehere",
        description = "§e/hd movehere <hologram> §7 - Teleports hologram to you",
        aliases = {
                "mvh",
                "mv"
        }
)
@Permissible("advancedholograms.command.movehere")
@Child
public class HologramsMoveHereCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender ServerPlayer sender, @Argument Hologram hologram) {
        sender.sendSystemMessage(Component.literal("§eTeleporting... "));
        hologram.teleport(UtilWorld.getName(sender.level), sender.getX(), sender.getY() - 1, sender.getZ());
    }
}
