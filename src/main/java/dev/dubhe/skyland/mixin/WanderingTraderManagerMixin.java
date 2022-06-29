package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {

    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private ServerWorldProperties properties;

    @Redirect(method = "trySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int spawnX10(Random random, int value, ServerWorld world) {
        return world.getGameRules().getBoolean(SkyLandGamerules.CHIEFTAIN) ? 0 : random.nextInt(value);
    }

    @Inject(method = "trySpawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WanderingTraderManager;getNearbySpawnPos(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/util/math/BlockPos;",
                    shift = At.Shift.AFTER
            )
    )
    private void spawn(ServerWorld world, CallbackInfoReturnable<Boolean> cir){
        ServerPlayerEntity playerEntity = world.getRandomAlivePlayer();
        if (playerEntity != null) {
            BlockPos blockPos = playerEntity.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
            Optional<BlockPos> optional = pointOfInterestStorage.getPosition(registryEntry -> registryEntry.matchesKey(
                    PointOfInterestTypes.MEETING), pos -> true, blockPos, 48, PointOfInterestStorage.OccupationStatus.ANY);
            BlockPos blockPos2 = optional.orElse(blockPos);
            BlockPos blockPos3 = this.getNearbySpawnPos(world, blockPos2, 48);
            if (blockPos3 != null && this.doesNotSuffocateAt(world, blockPos3)) {
                if (world.getBiome(blockPos3).isIn(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                    return;
                }
                WanderingTraderEntity wanderingTraderEntity = EntityType.WANDERING_TRADER.spawn(world, null, null, null, blockPos3, SpawnReason.EVENT, false, false);
                if (wanderingTraderEntity != null) {
                    this.spawnStrider(world, wanderingTraderEntity);
                    properties.setWanderingTraderId(wanderingTraderEntity.getUuid());
                    wanderingTraderEntity.setDespawnDelay(48000);
                    wanderingTraderEntity.setWanderTarget(blockPos2);
                    wanderingTraderEntity.setPositionTarget(blockPos2, 16);
                }
            }
        }
    }

    private void spawnStrider(ServerWorld world, WanderingTraderEntity wanderingTrader) {
        BlockPos blockPos = this.getNearbySpawnPos(world, wanderingTrader.getBlockPos(), 4);
        if (blockPos != null) {
            StriderEntity striderEntity = EntityType.STRIDER.spawn(world, null, null, null, blockPos, SpawnReason.EVENT,
                    false, false);
            if (striderEntity != null) {
                striderEntity.saddle(null);
                striderEntity.startRiding(wanderingTrader,true);
            }
        }
    }

    @Nullable
    private BlockPos getNearbySpawnPos(ServerWorld world, BlockPos pos, int range) {
        BlockPos blockPos = null;

        for (int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(range * 2) - range;
            int k = pos.getZ() + this.random.nextInt(range * 2) - range;
            int l = world.getTopY(Type.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            BlockState blockState = world.getBlockState(blockPos2);
            if (blockState.getFluidState().isOf(Fluids.LAVA) && blockState.getFluidState().isStill()) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }

    @Shadow
    protected abstract boolean doesNotSuffocateAt(BlockView world, BlockPos pos);
}
