package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.type.UtilParse;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

@Command(
        value = "insertline",
        description = "§e/hd insert <hologram> <line#> <line> §7 - Inserts line to hologram"
)
@Permissible("advancedholograms.command.insertline")
@Child
public class HologramsInsertLineCommand {

    @CommandProcessor
    public void setLineCommand(@Sender ServerPlayer sender, String[] args) {
        if (args.length < 3) {
            sender.sendSystemMessage(Component.literal("§4Cannot find a hologram with that id. §7/hd insertline <id> <line#> <text>"));
            return;
        }

        Hologram byId = HologramManager.getById(args[0]);

        if (byId == null) {
            sender.sendSystemMessage(Component.literal("§4Cannot find a hologram with that id. §7/hd insertline <id> <line#> <text>"));
            return;
        }

        int lineNum = UtilParse.parseInteger(args[1]).orElse(-1);

        if (lineNum < 1) {
            sender.sendSystemMessage(Component.literal("§4Line number must be positive. §7/hd insertline <id> <line#> <text>"));
            return;
        }

        String line = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        byId.insertLine(lineNum, line);
        sender.sendSystemMessage(Component.literal("§eInserted line " + UtilChatColour.translateColourCodes('&', line) + " on " + args[0]));
    }
}
