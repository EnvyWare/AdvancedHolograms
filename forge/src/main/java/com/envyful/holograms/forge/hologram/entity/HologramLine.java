package com.envyful.holograms.forge.hologram.entity;

import com.envyful.api.forge.chat.UtilChatColour;
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

/*        if (text.startsWith("ICON: ")) {
            String icon = text.split("ICON: ")[1];

            if (!icon.contains(":")) {
                if (!(this.entity instanceof EntityItem)) {
                    this.entity = new EntityItem(this.entity.world, this.entity.posX, this.entity.posY, this.entity.posZ);
                    this.initEntity();
                }

                ((EntityItem) this.entity).setItem(new ItemStack(Item.getByNameOrId(icon)));
            } else {
                String[] args = icon.split(":");
                int damage = UtilParse.parseInteger(args[1]).orElse(-1);

                if (damage < 0) {
                    this.entity = new EntityItem(this.entity.world, this.entity.posX, this.entity.posY, this.entity.posZ);
                    ((EntityItem) this.entity).setItem(new ItemStack(Item.getByNameOrId(icon)));
                } else {
                    this.entity = new EntityItem(this.entity.world, this.entity.posX, this.entity.posY, this.entity.posZ);
                    ((EntityItem) this.entity).setItem(new ItemStack(Item.getByNameOrId(args[0]), 1, damage));
                }
            }
        } else {*/
            if (!(this.entity instanceof EntityArmorStand)) {
                this.entity = new EntityArmorStand(entity.world, entity.posX, entity.posY, entity.posZ);
                this.initEntity();
            }

            this.entity.setCustomNameTag(UtilChatColour.translateColourCodes('&', this.text));
/*        }*/
    }

    public String getText() {
        return this.text;
    }
}
