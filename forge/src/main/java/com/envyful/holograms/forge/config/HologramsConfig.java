package com.envyful.holograms.forge.config;

import com.envyful.api.commons.shade.configurate.objectmapping.ConfigSerializable;
import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;

@ConfigPath("config/AdvancedHolograms/config.yml")
@ConfigSerializable
public class HologramsConfig extends AbstractYamlConfig {

    private String storageLocation = "config/AdvancedHolograms/holograms.json";

    public HologramsConfig() {}

    public String getStorageLocation() {
        return this.storageLocation;
    }
}
