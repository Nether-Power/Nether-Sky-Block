package dev.dubhe.skyland.mixin;


import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {

    @Redirect(
            method = "start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;getLifecycle()Lcom/mojang/serialization/Lifecycle;"),
            require = 1
    )
    private Lifecycle removeAdviceOnLoad(SaveProperties properties) {
        Lifecycle original = properties.getLifecycle();
        if (original == Lifecycle.stable() || original == Lifecycle.experimental()) {
            return Lifecycle.stable();
        } else {
            return original;
        }
    }

    @Inject(
            method = "tryLoad(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Lcom/mojang/serialization/Lifecycle;Ljava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private static void removeAdviceOnCreation(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, CallbackInfo ci) {
        loader.run();
        ci.cancel();
    }
}
