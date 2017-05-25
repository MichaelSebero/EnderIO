package crazypants.enderio.machine.baselegacy;

import crazypants.enderio.EnderIO;
import crazypants.enderio.power.ILegacyPoweredTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPowerStorage implements IMessage, IMessageHandler<PacketPowerStorage, IMessage> {

  private BlockPos pos;
  private int storedEnergy;

  public PacketPowerStorage() {
  }

  public PacketPowerStorage(ILegacyPoweredTile ent) {
    pos = ent.getLocation().getBlockPos();
    storedEnergy = ent.getEnergyStored(null);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeLong(pos.toLong());
    buf.writeInt(storedEnergy);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    if (pos != null)
      throw new RuntimeException("Oops, seems mc is recycling these messages. Need to copy them over before enqueuing them for the main thread");
    pos = BlockPos.fromLong(buf.readLong());
    storedEnergy = buf.readInt();
  }

  @Override
  public IMessage onMessage(PacketPowerStorage message, MessageContext ctx) {
    EntityPlayer player = EnderIO.proxy.getClientPlayer();
    if (player != null && player.world != null) {
      TileEntity te = player.world.getTileEntity(message.pos);
      if (te instanceof ILegacyPoweredTile) {
        ILegacyPoweredTile me = (ILegacyPoweredTile) te;
        me.setEnergyStored(message.storedEnergy);
      }
    }
    return null;
  }

}
