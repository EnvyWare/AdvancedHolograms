package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.ForgeHolograms;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "reload",
        description = "§e/hd reload §7 - Reloads the database of holograms"
)
@Permissible("advancedholograms.command.reload")
@Child
public class HologramsReloadCommand {

    @CommandProcessor
    public void addLineCommand(@Sender ServerPlayer sender) {
        sender.sendSystemMessage(UtilChatColour.colour(
                ForgeHolograms.getInstance().getConfig().getReloadingMessage()
        ));
        HologramManager.clear();
        HologramManager.load();
        sender.sendSystemMessage(UtilChatColour.colour(
                ForgeHolograms.getInstance().getConfig().getReloadedMessage()
        ));
    }
}
