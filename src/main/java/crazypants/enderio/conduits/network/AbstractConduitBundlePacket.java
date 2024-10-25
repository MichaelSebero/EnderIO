package crazypants.enderio.conduits.network;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;

import com.enderio.core.common.network.MessageTileEntity;

import io.netty.buffer.ByteBuf;

public abstract class AbstractConduitBundlePacket extends MessageTileEntity<TileEntity> {

    public AbstractConduitBundlePacket() {}

    public AbstractConduitBundlePacket(@Nonnull TileEntity tile) {
        super(tile);
    }

    @Override
    public final void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
    }

    @Override
    public final void toBytes(ByteBuf buf) {
        super.toBytes(buf);
    }
}
