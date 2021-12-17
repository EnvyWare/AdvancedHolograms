package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.holograms.forge.command.file.HologramsCreateFromFileCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "holograms",
        description = "Main holograms command",
        aliases = {
                "hd",
                "holographicdisplays",
                "advancedholograms"
        }
)
@Permissible("advancedholograms.command")
@SubCommands({
        HologramsCreateCommand.class,
        HologramsDeleteCommand.class,
        HologramsNearCommand.class,
        HologramsTeleportCommand.class,
        HologramsMoveHereCommand.class,
        HologramsAddLineCommand.class,
        HologramsRemoveLineCommand.class,
        HologramsSetLineCommand.class,
        HologramsInsertLineCommand.class,
        HologramsCopyCommand.class,
        HologramsListCommand.class,
        HologramsCreateFromFileCommand.class,
        HologramsReloadCommand.class
})
public class HologramsCommand {

    private static final String HELP_MESSAGE =
            "&e/hd reload - Reloads hologram database\n" +
            "§e/hd create <name> (line) §7 - Creates hologram\n" +
            "§e/hd delete <name> §7 - Deletes hologram\n" +
            "§e/hd near <radius> §7 - Finds nearby holograms\n" +
            "§e/hd tp <hologram> §7 - Teleports to hologram\n" +
            "§e/hd movehere <hologram> §7 - Teleports hologram to you\n" +
            "§e/hd addline <hologram> <line> §7 - Adds line to hologram\n" +
            "§e/hd removeline <hologram> <line#> §7 - Removes line from hologram\n" +
            "§e/hd setline <hologram> <line#> <line> §7 - Sets line on hologram\n" +
            "§e/hd insert <hologram> <line#> <line> §7 - Inserts line to hologram\n" +
            "§e/hd copy <hologram> <newHologram> §7 - Copies a hologram\n" +
            "§e/hd list (page) §7 - Lists all the holograms\n" +
            "§e/hd createff <hologram> <path> §7 - Copies text from the file to the hologram";

    @CommandProcessor
    public void helpCommand(@Sender ICommandSender sender, String[] args) {
        sender.sendMessage(new TextComponentString(HELP_MESSAGE));
    }
}
