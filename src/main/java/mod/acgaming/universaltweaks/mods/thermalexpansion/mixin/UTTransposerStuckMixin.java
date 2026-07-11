package mod.acgaming.universaltweaks.mods.thermalexpansion.mixin;

import cofh.core.fluid.FluidTankCore;
import cofh.thermalexpansion.block.machine.TileMachineBase;
import cofh.thermalexpansion.block.machine.TileTransposer;
import mod.acgaming.universaltweaks.config.UTConfigMods;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileTransposer.class, remap = false)
public abstract class UTTransposerStuckMixin extends TileMachineBase {

    @Shadow
    public boolean extractMode;

    @Shadow
    private boolean hasFluidHandler;

    @Shadow
    private FluidTankCore tank;

    @Shadow
    protected abstract void transferHandler();

    /**
     * The center (in-process) slot is only emptied by transferHandler(), which is only reached from
     * the active and finished path. If a fluid-handler item lands there that canStartHandler() can
     * never satisfy, the idle branch never retries the move and the machine wedges until a player
     * pulls the item out. This adds a flush to remove items during the machine's idle tick
     * processing cycle. This doesn't prevent the problem, but it heals anything currently in the
     * bad state. It's in a rarely hit code path, otherwise.
     */
    @Inject(method = "updateHandler", at = @At("TAIL"))
    private void utTransposerUnstick(CallbackInfo ci) {
        if (!UTConfigMods.THERMAL_EXPANSION.utTransposerStuckFixToggle || world.isRemote) {
            return;
        }
        if (isActive || !hasFluidHandler || inventory[1].isEmpty() || !redstoneControlOrDisable()
            || !timeCheckEighth()) {
            return;
        }
        if (ut$isHandlerSpent()) {
            // transferHandler() may re-fill the process slot from the input slot, so hasFluidHandler is
            // not a reliable "did anything move" signal.
            // However, the process slot reference only changes on a real transfer.
            ItemStack processStack = inventory[1];
            transferHandler();
            if (inventory[1] != processStack) {
                markChunkDirty();
            }
        }
    }

    /**
     * True only when the item in the process slot can never be processed in the current mode
     * (already full/wrong fluid in fill mode, empty container in extract mode) - not merely waiting
     * on energy, output space, or tank state.
     */
    @Unique
    private boolean ut$isHandlerSpent() {
        IFluidHandlerItem handler = inventory[1].getCapability(
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (handler == null) {
            return false;
        }
        if (!extractMode) {
            return tank.getFluidAmount() > 0
                && handler.fill(new FluidStack(tank.getFluid(), Fluid.BUCKET_VOLUME), false) <= 0;
        }
        FluidStack drainStack = handler.drain(Fluid.BUCKET_VOLUME, false);
        return tank.getSpace() > 0 && drainStack == null;
    }
}
