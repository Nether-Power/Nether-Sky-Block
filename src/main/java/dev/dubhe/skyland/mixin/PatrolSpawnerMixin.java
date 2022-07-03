package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(PatrolSpawner.class)
public abstract class PatrolSpawnerMixin {

    @Inject(method = "spawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/registry/RegistryEntry;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void atSpawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
            CallbackInfoReturnable<Integer> cir) {
        if (world.getGameRules().getBoolean(SkyLandGamerules.NETHER_PATROL)) {
            Random random = world.random;
            int i = world.getPlayers().size();
            PlayerEntity playerEntity = world.getPlayers().get(random.nextInt(i));
            int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
            int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
            BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(j, 0, k);
            RegistryEntry<Biome> registryEntry = world.getBiome(mutable);
            if (registryEntry.isIn(BiomeTags.IS_NETHER)) {
                int n = 0;
                int o = (int) Math.ceil(world.getLocalDifficulty(mutable).getLocalDifficulty()) + 1;
                for (int p = 0; p < o; ++p) {
                    ++n;
                    mutable.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                    if (p == 0) {
                        if (!this.spawnPillager(world, mutable, random, true)) {
                            break;
                        }
                    } else {
                        this.spawnPillager(world, mutable, random, false);
                    }
                    mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                    mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                }
                cir.setReturnValue(n);
            }
        }
    }

    @Shadow
    protected abstract boolean spawnPillager(ServerWorld world, BlockPos pos, Random random, boolean captain);
}
