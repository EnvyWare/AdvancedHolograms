package com.envyful.advanced.holograms.forge.hologram.entity;

import com.envyful.advanced.holograms.forge.ForgeHolograms;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class HologramLine {

    private static int ENTITY_ID = 0;

    private Entity entity;
    private String text;

    public HologramLine(LivingEntity entity) {
        this.entity = entity;
        this.initEntity();
    }

    private void initEntity() {
        this.entity.setInvisible(true);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(new StringTextComponent(""));
        this.entity.setEntityId(ENTITY_ID--);

        if (entity instanceof ArmorStandEntity) {
            entity.getDataManager().set(
                    ArmorStandEntity.STATUS,
                    this.setBit(
                            entity.getDataManager().get(ArmorStandEntity.STATUS),
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

    public void despawnForPlayer(ServerPlayerEntity player) {
        player.connection.sendPacket(new SDestroyEntitiesPacket(this.entity.getEntityId()));
    }

    public void spawnForPlayer(ServerPlayerEntity player) {
        if (player == null || player.connection == null) {
            return;
        }

        if (this.entity instanceof LivingEntity) {
            SSpawnMobPacket packet = new SSpawnMobPacket((LivingEntity) this.entity);
            player.connection.sendPacket(packet);
            this.updateForPlayer(player);
        } else {
            SSpawnObjectPacket packet = new SSpawnObjectPacket(this.entity, EntityType.ITEM, 1, this.entity.getPosition());
            player.connection.sendPacket(packet);
            this.updateForPlayer(player);
        }
    }

    public void updateForPlayer(ServerPlayerEntity player) {
        if (ForgeHolograms.getInstance().arePlaceholdersEnabled()) {
            this.entity.setCustomName(UtilChatColour.colour(
                    UtilPlaceholder.replaceIdentifiers(
                            player,
                            this.text
                    )
            ));
        }

        player.connection.sendPacket(new SEntityMetadataPacket(this.entity.getEntityId(), this.entity.getDataManager(), true));
    }

    public void sendTeleportPacket(ServerPlayerEntity player) {
        player.connection.sendPacket(new SEntityTeleportPacket(this.entity));
    }

    public void setWorld(World world) {
        this.entity.world = world;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPosition(x, y, z);
    }

    public void setText(String text) {
        this.text = text;

        if (this.text.equals("{empty}")) {
            this.entity.setCustomNameVisible(false);
            this.entity.setCustomName(new StringTextComponent(" "));
        } else {
            this.entity.setCustomName(UtilChatColour.colour(this.text));
        }
    }

    public String getText() {
        return this.text;
    }
}
