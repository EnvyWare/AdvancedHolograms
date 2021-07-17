package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.HologramFactory;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

@Command(
        value = "delete",
        description = "Deletes a hologram",
        aliases = {
                "d"
        }
)
@Permissible("holograms.command.create")
@Child
public class HologramsDeleteCommand {

    @CommandProcessor
    public void createHologram(@Sender EntityPlayerMP sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("§4Error! Cannot delete hologram without id. §7/hd delete <id>"));
            return;
        }

        String id = args[0];
        Hologram foundHologram = HologramManager.getById(id);

        if (foundHologram == null) {
            sender.sendMessage(new TextComponentString("§4Error! Cannot find hologram with that id. §7/hd delete <id>"));
            return;
        }

        foundHologram.delete();
        HologramManager.removeHologram(foundHologram);
        sender.sendMessage(new TextComponentString("§aDeleted hologram"));
    }
}
