package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "delete",
        description = "Deletes a hologram",
        aliases = {
                "d"
        }
)
@Permissible("advancedholograms.command.delete")
@Child
public class HologramsDeleteCommand {

    @CommandProcessor
    public void createHologram(@Sender EntityPlayerMP sender, @Argument Hologram hologram) {
        hologram.delete();
        HologramManager.removeHologram(hologram);
        sender.sendMessage(new TextComponentString("§aDeleted hologram"));
    }
}
