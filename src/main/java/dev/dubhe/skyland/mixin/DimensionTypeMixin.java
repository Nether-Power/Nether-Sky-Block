package dev.dubhe.skyland.mixin;

import com.mojang.serialization.Lifecycle;
import dev.dubhe.skyland.generator.SkyLandGeneratorSettings;
import net.minecraft.util.registry.DynamicRegistryManager.Mutable;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = "addRegistryDefaults", at = @At(value ="HEAD"))
    private static void addRegistry(Mutable registryManager, CallbackInfoReturnable<Mutable> cir){
        MutableRegistry<DimensionType> mutableRegistry = registryManager.getMutable(Registry.DIMENSION_TYPE_KEY);
        mutableRegistry.add(SkyLandGeneratorSettings.OVER_NETHER_REGISTRY_KEY, SkyLandGeneratorSettings.OVER_NETHER, Lifecycle.stable());
    }
}
