package com.envyful.advanced.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void nearbyCommand(@Sender ServerPlayerEntity sender, @Argument Hologram hologram) {
        sender.sendMessage(new StringTextComponent("§eTeleporting... "), Util.DUMMY_UUID);
        hologram.teleport(UtilWorld.getName(sender.world), sender.getPosX(), sender.getPosY() - 1, sender.getPosZ());
    }
}
