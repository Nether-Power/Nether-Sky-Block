package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public class SpawnHelperMixin {
    @Inject(
        method = "spawnCategoryForChunk("
                 + "Lnet/minecraft/world/entity/MobCategory;"
                 + "Lnet/minecraft/server/level/ServerLevel;"
                 + "Lnet/minecraft/world/level/chunk/LevelChunk;"
                 + "Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;"
                 + "Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;"
                 + ")V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void spawnEntities(
        MobCategory category, ServerLevel level, LevelChunk chunk,
        NaturalSpawner.SpawnPredicate predicate, NaturalSpawner.AfterSpawnCallback callback,
        CallbackInfo ci
    ) {
        if (level.getGameRules().get(SkyLandGamerules.LC)) {
            ChunkPos chunkPos = chunk.getPos();
            for (int i = chunk.getMinY(); i < chunk.getMaxY(); i += 16) {
                LevelChunkSection section = chunk.getSections()[chunk.getSectionIndex(i)];
                if (section != null && !section.hasOnlyAir()) {
                    int x = chunkPos.getMinBlockX() + level.getRandom().nextInt(16);
                    int z = chunkPos.getMinBlockZ() + level.getRandom().nextInt(16);
                    int y = level.getRandom().nextInt(16) + 1 + i;
                    BlockPos blockPos = new BlockPos(x, y, z);
                    NaturalSpawner.spawnCategoryForPosition(category, level, chunk, blockPos, predicate, callback);
                }
            }
            ci.cancel();
        }
    }
}
