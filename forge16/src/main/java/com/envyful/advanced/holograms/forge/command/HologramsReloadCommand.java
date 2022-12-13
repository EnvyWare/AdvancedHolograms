package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.ForgeHolograms;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

@Command(
        value = "reload",
        description = "§e/hd reload §7 - Reloads the database of holograms"
)
@Permissible("advancedholograms.command.reload")
@Child
public class HologramsReloadCommand {

    @CommandProcessor
    public void addLineCommand(@Sender ServerPlayerEntity sender) {
        sender.sendMessage(UtilChatColour.colour(
                ForgeHolograms.getInstance().getConfig().getReloadingMessage()
        ), Util.DUMMY_UUID);
        HologramManager.clear();
        HologramManager.load();
        sender.sendMessage(UtilChatColour.colour(
                ForgeHolograms.getInstance().getConfig().getReloadedMessage()
        ), Util.DUMMY_UUID);
    }
}
