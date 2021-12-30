package com.envyful.advanced.holograms.forge.command;

import com.envyful.advanced.holograms.forge.hologram.ForgeHologram;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.PageBuilder;
import com.envyful.api.type.UtilParse;
import com.envyful.holograms.api.hologram.Hologram;
import net.minecraft.command.ICommandSource;

import java.util.List;

@Command(
        value = "list",
        description = "§e/hd list (page) §7 - Lists all the holograms"
)
@Permissible("advancedholograms.command.list")
public class HologramsListCommand {

    @CommandProcessor
    public void listCommand(@Sender ICommandSource sender, String[] args) {
        List<Hologram> allHolograms = HologramManager.getAllHolograms();
        int page = 0;

        if (args.length > 0) {
            page = UtilParse.parseInteger(args[0]).orElse(1) - 1;
        }

        if (page > (allHolograms.size() / 10)) {
            page = 0;
        }

        PageBuilder.instance(Hologram.class)
                .values(allHolograms)
            .display(hologram -> {
                if (!(hologram instanceof ForgeHologram)) {
                    return "";
                }

                return ((ForgeHologram) hologram).getId();
            })
            .pageSize(10)
            .send(sender, page);
    }
}
