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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void addLineCommand(@Sender ServerPlayerEntity sender, @Argument Hologram hologram, @Argument String newId) {
        if (HologramManager.getById(newId) != null) {
            sender.sendMessage(new StringTextComponent("§4Hologram with that id already exists. §7/hd copy <hologram> <newId>"), Util.DUMMY_UUID);
            return;
        }

        UtilForgeConcurrency.runSync(() -> {
            hologram.copy(newId, UtilWorld.getName(sender.world), sender.getPosX(), sender.getPosY(), sender.getPosZ());
            sender.sendMessage(new StringTextComponent("§aSuccessfully copied the hologram to " + newId), Util.DUMMY_UUID);
        });
    }
}
