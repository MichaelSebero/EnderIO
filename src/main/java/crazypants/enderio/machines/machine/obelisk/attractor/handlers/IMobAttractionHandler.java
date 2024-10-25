package crazypants.enderio.machines.machine.obelisk.attractor.handlers;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLiving;

import crazypants.enderio.machines.machine.obelisk.attractor.TileAttractor;

public interface IMobAttractionHandler {

    enum State {
        CAN_ATTRACT,
        CANNOT_ATTRACT,
        ALREADY_ATTRACTING;
    }

    @Nonnull
    State canAttract(TileAttractor attractor, EntityLiving entity);

    void startAttracting(TileAttractor attractor, EntityLiving entity);

    void tick(TileAttractor attractor, EntityLiving entity);

    void release(TileAttractor attractor, EntityLiving entity);
}
