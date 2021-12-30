package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void addLineCommand(@Sender ServerPlayerEntity sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new StringTextComponent("§4Cannot find a hologram with that id. §7/hd addline <id> <line>"), Util.DUMMY_UUID);
            return;
        }

        Hologram byId = HologramManager.getById(args[0]);

        if (byId == null) {
            sender.sendMessage(new StringTextComponent("§4Cannot find a hologram with that id. §7/hd addline <id> <line>"), Util.DUMMY_UUID);
            return;
        }

        String line = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        sender.sendMessage(new StringTextComponent("§eAdding line " + UtilChatColour.translateColourCodes('&', line) + " to " + args[0]), Util.DUMMY_UUID);
        byId.addLine(line);
    }
}
