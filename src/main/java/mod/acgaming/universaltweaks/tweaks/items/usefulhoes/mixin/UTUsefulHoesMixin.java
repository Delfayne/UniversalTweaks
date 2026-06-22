package mod.acgaming.universaltweaks.tweaks.items.usefulhoes.mixin;

import mod.acgaming.universaltweaks.tweaks.items.usefulhoes.UTUsefulHoes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemHoe.class)
public class UTUsefulHoesMixin extends Item
{
    @Shadow protected ToolMaterial toolMaterial;

    /**
     * @author Invadermonky
     * Allows hoes to have faster harvesting speed for whitelisted blocks.
     */
    @Override
    public boolean canHarvestBlock(@NotNull IBlockState blockIn)
    {
        return super.canHarvestBlock(blockIn) || UTUsefulHoes.canHarvestBlock(blockIn);
    }

    /**
     * @author Invadermonky
     * Allows hoes to have faster harvesting speed for whitelisted blocks.
     */
    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull IBlockState state)
    {
        if(UTUsefulHoes.canHarvestBlock(state))
        {
            return toolMaterial.getEfficiency();
        }
        return super.getDestroySpeed(stack, state);
    }

    /**
     * @author Invadermonky
     * Allows hoes to be enchanted with Efficiency to increase their harvest speed for whitelisted blocks.
     */
    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment)
    {
        return enchantment == Enchantments.EFFICIENCY || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
