package mod.acgaming.universaltweaks.bugfixes.misc.mousekeybind.mixin;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.common.FMLCommonHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class UTMinecraftMixin
{
    /**
     * @author decce6
     * @reason Mods usually handle keybinds by listening to the {@link net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent} event.
     * This event, however, is normally only fired on <b>keyboard</b> input. This means if a keybind is bound to a mouse button, the input
     * won't be detected by mods when user presses the button, until they subsequently press a key on the keyboard.
     * </p>
     * This mixin fixes the issue by firing an additional {@link net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent} event on mouse input.
     */
    @Inject(method = "runTickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireMouseInput()V", remap = false))
    private void utFireKeyboardInputOnMouseInput(CallbackInfo ci) {
        FMLCommonHandler.instance().fireKeyInput();
    }
}
