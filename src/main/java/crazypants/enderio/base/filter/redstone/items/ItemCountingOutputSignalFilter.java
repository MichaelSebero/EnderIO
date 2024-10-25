package crazypants.enderio.base.filter.redstone.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.enderio.core.common.TileEntityBase;

import crazypants.enderio.api.IModObject;
import crazypants.enderio.base.EnderIOTab;
import crazypants.enderio.base.filter.FilterRegistry;
import crazypants.enderio.base.filter.IFilterContainer;
import crazypants.enderio.base.filter.gui.ContainerFilter;
import crazypants.enderio.base.filter.gui.IncrementingValueFilterGui;
import crazypants.enderio.base.filter.redstone.CountingOutputSignalFilter;
import crazypants.enderio.base.filter.redstone.IOutputSignalFilter;
import crazypants.enderio.util.NbtValue;

public class ItemCountingOutputSignalFilter extends Item implements IItemOutputSignalFilterUpgrade {

    public static ItemCountingOutputSignalFilter create(@Nonnull IModObject modObject, @Nullable Block block) {
        return new ItemCountingOutputSignalFilter(modObject);
    }

    public ItemCountingOutputSignalFilter(@Nonnull IModObject modObject) {
        setCreativeTab(EnderIOTab.tabEnderIOItems);
        modObject.apply(this);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(64);
    }

    @Override
    public IOutputSignalFilter createFilterFromStack(@Nonnull ItemStack stack) {
        CountingOutputSignalFilter filter = new CountingOutputSignalFilter();
        if (NbtValue.FILTER.hasTag(stack)) {
            filter.readFromNBT(NbtValue.FILTER.getTag(stack));
        }
        return filter;
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nullable
    public GuiScreen getClientGuiElement(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos,
                                         @Nullable EnumFacing facing, int param1) {
        Container container = player.openContainer;
        if (container instanceof IFilterContainer) {
            return new IncrementingValueFilterGui(player.inventory,
                    new ContainerFilter(player, (TileEntityBase) world.getTileEntity(pos), facing, param1),
                    world.getTileEntity(pos),
                    ((IFilterContainer<CountingOutputSignalFilter>) container).getFilter(param1));
        } else {
            return new IncrementingValueFilterGui(player.inventory, new ContainerFilter(player, null, facing, param1),
                    null,
                    FilterRegistry.getFilterForUpgrade(player.getHeldItem(EnumHand.values()[param1])));
        }
    }
}
