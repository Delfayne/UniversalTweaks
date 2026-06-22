package mod.acgaming.universaltweaks.tweaks.items.usefulhoes;

import mod.acgaming.universaltweaks.UniversalTweaks;

import mod.acgaming.universaltweaks.config.UTConfigTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class UTUsefulHoes
{
    private static final Set<Block> HARVESTABLE_BLOCKS = new HashSet<>();

    public static boolean canHarvestBlock(IBlockState state)
    {
        return HARVESTABLE_BLOCKS.contains(state.getBlock());
    }

    public static void initLists() {
        HARVESTABLE_BLOCKS.clear();
        for(String str : UTConfigTweaks.ITEMS.USEFUL_HOES.utHoeHarvestableBlocks)
        {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str));
            if(block != null && block != Blocks.AIR)
            {
                HARVESTABLE_BLOCKS.add(block);
            }
            else
            {
                UniversalTweaks.LOGGER.error("No registered block found for {}", str);
            }
        }
    }
}
