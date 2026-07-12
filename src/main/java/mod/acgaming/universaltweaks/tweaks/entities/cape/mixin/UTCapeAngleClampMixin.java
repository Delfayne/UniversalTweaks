package mod.acgaming.universaltweaks.tweaks.entities.cape.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.util.math.MathHelper;

import mod.acgaming.universaltweaks.config.UTConfigTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LayerCape.class)
public class UTCapeAngleClampMixin
{
    @ModifyVariable(
        method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V",
        at = @At(value = "STORE", ordinal = 0),
        ordinal = 9 // f2
    )
    private float utClampCapeLean(
        float capeLean,
        AbstractClientPlayer player,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks
    )
    {
        if (!UTConfigTweaks.ENTITIES.utCapeAngleClamp) return capeLean;

        float ticks = player.getTicksElytraFlying() + partialTicks;
        float fallFlyingScale = MathHelper.clamp(ticks * ticks / 100.0F, 0.0F, 1.0F);
        capeLean *= 1.0F - fallFlyingScale;
        return MathHelper.clamp(capeLean, 0.0F, 150.0F);
    }

    @ModifyVariable(
        method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V",
        at = @At(value = "STORE", ordinal = 0),
        ordinal = 10 // f3
    )
    private float utClampCapeLean2(float capeLean2)
    {
        if (!UTConfigTweaks.ENTITIES.utCapeAngleClamp) return capeLean2;

        return MathHelper.clamp(capeLean2, -20.0F, 20.0F);
    }
}
