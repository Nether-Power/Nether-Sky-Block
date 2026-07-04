package dev.dubhe.skyland.mixin.client;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldOpenFlows.class)
public class IntegratedServerLoaderMixin {
    @Redirect(
        method = "openWorld",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Lifecycle;equals(Ljava/lang/Object;)Z"
        ),
        require = 0
    )
    private boolean skipExperimentalWarning(Lifecycle instance, Object other) {
        return true;
    }
}
