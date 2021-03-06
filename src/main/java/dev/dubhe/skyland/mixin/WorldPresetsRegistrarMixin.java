package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandMod;
import dev.dubhe.skyland.SkyLandChunkGenerator;
import net.minecraft.structure.StructureSet;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
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
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.OptionalLong;

@Mixin(targets = "net.minecraft.world.gen.WorldPresets$Registrar")
public class WorldPresetsRegistrarMixin {

    @Final
    @Shadow
    private Registry<WorldPreset> worldPresetRegistry;
    @Final
    @Shadow
    private Registry<Biome> biomeRegistry;
    @Final
    @Shadow
    private Registry<StructureSet> structureSetRegistry;
    @Final
    @Shadow
    private Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;
    @Final
    @Shadow
    private Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersRegistry;
    @Final
    @Shadow
    private RegistryEntry<DimensionType> theNetherDimensionType;
    @Final
    @Shadow
    private Registry<DimensionType> dimensionTypeRegistry;
    @Final
    @Shadow
    private RegistryEntry<DimensionType> theEndDimensionType;

    private static final DimensionType overNether = new DimensionType(OptionalLong.of(18000L), false, true, true, false,
            1.0, false, true, 0, 256, 128, BlockTags.INFINIBURN_NETHER,
            net.minecraft.world.dimension.DimensionTypes.THE_NETHER_ID, 0.1F,
            new DimensionType.MonsterSettings(true, true, ConstantIntProvider.create(11), 15));

    private static final RegistryKey<DimensionType> OVER_NETHER = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,
            new Identifier("over_nether"));

    @Inject(method = "initAndGetDefault", at = @At("RETURN"))
    private void register(CallbackInfoReturnable<RegistryEntry<WorldPreset>> cir) {
        BuiltinRegistries.add(dimensionTypeRegistry, OVER_NETHER, overNether);
        DimensionOptions overworld = this.createSkyDimensionOptions(
                this.dimensionTypeRegistry.getOrCreateEntry(OVER_NETHER),
                MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.NETHER);
        DimensionOptions nether = this.createSkyDimensionOptions(this.theNetherDimensionType,
                MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.NETHER);
        DimensionOptions end = this.createSkyDimensionOptions(this.theEndDimensionType,
                new TheEndBiomeSource(this.biomeRegistry), ChunkGeneratorSettings.END);
        BuiltinRegistries.add(this.worldPresetRegistry, SkyLandMod.SKYLAND, new WorldPreset(
                Map.of(DimensionOptions.OVERWORLD, overworld, DimensionOptions.NETHER, nether, DimensionOptions.END,
                        end)));
    }

    private DimensionOptions createSkyDimensionOptions(RegistryEntry<DimensionType> type, BiomeSource biomes,
            RegistryKey<ChunkGeneratorSettings> key) {
        return new DimensionOptions(type,
                new SkyLandChunkGenerator(this.structureSetRegistry, this.noiseParametersRegistry, biomes,
                        this.chunkGeneratorSettingsRegistry.getOrCreateEntry(key)));
    }
}
