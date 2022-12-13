package com.envyful.advanced.holograms.forge.hologram.entity;

import com.envyful.advanced.holograms.forge.ForgeHolograms;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;

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
        this.entity.setCustomName(Component.empty());
        this.entity.setId(ENTITY_ID--);

        if (entity instanceof ArmorStand) {
            entity.getEntityData().set(
                    ArmorStand.DATA_CLIENT_FLAGS,
                    this.setBit(
                            entity.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS),
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

    public void despawnForPlayer(ServerPlayer player) {
        player.connection.send(new ClientboundRemoveEntitiesPacket(this.entity.getId()));
    }

    public void spawnForPlayer(ServerPlayer player) {
        if (player == null || player.connection == null) {
            return;
        }

        if (this.entity instanceof LivingEntity) {
            ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(this.entity);
            player.connection.send(packet);
            this.updateForPlayer(player);
        } else {
            ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(this.entity);
            player.connection.send(packet);
            this.updateForPlayer(player);
        }
    }

    public void updateForPlayer(ServerPlayer player) {
        if (ForgeHolograms.getInstance().arePlaceholdersEnabled()) {
            this.entity.setCustomName(UtilChatColour.colour(
                    UtilPlaceholder.replaceIdentifiers(
                            player,
                            this.text
                    )
            ));
        }

        player.connection.send(new ClientboundSetEntityDataPacket(this.entity.getId(), this.entity.getEntityData().packDirty()));
    }

    public void sendTeleportPacket(ServerPlayer player) {
        player.connection.send(new ClientboundTeleportEntityPacket(this.entity));
    }

    public void setWorld(Level world) {
        this.entity.level = world;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPos(x, y, z);
    }

    public void setText(String text) {
        this.text = text;

        if (this.text.equals("{empty}")) {
            this.entity.setCustomNameVisible(false);
            this.entity.setCustomName(Component.literal(" "));
        } else {
            this.entity.setCustomName(UtilChatColour.colour(this.text));
        }
    }

    public String getText() {
        return this.text;
    }
}
