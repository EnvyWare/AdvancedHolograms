package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "copy",
        description = "§e/hd copy <hologram> <newHologram> §7 - Copies a hologram",
        aliases = {
                "duplicate"
        }
)
@Permissible("advancedholograms.command.copy")
@Child
public class HologramsCopyCommand {

    @CommandProcessor
    public void addLineCommand(@Sender ServerPlayer sender, @Argument Hologram hologram, @Argument String newId) {
        if (HologramManager.getById(newId) != null) {
            sender.sendSystemMessage(Component.literal("§4Hologram with that id already exists. §7/hd copy <hologram> <newId>"));
            return;
        }

        UtilForgeConcurrency.runSync(() -> {
            hologram.copy(newId, UtilWorld.getName(sender.level), sender.getX(), sender.getY(), sender.getZ());
            sender.sendSystemMessage(Component.literal("§aSuccessfully copied the hologram to " + newId));
        });
    }
}
