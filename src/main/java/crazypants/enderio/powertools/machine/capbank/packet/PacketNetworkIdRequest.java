package crazypants.enderio.powertools.machine.capbank.packet;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import crazypants.enderio.powertools.machine.capbank.TileCapBank;

public class PacketNetworkIdRequest extends PacketCapBank<PacketNetworkIdRequest, PacketNetworkIdResponse> {

    public PacketNetworkIdRequest() {}

    public PacketNetworkIdRequest(@Nonnull TileCapBank capBank) {
        super(capBank);
    }

    @Override
    protected PacketNetworkIdResponse handleMessage(TileCapBank te, PacketNetworkIdRequest message,
                                                    MessageContext ctx) {
        if (te.getNetwork() != null) {
            return new PacketNetworkIdResponse(te);
        }
        return null;
    }
}
