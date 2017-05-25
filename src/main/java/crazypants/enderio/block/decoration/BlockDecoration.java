package crazypants.enderio.block.decoration;

import java.util.Map;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.NNList.NNIterator;

import crazypants.enderio.EnderIOTab;
import crazypants.enderio.init.IModObject;
import crazypants.enderio.render.IHaveRenderers;
import crazypants.enderio.render.property.EnumDecoBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDecoration extends Block implements IHaveRenderers {

  public static BlockDecoration create(@Nonnull IModObject modObject) {
    return new BlockDecoration(modObject).init(modObject);
  }

  protected BlockDecoration(@Nonnull IModObject modObject) {
    super(Material.ROCK);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setUnlocalizedName(modObject.getUnlocalisedName());
    setRegistryName(modObject.getUnlocalisedName());
    setHardness(0.5F);
    setSoundType(SoundType.METAL);
    setHarvestLevel("pickaxe", 0);
    initDefaultState();
  }

  protected void initDefaultState() {
    setDefaultState(this.blockState.getBaseState());
  }

  @Override
  protected @Nonnull BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { EnumDecoBlock.TYPE });
  }

  protected BlockDecoration init(@Nonnull IModObject modObject) {
    GameRegistry.register(this);
    GameRegistry.register(new ItemBlockDecoration(this, modObject.getUnlocalisedName()) {
      @Override
      public @Nonnull String getUnlocalizedName(@Nonnull ItemStack stack) {
        return EnumDecoBlock.getTypeFromMeta(stack.getMetadata()).getUnlocalizedName(this);
      }
    });
    return this;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(@Nonnull Item itemIn, @Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
    NNIterator<EnumDecoBlock> iterator = NNList.of(EnumDecoBlock.class).iterator();
    while (iterator.hasNext()) {
      EnumDecoBlock type = iterator.next();
      list.add(new ItemStack(itemIn, 1, EnumDecoBlock.getMetaFromType(type)));
    }
  }

  @Override
  public @Nonnull IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(EnumDecoBlock.TYPE, EnumDecoBlock.getTypeFromMeta(meta));
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {
    return EnumDecoBlock.getMetaFromType(state.getValue(EnumDecoBlock.TYPE));
  }

  @Override
  public int damageDropped(@Nonnull IBlockState st) {
    return getMetaFromState(st);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerRenderers(@Nonnull IModObject modObject) {
    Item item = Item.getItemFromBlock(this);
    Map<IBlockState, ModelResourceLocation> locations = new DefaultStateMapper().putStateModelLocations(this);
    NNIterator<EnumDecoBlock> iterator = NNList.of(EnumDecoBlock.class).iterator();
    while (iterator.hasNext()) {
      EnumDecoBlock type = iterator.next();
      IBlockState state = getDefaultState().withProperty(EnumDecoBlock.TYPE, type);
      ModelResourceLocation mrl = locations.get(state);
      ModelLoader.setCustomModelResourceLocation(item, EnumDecoBlock.getMetaFromType(type), mrl);
    }
  }

  @Override
  public @Nonnull BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    EnumDecoBlock type = state.getValue(EnumDecoBlock.TYPE);
    return type != EnumDecoBlock.TYPE00 && type != EnumDecoBlock.TYPE14 && type != EnumDecoBlock.TYPE15;
  }

  @Override
  public boolean isSideSolid(@Nonnull IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return isOpaqueCube(base_state);
  }

}