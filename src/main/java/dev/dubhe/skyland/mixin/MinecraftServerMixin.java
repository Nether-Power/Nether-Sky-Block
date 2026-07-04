package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import dev.dubhe.skyland.SkyLandStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "setInitialSpawn", at = @At("HEAD"), cancellable = true)
    private static void generateSpawnPlatform(
        ServerLevel level,
        ServerLevelData levelData,
        boolean generateBonusChest,
        boolean isDebug,
        LevelLoadListener levelLoadListener,
        CallbackInfo ci
    ) {
        ServerChunkCache chunkSource = level.getChunkSource();
        ChunkGenerator generator = chunkSource.getGenerator();
        if (!(generator instanceof SkyLandChunkGenerator)) {
            return;
        }

        int spawnHeight = generator.getSpawnHeight(level);
        BlockPos worldSpawn = new BlockPos(0, spawnHeight, 0);
        levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), worldSpawn, 0.0f, 0.0f));

        new SkyLandStructures.SpawnPlatform(worldSpawn).generate(level, level.getRandom());

        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(1024, 0, 0));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(-1024, 0, 0));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(0, 0, 1024));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(0, 0, -1024));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(724, 0, 724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(724, 0, -724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(-724, 0, 724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(level, level.getRandom(), new BlockPos(-724, 0, -724));

        ci.cancel();
    }
}
