package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.manager.HologramFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
    public void createHologram(@Sender ServerPlayer sender, String[] args) {
        if (args.length < 1) {
            sender.sendSystemMessage(Component.literal("§4Error! Cannot create hologram without id. §7/hd create <id> <first line>"));
            return;
        }

        String id = args[0];

        if (HologramManager.getById(id) != null) {
            sender.sendSystemMessage(Component.literal("§4Error! A hologram with that id already exists. §7/hd create <id> <first line>"));
            return;
        }

        String firstLine = id;

        if (args.length > 1) {
            firstLine = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        HologramFactory.builder(id)
                .line(firstLine)
                .world(UtilWorld.getName(sender.level))
                .position(sender.getX(), sender.getY() - 1, sender.getZ())
                .range(64)
                .build();

        sender.sendSystemMessage(Component.literal("§aCreated hologram with id " + id));
    }
}
