package com.envyful.advanced.holograms.forge;


import com.envyful.advanced.holograms.forge.command.HologramsCommand;
import com.envyful.advanced.holograms.forge.config.HologramsConfig;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.advanced.holograms.forge.hologram.manager.ForgeHologramManager;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.HologramFactory;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod(ForgeHolograms.MOD_ID)
public class ForgeHolograms {

    public static final String MOD_ID = "advancedholograms";
    public static final String VERSION = "1.0.0";

    private static ForgeHolograms instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private HologramsConfig config;
    private boolean placeholders;

    public ForgeHolograms() {
        instance = this;
        this.loadConfig();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        HologramFactory.setHologramManager(new ForgeHologramManager());
        HologramManager.preInit();
    }

    private void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(HologramsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForPAPI() {
        try {
            Class.forName("com.envyful.papi.forge.ForgePlaceholderAPI");
            this.placeholders = true;
        } catch (ClassNotFoundException e) {
            this.placeholders = false;
        }
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        HologramManager.load();
        this.checkForPAPI();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        this.commandFactory.registerInjector(Hologram.class, (sender, args) -> {
            Hologram byId = HologramManager.getById(args[0]);

            if (byId == null) {
                sender.sendSystemMessage(Component.literal("§4Cannot find a hologram with that id"));
                return null;
            }

            return byId;
        });

        this.commandFactory.registerCommand(event.getDispatcher(), new HologramsCommand());
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
