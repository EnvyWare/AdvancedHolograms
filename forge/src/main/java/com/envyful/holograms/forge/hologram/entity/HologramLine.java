package com.envyful.holograms.forge.hologram.entity;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.holograms.forge.ForgeHolograms;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.*;
import net.minecraft.world.World;

public class HologramLine {

    private static int ENTITY_ID = 0;

    private Entity entity;
    private String text;

    public HologramLine(EntityLivingBase entity) {
        this.entity = entity;
        this.initEntity();
    }

    private void initEntity() {
        this.entity.setInvisible(true);
        this.entity.setAlwaysRenderNameTag(true);
        this.entity.setCustomNameTag("");
        this.entity.setEntityId(ENTITY_ID--);

        if (entity instanceof EntityArmorStand) {
            entity.getDataManager().set(
                    EntityArmorStand.STATUS,
                    this.setBit(
                            entity.getDataManager().get(EntityArmorStand.STATUS),
                            16, true
                    )
            );
        }
    }

    private byte setBit(byte p_184797_1_, int p_184797_2_, boolean p_184797_3_) {
        if (p_184797_3_) {
            p_184797_1_ = (byte) (p_184797_1_ | p_184797_2_);
        } else {
            p_184797_1_ = (byte) (p_184797_1_ & ~p_184797_2_);
        }

        return p_184797_1_;
    }

    public void despawnForPlayer(EntityPlayerMP player) {
        player.connection.sendPacket(new SPacketDestroyEntities(this.entity.getEntityId()));
    }

    public void spawnForPlayer(EntityPlayerMP player) {
        if (this.entity instanceof EntityLivingBase) {
            SPacketSpawnMob packet = new SPacketSpawnMob((EntityLivingBase) this.entity);
            player.connection.sendPacket(packet);
        } else {
            SPacketSpawnObject packet = new SPacketSpawnObject(this.entity, 2, 1);
            player.connection.sendPacket(packet);
            this.updateForPlayer(player);
        }
    }

    public void updateForPlayer(EntityPlayerMP player) {
        if (ForgeHolograms.getInstance().arePlaceholdersEnabled()) {
            this.entity.setCustomNameTag(UtilChatColour.translateColourCodes(
                    '&',
                    UtilPlaceholder.replaceIdentifiers(
                            player,
                            this.text
                    )
            ));
        }

        player.connection.sendPacket(new SPacketEntityMetadata(this.entity.getEntityId(), this.entity.getDataManager(), false));
    }

    public void sendTeleportPacket(EntityPlayerMP player) {
        player.connection.sendPacket(new SPacketEntityTeleport(this.entity));
    }

    public void setWorld(World world) {
        this.entity.world = world;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.posX = x;
        this.entity.posY = y;
        this.entity.posZ = z;
    }

    public void setText(String text) {
        this.text = text;

        if (this.text.equals("{empty}")) {
            this.entity.setAlwaysRenderNameTag(false);
            this.entity.setCustomNameTag(" ");
        } else {
            this.entity.setCustomNameTag(UtilChatColour.translateColourCodes('&', this.text));
        }
    }

    public String getText() {
        return this.text;
    }
}
