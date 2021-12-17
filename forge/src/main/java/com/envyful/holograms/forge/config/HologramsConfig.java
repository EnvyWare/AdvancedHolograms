package com.envyful.holograms.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigPath("config/AdvancedHolograms/config.yml")
@ConfigSerializable
public class HologramsConfig extends AbstractYamlConfig {

    private String storageLocation = "config/AdvancedHolograms/holograms.json";
    private String reloadingMessage = "&e&l(!) &eReloading database...";
    private String reloadedMessage = "&e&l(!) &eReloaded database";

    public HologramsConfig() {}

    public String getStorageLocation() {
        return this.storageLocation;
    }

    public String getReloadingMessage() {
        return this.reloadingMessage;
    }

    public String getReloadedMessage() {
        return this.reloadedMessage;
    }
}
