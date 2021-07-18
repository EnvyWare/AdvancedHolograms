package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "copy",
        description = "Copies the hologram to a new one at the player's position",
        aliases = {
                "duplicate"
        }
)
@Permissible("holograms.command.copy")
@Child
public class HologramsCopyCommand {

    @CommandProcessor
    public void addLineCommand(@Sender EntityPlayerMP sender, @Argument Hologram hologram, @Argument String newId) {
        if (HologramManager.getById(newId) != null) {
            sender.sendMessage(new TextComponentString("§4Hologram with that id already exists. §7/hd copy <hologram> <newId>"));
            return;
        }

        UtilForgeConcurrency.runSync(() -> {
            hologram.copy(newId, UtilWorld.getName(sender.world), sender.posX, sender.posY, sender.posZ);
            sender.sendMessage(new TextComponentString("§aSuccessfully copied the hologram to " + newId));
        });
    }
}
