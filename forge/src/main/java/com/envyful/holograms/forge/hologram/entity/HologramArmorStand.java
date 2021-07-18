package com.envyful.holograms.forge.hologram.entity;

import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.world.World;

import java.lang.reflect.Field;

/**
 *
 * Extension of the {@link EntityArmorStand} entity class for handling setting type IDs, entity IDs, spawning (etc)
 *
 */
public class HologramArmorStand extends EntityArmorStand {

    private static final int HOLOGRAM_TYPE_ID = EntityList.getID(EntityArmorStand.class);
    private static int ENTITY_ID = 0;

    private static Field TYPE_ID_FIELD;

    static {
        try {
            TYPE_ID_FIELD = SPacketSpawnMob.class.getDeclaredField("field_149040_b");
            TYPE_ID_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private String text;

    public HologramArmorStand(World worldIn) {
        super(worldIn);

        this.init();
        this.setEntityId(ENTITY_ID--);
    }

    public HologramArmorStand(World worldIn, double posX, double posY, double posZ) {
        super(worldIn, posX, posY, posZ);

        this.init();
        this.setEntityId(ENTITY_ID--);
    }

    private void init() {
        this.setInvisible(true);
        this.setAlwaysRenderNameTag(true);
        this.setCustomNameTag("");
    }

    public void despawnForPlayer(EntityPlayerMP player) {
        player.connection.sendPacket(new SPacketDestroyEntities(this.getEntityId()));
    }

    public void spawnForPlayer(EntityPlayerMP player) {
        SPacketSpawnMob packet = new SPacketSpawnMob(this);
        this.setTypeId(packet);
        player.connection.sendPacket(packet);
    }

    private void setTypeId(SPacketSpawnMob packet) {
        try {
            TYPE_ID_FIELD.set(packet, HOLOGRAM_TYPE_ID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateForPlayer(EntityPlayerMP player) {
        player.connection.sendPacket(new SPacketEntityMetadata(this.getEntityId(), this.getDataManager(), false));
    }

    /**
     *
     * Sets the text for the line of the hologram
     *
     * @param text The new line of the hologram
     */
    public void setText(String text) {
        this.text = text;
        this.setCustomNameTag(UtilChatColour.translateColourCodes('&', text));
    }

    public String getText() {
        return this.text;
    }
}
