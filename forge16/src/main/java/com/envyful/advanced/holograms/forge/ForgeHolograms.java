package com.envyful.advanced.holograms.forge;


import com.envyful.advanced.holograms.forge.command.HologramsCommand;
import com.envyful.advanced.holograms.forge.config.HologramsConfig;
import com.envyful.advanced.holograms.forge.hologram.HologramManager;
import com.envyful.advanced.holograms.forge.hologram.manager.ForgeHologramManager;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.holograms.api.hologram.Hologram;
import com.envyful.holograms.api.manager.HologramFactory;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.IOException;

@Mod(
        value = ForgeHolograms.MOD_ID
)
@Mod.EventBusSubscriber(modid = ForgeHolograms.MOD_ID)
public class ForgeHolograms {

    public static final String MOD_ID = "advancedholograms";
    public static final String VERSION = "0.6.2";

    private static ForgeHolograms instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private HologramsConfig config;
    private boolean placeholders;

    public ForgeHolograms() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        HologramFactory.setHologramManager(new ForgeHologramManager());
        instance = this;

        this.loadConfig();
        HologramManager.preInit();
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

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        this.commandFactory.registerInjector(Hologram.class, (sender, args) -> {
            Hologram byId = HologramManager.getById(args[0]);

            if (byId == null) {
                sender.sendMessage(new StringTextComponent("§4Cannot find a hologram with that id"), Util.DUMMY_UUID);
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
