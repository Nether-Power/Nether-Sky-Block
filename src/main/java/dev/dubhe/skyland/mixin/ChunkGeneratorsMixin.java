package dev.dubhe.skyland.mixin;

import com.mojang.serialization.Codec;
import dev.dubhe.skyland.SkyLand;
import dev.dubhe.skyland.SkyLandChunkGenerator;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerators.class)
public class ChunkGeneratorsMixin {
    @Inject(method = "registerAndGetDefault", at = @At("RETURN"))
    private static void register(Registry<Codec<? extends ChunkGenerator>> registry, CallbackInfoReturnable<Codec<? extends ChunkGenerator>> cir) {
        Registry.register(registry, SkyLand.ID, SkyLandChunkGenerator.CODEC);
    }
}
