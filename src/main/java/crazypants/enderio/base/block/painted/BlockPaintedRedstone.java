package crazypants.enderio.base.block.painted;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.NullHelper;

import crazypants.enderio.api.IModObject;
import crazypants.enderio.base.paint.IPaintable;
import crazypants.enderio.base.paint.PaintUtil;
import crazypants.enderio.base.paint.render.PaintHelper;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.painter.BasicPainterTemplate;
import crazypants.enderio.base.render.IBlockStateWrapper;
import crazypants.enderio.base.render.ICustomSubItems;
import crazypants.enderio.base.render.pipeline.BlockStateWrapperBase;
import crazypants.enderio.base.render.registry.SmartModelAttacher;
import crazypants.enderio.util.Prep;

public abstract class BlockPaintedRedstone extends BlockCompressedPowered
                                           implements ITileEntityProvider, IPaintable.IBlockPaintableBlock,
                                           IModObject.WithBlockItem, ICustomSubItems {

    public static BlockPaintedRedstone create_solid(@Nonnull IModObject modObject) {
        BlockPaintedRedstone result = new BlockPaintedRedstoneSolid(modObject);
        result.init(modObject);
        return result;
    }

    public static BlockPaintedRedstone create(@Nonnull IModObject modObject) {
        BlockPaintedRedstone result = new BlockPaintedRedstoneNonSolid(modObject);
        result.init(modObject);
        return result;
    }

    public static class BlockPaintedRedstoneSolid extends BlockPaintedRedstone
                                                  implements IPaintable.ISolidBlockPaintableBlock {

        protected BlockPaintedRedstoneSolid(@Nonnull IModObject modObject) {
            super(modObject);
        }
    }

    public static class BlockPaintedRedstoneNonSolid extends BlockPaintedRedstone
                                                     implements IPaintable.INonSolidBlockPaintableBlock {

        protected BlockPaintedRedstoneNonSolid(@Nonnull IModObject modObject) {
            super(modObject);
            useNeighborBrightness = true;
            setLightOpacity(0);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getAmbientOcclusionLightValue(@Nonnull IBlockState bs) {
            return 1;
        }

        @Override
        public boolean doesSideBlockRendering(@Nonnull IBlockState bs, @Nonnull IBlockAccess world,
                                              @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
            return false;
        }
    }

    protected BlockPaintedRedstone(@Nonnull IModObject modObject) {
        super(Material.IRON, MapColor.TNT);
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
        Prep.setNoCreativeTab(this);
        modObject.apply(this);
    }

    private void init(@Nonnull IModObject modObject) {
        MachineRecipeRegistry.instance.registerRecipe(MachineRecipeRegistry.PAINTER,
                new BasicPainterTemplate<BlockPaintedRedstone>(this, Blocks.REDSTONE_BLOCK));
        SmartModelAttacher.registerNoProps(this);
    }

    @Override
    public Item createBlockItem(@Nonnull IModObject modObject) {
        return modObject.apply(new BlockItemPaintedBlock(this));
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState bs, @Nonnull World world, @Nonnull BlockPos pos,
                                   @Nonnull EntityPlayer player, boolean willHarvest) {
        if (willHarvest) {
            return true;
        }
        return super.removedByPlayer(bs, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos,
                             @Nonnull IBlockState state, @Nullable TileEntity te,
                             @Nonnull ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        super.removedByPlayer(state, worldIn, pos, player, true);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos,
                         @Nonnull IBlockState state, int fortune) {
        NNList<ItemStack> drops2 = new NNList<>();
        super.getDrops(drops2, world, pos, state, fortune);
        for (ItemStack drop : drops2) {
            PaintUtil.setSourceBlock(NullHelper.notnullM(drop, "null stack from getDrops()"),
                    getPaintSource(state, world, pos));
        }
        drops.addAll(drops2);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileEntityPaintedBlock();
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                @Nonnull EntityLivingBase player,
                                @Nonnull ItemStack stack) {
        setPaintSource(state, world, pos, PaintUtil.getSourceBlock(stack));
        if (!world.isRemote) {
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public @Nonnull ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target,
                                           @Nonnull World world, @Nonnull BlockPos pos,
                                           @Nonnull EntityPlayer player) {
        final ItemStack pickBlock = super.getPickBlock(state, target, world, pos, player);
        PaintUtil.setSourceBlock(pickBlock, getPaintSource(state, world, pos));
        return pickBlock;
    }

    @Override
    public @Nonnull IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world,
                                                 @Nonnull BlockPos pos) {
        IBlockStateWrapper blockStateWrapper = new BlockStateWrapperBase(state, world, pos, null);
        blockStateWrapper.addCacheKey(0);
        blockStateWrapper.bakeModel();
        return blockStateWrapper;
    }

    @Override
    public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        // Painted blocks don't show in the Creative Inventory or JEI
    }

    @Override
    @Nonnull
    public NNList<ItemStack> getSubItems() {
        return getSubItems(this, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(@Nonnull IBlockState state, @Nonnull World world, @Nonnull RayTraceResult target,
                                 @Nonnull ParticleManager effectRenderer) {
        return PaintHelper.addHitEffects(state, world, target, effectRenderer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(@Nonnull World world, @Nonnull BlockPos pos,
                                     @Nonnull ParticleManager effectRenderer) {
        return PaintHelper.addDestroyEffects(world, pos, effectRenderer);
    }
}
