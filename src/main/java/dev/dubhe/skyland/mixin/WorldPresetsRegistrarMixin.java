package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(targets = "net.minecraft.world.level.levelgen.presets.WorldPresets$Bootstrap")
public class WorldPresetsRegistrarMixin {
    @Unique
    private static final ResourceKey<WorldPreset> SKYLAND = ResourceKey.create(
        Registries.WORLD_PRESET, SkyLandMod.ID);

    @Shadow
    @Final
    private BootstrapContext<WorldPreset> context;
    @Shadow
    @Final
    private HolderGetter<NoiseGeneratorSettings> noiseSettings;
    @Shadow
    @Final
    private HolderGetter<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private void registerSkyLand(CallbackInfo ci) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        Holder<DimensionType> netherDimensionType = dimensionTypes.getOrThrow(BuiltinDimensionTypes.NETHER);
        Holder<NoiseGeneratorSettings> netherNoiseSettings = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);
        Holder<NoiseGeneratorSettings> endNoiseSettings = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.END);
        Holder.Reference<MultiNoiseBiomeSourceParameterList> netherBiomePreset =
            this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);

        LevelStem overworld = new LevelStem(
            netherDimensionType,
            new SkyLandChunkGenerator(MultiNoiseBiomeSource.createFromPreset(netherBiomePreset), netherNoiseSettings)
        );
        LevelStem nether = new LevelStem(
            netherDimensionType,
            new SkyLandChunkGenerator(MultiNoiseBiomeSource.createFromPreset(netherBiomePreset), netherNoiseSettings)
        );
        Holder<DimensionType> endDimensionType = dimensionTypes.getOrThrow(BuiltinDimensionTypes.END);
        LevelStem end = new LevelStem(
            endDimensionType,
            new SkyLandChunkGenerator(TheEndBiomeSource.create(biomes), endNoiseSettings)
        );
        context.register(
            SKYLAND, new WorldPreset(
                Map.of(LevelStem.OVERWORLD, overworld, LevelStem.NETHER, nether, LevelStem.END, end)
            )
        );
    }
}
