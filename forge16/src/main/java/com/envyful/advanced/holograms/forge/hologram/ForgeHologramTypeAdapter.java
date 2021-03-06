package com.envyful.advanced.holograms.forge.hologram;

import com.envyful.advanced.holograms.forge.hologram.entity.HologramLine;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.holograms.api.manager.HologramFactory;
import com.google.common.collect.Lists;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class ForgeHologramTypeAdapter implements JsonSerializer<ForgeHologram>, JsonDeserializer<ForgeHologram> {

    @Override
    public JsonElement serialize(ForgeHologram hologram, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("id", hologram.getId());
        object.add("loc", this.getLocationObject(hologram));

        JsonArray lines = new JsonArray();

        for (HologramLine line : hologram.getLines()) {
            lines.add(line.getText());
        }

        object.add("lines", lines);
        object.addProperty("range", hologram.getRange());
        return object;
    }

    private JsonObject getLocationObject(ForgeHologram hologram) {
        JsonObject object = new JsonObject();

        object.addProperty("x", hologram.getPosition().x);
        object.addProperty("y", hologram.getPosition().y);
        object.addProperty("z", hologram.getPosition().z);
        object.addProperty("world", UtilWorld.getName(hologram.getWorld()));

        return object;
    }

    @Override
    public ForgeHologram deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        String id = object.get("id").getAsString();

        JsonObject loc = object.getAsJsonObject("loc");

        String world = loc.get("world").getAsString();
        double x = loc.get("x").getAsDouble();
        double y = loc.get("y").getAsDouble();
        double z = loc.get("z").getAsDouble();
        int range = 64;

        if (object.has("range")) {
            range = object.get("range").getAsInt();

            if (range == 0) {
                range = 64;
            }
        }

        JsonArray lines = object.getAsJsonArray("lines");
        List<String> textLines = Lists.newArrayList();

        for (JsonElement line : lines) {
            textLines.add(line.getAsString());
        }

        return (ForgeHologram) HologramFactory.builder()
                .id(id)
                .position(x, y, z)
                .world(world)
                .lines(textLines.toArray(new String[0]))
                .range(range)
                .build(false);
    }
}
