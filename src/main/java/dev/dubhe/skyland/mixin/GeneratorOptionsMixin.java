package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.generator.SkyLandGeneratorSettings;
import dev.dubhe.skyland.generator.SkyLandGeneratorType;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {

    @Inject(method = "fromProperties", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void addSkyLandGeneratorOptions(DynamicRegistryManager registryManager, ServerPropertiesHandler.WorldGenProperties worldGenProperties, CallbackInfoReturnable<GeneratorOptions> cir, long seed, Registry<DimensionType> registry, Registry<Biome> registry2, Registry<StructureSet> registry3, Registry<DimensionOptions> registry4) {
        if (SkyLandGeneratorType.ID.equals(worldGenProperties.levelType())) {
            cir.setReturnValue(new GeneratorOptions(seed, worldGenProperties.generateStructures(), false, SkyLandGeneratorSettings.getSkyLandRegistry(registryManager, seed)));
        }
    }
}
