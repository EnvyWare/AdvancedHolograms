package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "movehere",
        description = "Teleports the hologram to the player",
        aliases = {
                "mvh",
                "mv"
        }
)
@Permissible("holograms.command.movehere")
@Child
public class HologramsMoveHereCommand {

    @CommandProcessor
    public void nearbyCommand(@Sender EntityPlayerMP sender, @Argument Hologram hologram) {
        sender.sendMessage(new TextComponentString("§eTeleporting... "));
        hologram.teleport(UtilWorld.getName(sender.world), sender.posX, sender.posY, sender.posZ);
    }
}
