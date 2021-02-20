package network.nishi.interiorextensions.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import network.nishi.interiorextensions.InteriorExtension;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLamp extends Block {

    public BlockLamp() {
        super(Material.LEAVES);
        this.setRegistryName(InteriorExtension.MODID,"lamp_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("lamp_block");
        this.setHardness(0.5f);
        this.setLightLevel(1.0f);
        this.setSoundType(SoundType.GLASS);
    }

    protected static final AxisAlignedBB AABB_BASE =
            new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn,
                                      BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);
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
}
