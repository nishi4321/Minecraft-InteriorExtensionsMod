package network.nishi.interiorextensions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.nishi.interiorextensions.Blocks.*;
import network.nishi.interiorextensions.Entities.EntityZabuton;
import network.nishi.interiorextensions.world.WorldGenBamboo;

import java.util.Random;

@Mod(modid = InteriorExtension.MODID, version = InteriorExtension.VERSION, name = InteriorExtension.MODNAME)
public class InteriorExtension {

    public static final String MODID = "interiorextensions";
    public static final String MODNAME = "Interior Extension Mod";
    public static final String VERSION = "1.3.0";

    @Mod.Metadata(MODID)
    public static ModMetadata meta;

    public static final Block TATAMI_BLOCK = new BlockTatami();
    public static final Block TAKE_FENCE = new BlockBambooFence();
    public static final Block LAMP_BLOCK = new BlockLamp();
    public static final Block GLOW_GLASS_BLOCK = new BlockGlowGlass();
    public static final Block ZABUTON_BLOCK = new BlockZabuton();
    public static final Block FUTON_BLOCK = new BlockFuton();
    public static final Block ANDON_BLOCK = new BlockAndon();
    public static final Block BAMBOO_BLOCK = new BlockBamboo();

    public static BlockHalfGrassPath HALF_GRASS_PATH_BLOCK_HALF = new BlockHalfGrassPath.Half("grasspath_slab", Material.PLANTS);
    public static BlockHalfGrassPath HALF_GRASS_PATH_BLOCK_DOUBLE = new BlockHalfGrassPath.Double("double_grasspath_slab", Material.PLANTS);
    public static BlockHalfGlass HALF_GLASS_HALF = new BlockHalfGlass.Half("glass_slab", Material.GLASS);
    public static BlockHalfGlass HALF_GLASS_DOUBLE = new BlockHalfGlass.Double("double_glass_slab", Material.GLASS);
    public static BlockHalfGlowGlass HALF_GLOW_GLASS_HALF = new BlockHalfGlowGlass.Half("glow_glass_slab", Material.GLASS);
    public static BlockHalfGlowGlass HALF_GLOW_GLASS_DOUBLE = new BlockHalfGlowGlass.Double("double_glow_glass_slab", Material.GLASS);

    @Mod.EventHandler
    public void perInit(FMLPreInitializationEvent event) {
        ModInfo.loadInfo(meta);
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {
        GameRegistry.addShapedRecipe(new ResourceLocation("dirt_to_half"), new ResourceLocation(MODID), new ItemStack(HALF_GRASS_PATH_BLOCK_HALF, 6), new Object[]{"   ", "   ", "XXX", 'X', Blocks.DIRT});
        GameRegistry.addShapedRecipe(new ResourceLocation("glass_to_half"), new ResourceLocation(MODID), new ItemStack(HALF_GLASS_HALF, 6), new Object[]{"   ", "   ", "XXX", 'X', Blocks.GLASS});
        GameRegistry.addShapedRecipe(new ResourceLocation("glow_glass_to_half"), new ResourceLocation(MODID), new ItemStack(HALF_GLOW_GLASS_HALF, 6), new Object[]{"   ", "   ", "XXX", 'X', GLOW_GLASS_BLOCK});
    }

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        EntityEntry entry = EntityEntryBuilder.create().entity(EntityZabuton.class).id(new ResourceLocation(MODID), 0).name("EntityZabuton").tracker(64, 20, false).build();
        event.getRegistry().register(entry);
    }

    @SubscribeEvent
    public void decorate(DecorateBiomeEvent.Decorate event) {
        if (event.getType() == DecorateBiomeEvent.Decorate.EventType.FLOWERS) {
            Biome biome = event.getWorld().getBiomeForCoordsBody(event.getPos());
            if(biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_EDGE || biome == Biomes.JUNGLE_HILLS) {
                Random rand = event.getRand();
                if (rand.nextDouble() > 0.1) return;
                int x = rand.nextInt(16) + 8;
                int y = rand.nextInt(16) + 8;
                WorldGenBamboo genBamboo = new WorldGenBamboo();
                genBamboo.generate(event.getWorld(), event.getRand(), event.getPos().add(x, 63, y));
            }
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(TATAMI_BLOCK).setRegistryName(MODID, "tatami_block"));
        event.getRegistry().register(new ItemBlock(TAKE_FENCE).setRegistryName(MODID, "take_fence"));
        event.getRegistry().register(new ItemBlock(LAMP_BLOCK).setRegistryName(MODID, "lamp_block"));
        event.getRegistry().register(new ItemBlock(GLOW_GLASS_BLOCK).setRegistryName(MODID, "glow_glass_block"));
        event.getRegistry().register(new ItemBlock(ZABUTON_BLOCK).setRegistryName(MODID, "zabuton_block"));
        event.getRegistry().register(new ItemBlock(FUTON_BLOCK).setRegistryName(MODID, "futon_block"));
        event.getRegistry().register(new ItemBlock(ANDON_BLOCK).setRegistryName(MODID, "andon_block"));
        event.getRegistry().register(new ItemBlock(BAMBOO_BLOCK).setRegistryName(MODID, "bamboo_block"));
        event.getRegistry().register(new ItemSlab(HALF_GRASS_PATH_BLOCK_HALF, HALF_GRASS_PATH_BLOCK_HALF, HALF_GRASS_PATH_BLOCK_DOUBLE).setRegistryName(HALF_GRASS_PATH_BLOCK_HALF.getRegistryName()));
        event.getRegistry().register(new ItemSlab(HALF_GLASS_HALF, HALF_GLASS_HALF, HALF_GLASS_DOUBLE).setRegistryName(HALF_GLASS_HALF.getRegistryName()));
        event.getRegistry().register(new ItemSlab(HALF_GLOW_GLASS_HALF, HALF_GLOW_GLASS_HALF, HALF_GLOW_GLASS_DOUBLE).setRegistryName(HALF_GLOW_GLASS_HALF.getRegistryName()));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(TATAMI_BLOCK);
        event.getRegistry().register(TAKE_FENCE);
        event.getRegistry().register(LAMP_BLOCK);
        event.getRegistry().register(GLOW_GLASS_BLOCK);
        event.getRegistry().register(ZABUTON_BLOCK);
        event.getRegistry().register(FUTON_BLOCK);
        event.getRegistry().register(ANDON_BLOCK);
        event.getRegistry().register(BAMBOO_BLOCK);
        event.getRegistry().register(HALF_GRASS_PATH_BLOCK_HALF);
        event.getRegistry().register(HALF_GRASS_PATH_BLOCK_DOUBLE);
        event.getRegistry().register(HALF_GLASS_HALF);
        event.getRegistry().register(HALF_GLASS_DOUBLE);
        event.getRegistry().register(HALF_GLOW_GLASS_HALF);
        event.getRegistry().register(HALF_GLOW_GLASS_DOUBLE);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TATAMI_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "tatami_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TAKE_FENCE), 0, new ModelResourceLocation(new ResourceLocation(MODID, "take_fence"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LAMP_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "lamp_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(GLOW_GLASS_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "glow_glass_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ZABUTON_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "zabuton_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(FUTON_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "futon_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ANDON_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "andon_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BAMBOO_BLOCK), 0, new ModelResourceLocation(new ResourceLocation(MODID, "bamboo_block"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GRASS_PATH_BLOCK_HALF), 0, new ModelResourceLocation(new ResourceLocation(MODID, "grasspath_slab"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GRASS_PATH_BLOCK_DOUBLE), 0, new ModelResourceLocation(new ResourceLocation(MODID, "double_grasspath_slab"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GLASS_HALF), 0, new ModelResourceLocation(new ResourceLocation(MODID, "glass_slab"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GLASS_DOUBLE), 0, new ModelResourceLocation(new ResourceLocation(MODID, "double_glass_slab"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GLOW_GLASS_HALF), 0, new ModelResourceLocation(new ResourceLocation(MODID, "glow_glass_slab"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HALF_GLOW_GLASS_DOUBLE), 0, new ModelResourceLocation(new ResourceLocation(MODID, "double_glow_glass_slab"), "inventory"));
    }
}
