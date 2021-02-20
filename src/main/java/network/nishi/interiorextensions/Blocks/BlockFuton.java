package network.nishi.interiorextensions.Blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.nishi.interiorextensions.InteriorExtension;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFuton extends BlockHorizontal {

    public static final PropertyEnum<BlockFuton.EnumPartType> PART = PropertyEnum.<BlockFuton.EnumPartType>create("part", BlockFuton.EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

    protected static final AxisAlignedBB AABB_BASE =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    public BlockFuton() {
        super(Material.CLOTH);
        this.setRegistryName(InteriorExtension.MODID, "futon_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("futon_block");
        this.setHardness(0.2f);
        this.setSoundType(SoundType.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, EnumPartType.FOOT).withProperty(OCCUPIED, Boolean.valueOf(false)).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(!worldIn.isRemote) {
            BlockPos new_pos = pos.offset((EnumFacing) state.getValue(FACING));
            if (worldIn.isAirBlock(new_pos)) {
                worldIn.setBlockState(new_pos, this.getDefaultState().withProperty(PART, EnumPartType.HEAD).withProperty(OCCUPIED, Boolean.valueOf(false)).withProperty(FACING, (EnumFacing) state.getValue(FACING)));
            } else {
                worldIn.setBlockToAir(pos);
                if (!worldIn.isRemote) {
                    placer.dropItem(stack.getItem(), 1);
                }
            }
        }
    }

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
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite().rotateY().rotateY());
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        // ブロック破壊したときにEntity削除
        if (world.isRemote) {
            return;
        }else{
            BlockPos new_pos = pos;
            EnumPartType part = state.getValue(PART);
            if (part == EnumPartType.FOOT) {
                // FOOT
                new_pos = pos.offset((EnumFacing) state.getValue(FACING));
                if (world.getBlockState(new_pos).getBlock() == this) {
                    if (world.getBlockState(new_pos).getValue(PART) == EnumPartType.FOOT) {
                        return;
                    } else {
                        if ((EnumFacing) state.getValue(FACING) != world.getBlockState(new_pos).getValue(FACING)) {
                            return;
                        }
                    }
                }
            } else {
                // HEAD
                new_pos = pos.offset((EnumFacing) state.getValue(FACING), -1);
            }
            if (world.getBlockState(new_pos).getBlock() == this) {
                world.setBlockToAir(new_pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }else {
            if (state.getValue(PART) != EnumPartType.HEAD) {
                pos = pos.offset((EnumFacing) state.getValue(FACING));
                state = worldIn.getBlockState(pos);

                if (state.getBlock() != this) {
                    return true;
                }
            }

            net.minecraft.world.WorldProvider.WorldSleepResult sleepResult = worldIn.provider.canSleepAt(playerIn, pos);
            if (sleepResult != net.minecraft.world.WorldProvider.WorldSleepResult.BED_EXPLODES) {
                if (sleepResult == net.minecraft.world.WorldProvider.WorldSleepResult.DENY) return true;
                if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
                    EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

                    if (entityplayer != null) {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                        return true;
                    }

                    state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
                    worldIn.setBlockState(pos, state, 4);
                }

                EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);

                if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
                    state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
                    worldIn.setBlockState(pos, state, 4);
                    return true;
                } else {
                    if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                    } else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE) {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
                    } else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
                    }

                    return true;
                }
            } else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

                if (worldIn.getBlockState(blockpos).getBlock() == this) {
                    worldIn.setBlockToAir(blockpos);
                }

                worldIn.newExplosion((Entity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 0.0F, true, true);
                return true;
            }
        }
    }


    @Nullable
    private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
        for (EntityPlayer entityplayer : worldIn.playerEntities) {
            if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
                return entityplayer;
            }
        }

        return null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, BlockFuton.EnumPartType.HEAD).withProperty(FACING, enumfacing).withProperty(OCCUPIED, Boolean.valueOf((meta & 4) > 0)) : this.getDefaultState().withProperty(PART, BlockFuton.EnumPartType.FOOT).withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

        if (state.getValue(PART) == BlockFuton.EnumPartType.HEAD) {
            i |= 8;

            if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, PART, OCCUPIED});
    }

    public static enum EnumPartType implements IStringSerializable {
        HEAD("head"),
        FOOT("foot");

        private final String name;

        private EnumPartType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
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
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {
        return true;
    }
}
