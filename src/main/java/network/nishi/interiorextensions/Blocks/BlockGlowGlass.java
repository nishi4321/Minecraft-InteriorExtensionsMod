package network.nishi.interiorextensions.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.nishi.interiorextensions.InteriorExtension;

public class BlockGlowGlass extends Block {
    public BlockGlowGlass() {
        super(Material.GLASS);
        this.setRegistryName(InteriorExtension.MODID,"glow_glass_block");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("glow_glass_block");
        this.setHardness(0.5f);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(1.0f);
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
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
