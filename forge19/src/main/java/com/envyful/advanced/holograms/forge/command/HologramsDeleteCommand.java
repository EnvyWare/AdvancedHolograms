package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "delete",
        description = "§e/hd delete <name> §7 - Deletes hologram",
        aliases = {
                "d"
        }
)
@Permissible("advancedholograms.command.delete")
@Child
public class HologramsDeleteCommand {

    @CommandProcessor
    public void createHologram(@Sender ServerPlayer sender, @Argument Hologram hologram) {
        hologram.delete();
        HologramManager.removeHologram(hologram);
        sender.sendSystemMessage(Component.literal("§aDeleted hologram"));
    }
}
