package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.manager.HologramFactory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;

@Command(
        value = "create",
        description = "§e/hd create <name> (line) §7 - Creates hologram",
        aliases = {
                "c"
        }
)
@Permissible("advancedholograms.command.create")
@Child
public class HologramsCreateCommand {

    @CommandProcessor
    public void createHologram(@Sender ServerPlayerEntity sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new StringTextComponent("§4Error! Cannot create hologram without id. §7/hd create <id> <first line>"), Util.DUMMY_UUID);
            return;
        }

        String id = args[0];

        if (HologramManager.getById(id) != null) {
            sender.sendMessage(new StringTextComponent("§4Error! A hologram with that id already exists. §7/hd create <id> <first line>"), Util.DUMMY_UUID);
            return;
        }

        String firstLine = id;

        if (args.length > 1) {
            firstLine = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        HologramFactory.builder(id)
                .line(firstLine)
                .world(UtilWorld.getName(sender.world))
                .position(sender.getPosX(), sender.getPosY() - 1, sender.getPosZ())
                .range(64)
                .build();

        sender.sendMessage(new StringTextComponent("§aCreated hologram with id " + id), Util.DUMMY_UUID);
    }
}
