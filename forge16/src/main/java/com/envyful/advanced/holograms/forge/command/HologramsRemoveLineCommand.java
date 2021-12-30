package com.envyful.advanced.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.api.exception.HologramException;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "removeline",
        description = "§e/hd removeline <hologram> <line#> §7 - Removes line from hologram",
        aliases = {
                "rl"
        }
)
@Permissible("advancedholograms.command.removeline")
@Child
public class HologramsRemoveLineCommand {

    @CommandProcessor
    public void addLineCommand(@Sender ServerPlayerEntity sender, @Argument Hologram hologram, @Argument int line) {
        try {
            hologram.removeLine(line);
        } catch (HologramException e) {
            sender.sendMessage(new StringTextComponent(e.getMessage()), Util.DUMMY_UUID);
            return;
        }

        sender.sendMessage(new StringTextComponent("§eRemoved line " + line + " from hologram"), Util.DUMMY_UUID);
    }
}
