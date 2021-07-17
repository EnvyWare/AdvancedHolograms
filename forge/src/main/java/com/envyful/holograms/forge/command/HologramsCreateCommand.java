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
        value = "create",
        description = "Creates a hologram",
        aliases = {
                "c"
        }
)
@Permissible("holograms.command.create")
@Child
public class HologramsCreateCommand {

    @CommandProcessor
    public void createHologram(@Sender EntityPlayerMP sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new TextComponentString("§4Error! Cannot create hologram without id. §7/hd create <id> <first line>"));
            return;
        }

        String id = args[0];
        String firstLine = id;

        if (args.length > 2) {
            firstLine = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        }

        Hologram hologram = HologramFactory.builder(id)
                .line(firstLine)
                .world(UtilWorld.getName(sender.world))
                .position(sender.posX, sender.posY, sender.posZ)
                .build();

        HologramManager.addHologram(hologram);
        sender.sendMessage(new TextComponentString("§aCreated hologram with id " + id));
    }
}
