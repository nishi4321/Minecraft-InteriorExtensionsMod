package network.nishi.interiorextensions.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import network.nishi.interiorextensions.InteriorExtension;

import java.util.Random;

public class WorldGenBamboo extends WorldGenerator {
    public WorldGenBamboo() {

    }

    private Block bamboo = InteriorExtension.BAMBOO_BLOCK;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 256; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(24), rand.nextInt(8) - rand.nextInt(8));
            if ((worldIn.isAirBlock(blockpos) || worldIn.getBlockState(blockpos).getBlock() == Blocks.TALLGRASS) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && this.bamboo.canPlaceBlockAt(worldIn, blockpos)){
                worldIn.setBlockState(blockpos, this.bamboo.getDefaultState(), 2);
            }
        }
        return true;
    }
}
