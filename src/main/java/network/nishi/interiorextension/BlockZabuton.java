package network.nishi.interiorextension;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockZabuton extends Block {

    public BlockZabuton() {
        super(Material.GOURD);
        this.setRegistryName("interiorextension","zabuton_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("zabuton_block");
        this.setHardness(0.2f);
        this.setSoundType(SoundType.SNOW);
    }

    protected static final AxisAlignedBB AABB_BASE =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST =
            new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn,
                                      BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) {
            return true;
        }

        Entity entity = new EntityZabuton(worldIn);
        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        playerIn.startRiding(entity);
        worldIn.spawnEntity(entity);

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        // ブロック破壊したときにEntity削除
        if(world.isRemote) {
            List<Entity> entities = world.getLoadedEntityList();
            for (Entity e : entities) {
                if (pos.getX() == (int) (e.posX - 0.5) && pos.getY() == (int) e.posY && pos.getZ() == (int) (e.posZ - 0.5)) {
                    world.removeEntity(e);
                }
            }
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
}
