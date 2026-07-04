package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void tickChunk(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        if (self.getRandom().nextInt(64) == 0) {
            BlockPos randomPos = self.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING,
                self.getBlockRandomPos(i, 0, j, 15)
            );
            BlockPos belowPos = randomPos.below();
            if (self.getBiome(randomPos).is(Biomes.BASALT_DELTAS)) {
                BlockState belowState = self.getBlockState(belowPos);
                if (belowState.is(Blocks.CAULDRON) || belowState.is(Blocks.POWDER_SNOW_CAULDRON)) {
                    belowState.getBlock().handlePrecipitation(
                        belowState, self, belowPos,
                        self.getBiome(randomPos).value().getPrecipitationAt(randomPos, self.getSeaLevel())
                    );
                }
            }
        }
    }
}
