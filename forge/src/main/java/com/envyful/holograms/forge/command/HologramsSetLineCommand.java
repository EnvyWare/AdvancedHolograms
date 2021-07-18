package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.type.UtilParse;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.forge.hologram.HologramManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

@Command(
        value = "setline",
        description = "Sets a line on the hologram",
        aliases = {
                "sl"
        }
)
@Permissible("holograms.command.setline")
@Child
public class HologramsSetLineCommand {

    @CommandProcessor
    public void setLineCommand(@Sender EntityPlayerMP sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id. §7/hd setline <id> <line#> <text>"));
            return;
        }

        Hologram byId = HologramManager.getById(args[0]);

        if (byId == null) {
            sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id. §7/hd setline <id> <line#> <text>"));
            return;
        }

        int lineNum = UtilParse.parseInteger(args[1]).orElse(-1);

        if (lineNum < 1) {
            sender.sendMessage(new TextComponentString("§4Line number must be positive. §7/hd setline <id> <line#> <text>"));
            return;
        }

        String line = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        byId.setLine(lineNum, line);
        sender.sendMessage(new TextComponentString("§eSetting line to " + UtilChatColour.translateColourCodes('&', line) + " on " + args[0]));
    }
}
