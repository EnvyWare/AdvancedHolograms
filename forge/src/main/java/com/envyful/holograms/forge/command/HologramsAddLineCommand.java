package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

@Command(
        value = "addline",
        description = "§e/hd addline <hologram> <line> §7 - Adds line to hologram",
        aliases = {
                "al"
        }
)
@Permissible("advancedholograms.command.addline")
@Child
public class HologramsAddLineCommand {

    @CommandProcessor
    public void addLineCommand(@Sender EntityPlayerMP sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id. §7/hd addline <id> <line>"));
            return;
        }

        Hologram byId = HologramManager.getById(args[0]);

        if (byId == null) {
            sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id. §7/hd addline <id> <line>"));
            return;
        }

        String line = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        sender.sendMessage(new TextComponentString("§eAdding line " + UtilChatColour.translateColourCodes('&', line) + " to " + args[0]));
        byId.addLine(line);
    }
}
