package dev.dubhe.skyland.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        BlockPos blockPos;
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getStartX();
        int j = chunkPos.getStartZ();
        if (((ServerWorld) (Object) this).random.nextInt(64) == 0) {
            blockPos = ((ServerWorld) (Object) this).getTopPosition(Heightmap.Type.MOTION_BLOCKING,
                    ((ServerWorld) (Object) this).getRandomPosInChunk(i, 0, j, 15));
            BlockPos blockPos2 = blockPos.down();
            RegistryEntry<Biome> biome = ((ServerWorld) (Object) this).getBiome(blockPos);
            if (biome.matchesId(BiomeKeys.BASALT_DELTAS.getValue())) {
                BlockState blockState = ((ServerWorld) (Object) this).getBlockState(blockPos2);
                if(blockState.getBlock() == Blocks.CAULDRON || blockState.getBlock() == Blocks.POWDER_SNOW_CAULDRON){
                    Biome.Precipitation precipitation = biome.value().getPrecipitation();
                    blockState.getBlock()
                            .precipitationTick(blockState, ((ServerWorld) (Object) this), blockPos2, precipitation);
                }
            }
        }
    }
}
