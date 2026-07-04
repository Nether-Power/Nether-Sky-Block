package dev.dubhe.skyland.mixin;

import com.mojang.serialization.MapCodec;
import dev.dubhe.skyland.SkyLandChunkGenerator;
import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerators.class)
public class ChunkGeneratorsMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void register(
        Registry<MapCodec<? extends ChunkGenerator>> registry,
        CallbackInfoReturnable<MapCodec<? extends ChunkGenerator>> cir
    ) {
        Registry.register(registry, SkyLandMod.ID, SkyLandChunkGenerator.CODEC);
    }
}
