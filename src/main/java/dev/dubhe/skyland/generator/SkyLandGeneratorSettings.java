package dev.dubhe.skyland.generator;

import com.mojang.serialization.Lifecycle;
import net.minecraft.structure.StructureSet;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource.Preset;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.OptionalLong;

public class SkyLandGeneratorSettings {
    public static final RegistryKey<DimensionType> OVER_NETHER_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("over_nether"));
    public static final DimensionType OVER_NETHER = DimensionType.create(
            OptionalLong.of(18000L),
            false,
            true,
            true,
            false,
            1.0,
            false,
            true,
            false,
            true,
            true,
            0,
            256,
            128,
            BlockTags.INFINIBURN_NETHER,
            DimensionType.THE_NETHER_ID,
            0.1f
    );
    public static final RegistryKey<ChunkGeneratorSettings> OVER_NETHER_SETTINGS = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, new Identifier("over_nether"));
    public static SimpleRegistry<DimensionOptions> getSkyLandRegistry(DynamicRegistryManager drm, long seed) {
        SimpleRegistry<DimensionOptions> dimensionOptionsRegistry = new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental(), null);

        Registry<DimensionType> dimensionTypeRegistry = drm.get(Registry.DIMENSION_TYPE_KEY);
        Registry<StructureSet> structureSets = drm.get(Registry.STRUCTURE_SET_KEY);
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseParameters = drm.get(Registry.NOISE_WORLDGEN);
        Registry<Biome> biomes = drm.get(Registry.BIOME_KEY);
        Registry<ChunkGeneratorSettings> chunkGeneratorSettings = drm.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);

        dimensionOptionsRegistry.add(DimensionOptions.OVERWORLD, new DimensionOptions(
                dimensionTypeRegistry.getOrCreateEntry(OVER_NETHER_REGISTRY_KEY),
                new SkyLandChunkGenerator(
                        structureSets,
                        noiseParameters,
                        Preset.NETHER.getBiomeSource(biomes, true),
                        seed,
                        chunkGeneratorSettings.getOrCreateEntry(OVER_NETHER_SETTINGS)
                )
        ), Lifecycle.stable());

        dimensionOptionsRegistry.add(DimensionOptions.NETHER, new DimensionOptions(
                dimensionTypeRegistry.getOrCreateEntry(DimensionType.THE_NETHER_REGISTRY_KEY),
                new SkyLandChunkGenerator(
                        structureSets,
                        noiseParameters,
                        Preset.NETHER.getBiomeSource(biomes, true),
                        seed,
                        chunkGeneratorSettings.getOrCreateEntry(ChunkGeneratorSettings.NETHER)
                )
        ), Lifecycle.stable());

        dimensionOptionsRegistry.add(DimensionOptions.END, new DimensionOptions(
                dimensionTypeRegistry.getOrCreateEntry(DimensionType.THE_END_REGISTRY_KEY),
                new SkyLandChunkGenerator(
                        structureSets,
                        noiseParameters,
                        new TheEndBiomeSource(biomes, seed),
                        seed,
                        chunkGeneratorSettings.getOrCreateEntry(ChunkGeneratorSettings.END)
                )
        ), Lifecycle.stable());
        return dimensionOptionsRegistry;

    }
}
