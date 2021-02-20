package network.nishi.interiorextensions.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

public class BlockAndon extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockAndon() {
        super(Material.WOOD);
        this.setRegistryName(InteriorExtension.MODID, "andon_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("andon_block");
        this.setHardness(0.5f);
        this.setLightLevel(1.0f);
        this.setSoundType(SoundType.WOOD);
        // Default Facing
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }


    protected static final AxisAlignedBB AABB_BASE =
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB AABB_ZERO =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn,
                                      BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ZERO);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_BASE;
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
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite().rotateY().rotateY());
    }

    // Create ItemStack metadata from IBlockState. It call when it drop and call texture model.
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }

    // Create IBlockState from ItemStack metadata. It call when block placed.
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    // Create initialized BlockStateContainer.
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }
}
