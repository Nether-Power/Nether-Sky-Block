package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import dev.dubhe.skyland.SkyLandStructures;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
        method = "setupSpawn",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(value = "HEAD"),
        cancellable = true)
    private static void generateSpawnPlatform(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        ServerChunkManager chunkManager = world.getChunkManager();
        ChunkGenerator chunkGenerator = chunkManager.getChunkGenerator();
        if (!(chunkGenerator instanceof SkyLandChunkGenerator)) {
            return;
        }

        ChunkPos chunkPos = new ChunkPos(chunkManager.getNoiseConfig().getMultiNoiseSampler().findBestSpawnPosition());
        int spawnHeight = chunkGenerator.getSpawnHeight(world);
        BlockPos worldSpawn = chunkPos.getStartPos().add(8, spawnHeight, 8);
        worldProperties.setSpawnPos(worldSpawn, 0.0f);

        new SkyLandStructures.SpawnPlatform(worldSpawn).generate(world, world.random);

        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(1024, 0, 0));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(-1024, 0, 0));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(0, 0, 1024));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(0, 0, -1024));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(724, 0, 724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(724, 0, -724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(-724, 0, 724));
        new SkyLandStructures.TheEndPortal(worldSpawn).generate(world, world.random, new BlockPos(-724, 0, -724));

        new ItemEntity(world, worldSpawn.getX(), worldSpawn.getY()+1, worldSpawn.getZ(), new ItemStack(Items.BONE_MEAL,32));

        ci.cancel();
    }
}
