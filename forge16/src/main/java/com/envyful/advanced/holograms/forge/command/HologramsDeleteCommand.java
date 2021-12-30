package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void createHologram(@Sender ServerPlayerEntity sender, @Argument Hologram hologram) {
        hologram.delete();
        HologramManager.removeHologram(hologram);
        sender.sendMessage(new StringTextComponent("§aDeleted hologram"), Util.DUMMY_UUID);
    }
}
