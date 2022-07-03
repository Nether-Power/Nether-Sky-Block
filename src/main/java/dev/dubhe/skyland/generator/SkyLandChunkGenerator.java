package dev.dubhe.skyland.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomSeed;
import net.minecraft.world.gen.random.Xoroshiro128PlusPlusRandom;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkyLandChunkGenerator extends NoiseChunkGenerator {
    private final Registry<DoublePerlinNoiseSampler.NoiseParameters> structuresRegistry;
    private final long seed;

    public static final Codec<SkyLandChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryOps.createRegistryCodec(Registry.STRUCTURE_SET_KEY).forGetter(generator -> generator.field_37053),
            RegistryOps.createRegistryCodec(Registry.NOISE_WORLDGEN).forGetter(generator -> generator.structuresRegistry),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings)
    ).apply(instance, instance.stable(SkyLandChunkGenerator::new)));

    public SkyLandChunkGenerator(Registry<StructureSet> noiseRegistry, Registry<DoublePerlinNoiseSampler.NoiseParameters> structuresRegistry, BiomeSource biomeSource, long seed, RegistryEntry<ChunkGeneratorSettings> settings) {
        super(noiseRegistry, structuresRegistry, biomeSource, seed, settings);
        this.seed = seed;
        this.structuresRegistry = structuresRegistry;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new SkyLandChunkGenerator(this.field_37053, this.structuresRegistry, this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return new VerticalBlockSample(world.getBottomY(), new BlockState[0]);
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos pos = new BlockPos(chunkPos.getStartX(), chunk.getBottomY(), chunkPos.getStartZ());

        structureAccessor.getStructureStarts(
                ChunkSectionPos.from(pos),
                world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).get(new Identifier("minecraft:stronghold"))
        ).forEach(structureStart -> {
            for (StructurePiece piece : structureStart.getChildren()) {
                if (piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                    if (piece.intersectsChunk(chunkPos, 0)) {
                        ChunkRandom random = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
                        random.setCarverSeed(seed, chunkPos.x, chunkPos.z);
                    }
                }
            }
        });
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver generationStep) {
    }

}
