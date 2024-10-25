package crazypants.enderio.base.block.painted;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.enderio.core.common.util.NullHelper;

import crazypants.enderio.base.ItemEIO;

public class BlockItemPaintedBlock extends ItemEIO {

    public BlockItemPaintedBlock(@Nonnull Block block) {
        super(block, true);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public @Nonnull String getTranslationKey(@Nonnull ItemStack stack) {
        if (block instanceof INamedSubBlocks) {
            return ((INamedSubBlocks) block).getUnlocalizedName(stack.getMetadata());
        } else {
            return super.getTranslationKey(stack);
        }
    }

    public interface INamedSubBlocks {

        @Nonnull
        String getUnlocalizedName(int meta);
    }

    @Override
    public @Nonnull CreativeTabs[] getCreativeTabs() {
        // Hack for JEI
        if (NullHelper.untrust(getCreativeTab()) != null) {
            return super.getCreativeTabs();
        } else {
            return new CreativeTabs[] {};
        }
    }
}
