package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLand;
import dev.dubhe.skyland.SkyLandChunkGenerator;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(WorldPresets.Registrar.class)
public class WorldPresetsRegistrarMixin {
    @Final @Shadow private Registry<WorldPreset> worldPresetRegistry;
    @Final @Shadow private Registry<Biome> biomeRegistry;
    @Final @Shadow private Registry<StructureSet> structureSetRegistry;
    @Final @Shadow private Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;
    @Final @Shadow private Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersRegistry;
    @Final @Shadow private RegistryEntry<DimensionType> overworldDimensionType;
    @Final @Shadow private RegistryEntry<DimensionType> theNetherDimensionType;
    @Final @Shadow private RegistryEntry<DimensionType> theEndDimensionType;

    @Inject(method = "initAndGetDefault", at = @At("RETURN"))
    private void register(CallbackInfoReturnable<RegistryEntry<WorldPreset>> cir) {
        DimensionOptions overworld = this.createSkyDimensionOptions(this.overworldDimensionType, MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.OVERWORLD);
        DimensionOptions nether = this.createSkyDimensionOptions(this.theNetherDimensionType, MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.NETHER);
        DimensionOptions end = this.createSkyDimensionOptions(this.theEndDimensionType, new TheEndBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.END);
        BuiltinRegistries.add(this.worldPresetRegistry, SkyLand.SKYLAND, new WorldPreset(Map.of(DimensionOptions.OVERWORLD, overworld, DimensionOptions.NETHER, nether, DimensionOptions.END, end)));
    }

    private DimensionOptions createSkyDimensionOptions(RegistryEntry<DimensionType> type, BiomeSource biomes, RegistryKey<ChunkGeneratorSettings> key) {
        return new DimensionOptions(type, new SkyLandChunkGenerator(this.structureSetRegistry, this.noiseParametersRegistry, biomes, this.chunkGeneratorSettingsRegistry.getOrCreateEntry(key)));
    }
}