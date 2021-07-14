package com.envyful.holograms.forge;


import com.envyful.holograms.api.manager.HologramFactory;
import com.envyful.holograms.forge.command.TestAddLineCommand;
import com.envyful.holograms.forge.command.TestCreateCommand;
import com.envyful.holograms.forge.hologram.manager.ForgeHologramManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = "holograms",
        name = "Holograms Forge",
        version = "0.0.1",
        acceptableRemoteVersions = "*"
)
public class ForgeHolograms {

    private static ForgeHolograms instance;

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        HologramFactory.setHologramManager(new ForgeHologramManager());
        instance = this;
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    public static ForgeHolograms getInstance() {
        return instance;
    }
}
