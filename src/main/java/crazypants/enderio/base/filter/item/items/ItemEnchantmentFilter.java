package crazypants.enderio.base.filter.item.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;
import com.enderio.core.common.TileEntityBase;

import crazypants.enderio.api.IModObject;
import crazypants.enderio.base.EnderIOTab;
import crazypants.enderio.base.filter.FilterRegistry;
import crazypants.enderio.base.filter.IFilterContainer;
import crazypants.enderio.base.filter.gui.ContainerFilter;
import crazypants.enderio.base.filter.gui.EnchantmentFilterGui;
import crazypants.enderio.base.filter.item.EnchantmentFilter;
import crazypants.enderio.base.filter.item.IItemFilter;
import crazypants.enderio.base.init.ModObjectRegistry;
import crazypants.enderio.base.lang.Lang;
import crazypants.enderio.util.NbtValue;

public class ItemEnchantmentFilter extends Item implements IItemFilterItemUpgrade, IResourceTooltipProvider {

    private final int size;

    public static ItemEnchantmentFilter createNormal(@Nonnull IModObject modObject, @Nullable Block block) {
        return new ItemEnchantmentFilter(modObject, 1 * 5);
    }

    public static ItemEnchantmentFilter createBig(@Nonnull IModObject modObject, @Nullable Block block) {
        return new ItemEnchantmentFilter(modObject, 2 * 5);
    }

    protected ItemEnchantmentFilter(@Nonnull IModObject modObject, int size) {
        setCreativeTab(EnderIOTab.tabEnderIOItems);
        modObject.apply(this);
        setMaxDamage(0);
        setHasSubtypes(true);
        setMaxStackSize(64);
        this.size = size;
    }

    @Override
    public IItemFilter createFilterFromStack(@Nonnull ItemStack stack) {
        EnchantmentFilter filter = new EnchantmentFilter();
        filter.setSlotCount(size);
        NBTTagCompound tag = NbtValue.FILTER.getTag(stack);
        if (!tag.isEmpty()) {
            filter.readFromNBT(tag);
        }
        return filter;
    }

    @Override
    @Nonnull
    public String getUnlocalizedNameForTooltip(@Nonnull ItemStack itemStack) {
        return getTranslationKey();
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player,
                                                    @Nonnull EnumHand hand) {
        if (!world.isRemote && player.isSneaking()) {
            ModObjectRegistry.getModObjectNN(this).openGui(world, player.getPosition(), player, null, hand.ordinal());
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip,
                               @Nonnull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (FilterRegistry.isFilterSet(stack)) {
            if (SpecialTooltipHandler.showAdvancedTooltips()) {
                tooltip.add(Lang.ITEM_FILTER_CONFIGURED.get(TextFormatting.ITALIC));
                tooltip.add(Lang.ITEM_FILTER_CLEAR.get(TextFormatting.ITALIC));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public GuiScreen getClientGuiElement(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos,
                                         @Nullable EnumFacing facing, int param1) {
        Container container = player.openContainer;
        if (container instanceof IFilterContainer) {
            return new EnchantmentFilterGui(player.inventory,
                    new ContainerFilter(player, (TileEntityBase) world.getTileEntity(pos), facing, param1),
                    world.getTileEntity(pos), ((IFilterContainer<IItemFilter>) container).getFilter(param1));
        } else {
            return new EnchantmentFilterGui(player.inventory, new ContainerFilter(player, null, facing, param1), null,
                    FilterRegistry.getFilterForUpgrade(player.getHeldItem(EnumHand.values()[param1])));
        }
    }
}
