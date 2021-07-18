package com.envyful.holograms.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigPath("config/AdvancedHolograms/config.yml")
@ConfigSerializable
public class HologramsConfig extends AbstractYamlConfig {

    @Comment("Storage Types: 'sql' or 'json'")
    private String storageType = "sql";

    public HologramsConfig() {}

    public String getStorageType() {
        return this.storageType;
    }
}
