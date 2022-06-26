package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLand;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @Shadow
    public static void spawnEntitiesInChunk(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner) {}

    @Inject(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void spawnEntities(SpawnGroup group, ServerWorld world, WorldChunk chunk, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {
        if (world.getGameRules().getBoolean(SkyLand.LC)) {
            for (int i = chunk.getBottomY(); i < chunk.getTopY(); i += 16) {
                ChunkSection chunkSection = chunk.getSectionArray()[chunk.getSectionIndex(i)];
                if (chunkSection != null && !chunkSection.isEmpty()) {
                    BlockPos blockPos = getRandomPosInChunk(world, chunk).add(0, i, 0);
                    spawnEntitiesInChunk(group, world, chunk, blockPos, checker, runner);
                }
            }
            ci.cancel();
        }
    }

    private static BlockPos getRandomPosInChunk(World world, WorldChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int x = chunkPos.getStartX() + world.random.nextInt(16);
        int z = chunkPos.getStartZ() + world.random.nextInt(16);
        int y = world.random.nextInt(16) + 1;
        return new BlockPos(x, y, z);
    }
}
