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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void addLineCommand(@Sender ServerPlayerEntity sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new StringTextComponent("§4Not enough arguments. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"), Util.DUMMY_UUID);
            return;
        }

        if (HologramManager.getById(args[0]) != null) {
            sender.sendMessage(new StringTextComponent("§4Hologram with that id already exists. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"), Util.DUMMY_UUID);
            return;
        }

        File file = Paths.get(String.join(" ", Arrays.copyOfRange(args, 1, args.length))).toFile();

        if (!file.exists()) {
            sender.sendMessage(new StringTextComponent("§4Cannot find that file. §7/hd createff " +
                                                               "<hologram> " +
                                                               "<path>"), Util.DUMMY_UUID);
            return;
        }

        String[] text = this.getLines(file);

        sender.sendMessage(new StringTextComponent("§eCreating hologram: " + args[0]), Util.DUMMY_UUID);
        HologramFactory.builder(args[0]).lines(text).world(UtilWorld.getName(sender.world))
                .position(sender.getPosX(), sender.getPosY() - 1, sender.getPosZ())
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
