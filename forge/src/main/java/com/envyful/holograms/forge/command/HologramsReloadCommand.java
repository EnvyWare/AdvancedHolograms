package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.ForgeHolograms;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "reload",
        description = "§e/hd reload §7 - Reloads the database of holograms"
)
@Permissible("advancedholograms.command.reload")
@Child
public class HologramsReloadCommand {

    @CommandProcessor
    public void addLineCommand(@Sender EntityPlayerMP sender, @Argument Hologram hologram, @Argument String newId) {
        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                '&',
                ForgeHolograms.getInstance().getConfig().getReloadingMessage()
        )));
        HologramManager.clear();
        HologramManager.load();
        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                '&',
                ForgeHolograms.getInstance().getConfig().getReloadedMessage()
        )));
    }
}
