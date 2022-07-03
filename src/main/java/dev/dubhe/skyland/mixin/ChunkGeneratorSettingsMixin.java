package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.generator.SkyLandGeneratorSettings;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGeneratorSettings.class)
public class ChunkGeneratorSettingsMixin {
    @Inject(method = "<clinit>", at = @At(value = "HEAD"))
    private static void inject(CallbackInfo ci){
        register(SkyLandGeneratorSettings.OVER_NETHER_SETTINGS, createSurfaceSettings(false, false));
    }

    @Shadow private static void register(RegistryKey<ChunkGeneratorSettings> registryKey, ChunkGeneratorSettings settings){}
    @Shadow private static ChunkGeneratorSettings createSurfaceSettings(boolean amplified, boolean largeBiomes){return null;}
}
