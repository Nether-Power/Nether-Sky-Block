package dev.dubhe.skyland.generator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class SkyLandGeneratorType extends GeneratorType {
    public static final String ID = "skyland";

    public SkyLandGeneratorType(String translationKeySuffix) {
        super(translationKeySuffix);
    }

    @Override
    protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
        return new SkyLandChunkGenerator(
                registryManager.get(Registry.STRUCTURE_SET_KEY),
                registryManager.get(Registry.NOISE_WORLDGEN),
                MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(registryManager.get(Registry.BIOME_KEY), true),
                seed,
                registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY).getOrCreateEntry(ChunkGeneratorSettings.OVERWORLD)
        );
    }

    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager drm, long seed, boolean generateStructures, boolean bonusChest) {
        return new GeneratorOptions(seed, generateStructures, bonusChest, SkyLandGeneratorSettings.getSkyLandRegistry(drm, seed));
    }

    public static void register() {
        VALUES.add(new SkyLandGeneratorType(ID));
    }

}
