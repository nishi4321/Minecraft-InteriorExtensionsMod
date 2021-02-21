package network.nishi.interiorextensions.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.nishi.interiorextensions.InteriorExtension;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBamboo extends Block {
    public static final PropertyBool isGROW = PropertyBool.create("isgrow");

    public BlockBamboo() {
        super(Material.LEAVES);
        this.setRegistryName(InteriorExtension.MODID, "bamboo_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("bamboo_block");
        this.setHardness(1.0f);
        this.setLightLevel(0.0f);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        this.setHarvestLevel("axe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(isGROW, Boolean.valueOf(false)));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockPos new_pos = pos.offset(EnumFacing.DOWN);
        IBlockState state = worldIn.getBlockState(new_pos);
        if (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS) {
            this.setSoundType(SoundType.PLANT);
            return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        } else if (state.getBlock() == this) {
            this.setSoundType(SoundType.WOOD);
            return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        }
        return false;
    }

    protected static final AxisAlignedBB AABB_BASE_FALSE =
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.8125D, 0.75D);
    protected static final AxisAlignedBB AABB_BASE_TRUE =
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D);

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn,
                                      BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean p_185477_7_) {
        if (state.getValue(isGROW)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE_TRUE);
        } else {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE_FALSE);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(isGROW)) {
            return AABB_BASE_TRUE;
        } else {
            return AABB_BASE_FALSE;
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            BlockPos new_pos = pos.offset(EnumFacing.DOWN);
            if (worldIn.isAirBlock(new_pos)) {
                this.setSoundType(SoundType.WOOD);
                worldIn.destroyBlock(pos, true);
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!state.getValue(isGROW)) {
                worldIn.setBlockState(pos, state.withProperty(isGROW, true));
            }
            BlockPos new_pos = pos.offset(EnumFacing.UP);
            if (worldIn.isAirBlock(new_pos)) {
                if (getBambooLength(worldIn, pos) < 14) {
                    worldIn.setBlockState(new_pos, state.withProperty(isGROW, true));
                }
            }
        }
    }

    public int getBambooLength(World worldIn, BlockPos pos) {
        int length = 0;
        for (int i = 0; i < 16; i++) {
            if (pos.down(i).getY() >= 0 && worldIn.getBlockState(pos.down(i)).getBlock() == this) {
                length++;
            } else {
                return length;
            }
        }
        return length;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        BlockPos new_pos = pos.offset(EnumFacing.DOWN);
        if (worldIn.getBlockState(new_pos).getBlock() == this) {
            worldIn.setBlockState(new_pos, this.getDefaultState().withProperty(isGROW, true));
            return this.getDefaultState().withProperty(isGROW, true);
        }
        return this.getDefaultState();
    }

    // Create ItemStack metadata from IBlockState. It call when it drop and call texture model.
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (((Boolean) state.getValue(isGROW)).booleanValue()) {
            i += 1;
        }
        return i;
    }

    // Create IBlockState from ItemStack metadata. It call when block placed.
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 0) {
            return this.getDefaultState().withProperty(isGROW, false);
        } else {
            return this.getDefaultState().withProperty(isGROW, true);
        }
    }

    // Create initialized BlockStateContainer.
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{isGROW});
    }
}
