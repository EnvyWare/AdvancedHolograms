package com.envyful.holograms.forge;


import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.HologramFactory;
import com.envyful.holograms.forge.command.HologramsCommand;
import com.envyful.holograms.forge.config.HologramsConfig;
import com.envyful.holograms.forge.hologram.HologramManager;
import com.envyful.holograms.forge.hologram.manager.ForgeHologramManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.bstats.forge.Metrics;

import java.io.IOException;
import java.nio.file.Paths;

@Mod(
        modid = "advancedholograms",
        name = "AdvancedHolograms Forge",
        version = ForgeHolograms.VERSION,
        acceptableRemoteVersions = "*"
)
public class ForgeHolograms {

    public static final String VERSION = "0.3.0";

    private static ForgeHolograms instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private HologramsConfig config;
    private boolean placeholders;

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        HologramFactory.setHologramManager(new ForgeHologramManager());
        instance = this;

        this.loadConfig();
        HologramManager.preInit();

        Metrics metrics = new Metrics(
                Loader.instance().activeModContainer(),
                event.getModLog(),
                Paths.get("config/"),
                12199
        );

        ForgeUpdateBuilder.instance()
                .name("AdvancedHolograms")
                .requiredPermission("advancedholograms.update.notify")
                .owner("Pixelmon-Development")
                .repo("AdvancedHolograms")
                .version(VERSION)
                .start();
    }

    private void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(HologramsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForHolograms() {
        try {
            Class.forName("com.envyful.papi.forge.ForgePlaceholderAPI");
            this.placeholders = true;
        } catch (ClassNotFoundException e) {
            this.placeholders = false;
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        this.commandFactory.registerInjector(Hologram.class, (sender, args) -> {
            Hologram byId = HologramManager.getById(args[0]);

            if (byId == null) {
                sender.sendMessage(new TextComponentString("§4Cannot find a hologram with that id"));
                return null;
            }

            return byId;
        });

        this.commandFactory.registerCommand(event.getServer(), new HologramsCommand());

        HologramManager.load();
        this.checkForHolograms();
    }

    public static ForgeHolograms getInstance() {
        return instance;
    }

    public HologramsConfig getConfig() {
        return this.config;
    }

    public boolean arePlaceholdersEnabled() {
        return this.placeholders;
    }
}
