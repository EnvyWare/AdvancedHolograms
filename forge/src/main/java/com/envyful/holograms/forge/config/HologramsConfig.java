package com.envyful.holograms.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigPath("config/AdvancedHolograms/config.yml")
@ConfigSerializable
public class HologramsConfig extends AbstractYamlConfig {

    private String storageLocation = "config/AdvancedHolograms/holograms.json";

    public HologramsConfig() {}

    public String getStorageLocation() {
        return this.storageLocation;
    }
}
