package com.envyful.holograms.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import net.minecraft.command.ICommandSender;

@Command(
        value = "holograms",
        description = "Main holograms command",
        aliases = {
                "hd",
                "holographicdisplays"
        }
)
@Permissible("holograms.command")
@SubCommands({HologramsCreateCommand.class, HologramsDeleteCommand.class, HologramsNearCommand.class})
public class HologramsCommand {

    @CommandProcessor
    public void helpCommand(@Sender ICommandSender sender, String[] args) {
        //TODO: send help command
    }


}
