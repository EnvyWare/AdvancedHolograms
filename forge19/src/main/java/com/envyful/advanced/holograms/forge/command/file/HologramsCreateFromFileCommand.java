package com.envyful.advanced.holograms.forge.command.file;

import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.manager.HologramFactory;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Command(
        value = "createff",
        description = "§e/hd createff <hologram> <path> §7 - Copies text from the file to the hologram",
        aliases = {
                "createfromfile"
        }
)
@Permissible("advancedholograms.command.file.copy")
@Child
public class HologramsCreateFromFileCommand {

    @CommandProcessor
    public void addLineCommand(@Sender ServerPlayer sender, String[] args) {
        if (args.length < 2) {
            sender.sendSystemMessage(Component.literal("§4Not enough arguments. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"));
            return;
        }

        if (HologramManager.getById(args[0]) != null) {
            sender.sendSystemMessage(Component.literal("§4Hologram with that id already exists. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"));
            return;
        }

        File file = Paths.get(String.join(" ", Arrays.copyOfRange(args, 1, args.length))).toFile();

        if (!file.exists()) {
            sender.sendSystemMessage(Component.literal("§4Cannot find that file. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"));
            return;
        }

        String[] text = this.getLines(file);

        sender.sendSystemMessage(Component.literal("§eCreating hologram: " + args[0]));
        HologramFactory.builder(args[0]).lines(text).world(UtilWorld.getName(sender.level))
                .position(sender.getX(), sender.getY() - 1, sender.getZ())
                .range(64)
                .build();
    }

    private String[] getLines(File file) {
        List<String> lines = Lists.newArrayList();
        String currentLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((currentLine = br.readLine()) != null) {
                lines.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines.toArray(new String[0]);
    }
}
