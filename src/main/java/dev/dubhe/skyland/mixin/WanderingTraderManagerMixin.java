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
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestStorage.OccupationStatus;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Random;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {

    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private ServerWorldProperties properties;
    @Redirect(method = "trySpawn", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int spawnX10(Random random, int value, ServerWorld world) {
        return world.getGameRules().getBoolean(SkyLandGamerules.CHIEFTAIN) ? 0 : random.nextInt(value);
    }
    @Inject(
            method = "trySpawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;getRandomAlivePlayer()Lnet/minecraft/server/network/ServerPlayerEntity;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void spawn(ServerWorld world, CallbackInfoReturnable<Boolean> cir){

        if(world.getGameRules().getBoolean(SkyLandGamerules.NETHER_TRADER)){
            ServerPlayerEntity playerEntity = world.getRandomAlivePlayer();
            if (!world.getGameRules().getBoolean(SkyLandGamerules.CHIEFTAIN) && this.random.nextFloat() >= 0.1) {
                cir.setReturnValue(false);
            }
            if (playerEntity != null) {
                BlockPos blockPos = playerEntity.getBlockPos();
                PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
                Optional<BlockPos> optional = pointOfInterestStorage.getPosition(PointOfInterestType.MEETING.getCompletionCondition(), (pos) -> true, blockPos, 48, OccupationStatus.ANY);
                BlockPos blockPos2 = optional.orElse(blockPos);
                BlockPos blockPos3 = this.getNearbySpawnPos(world, blockPos2);
                if (blockPos3 != null && this.doesNotSuffocateAt(world, blockPos3)) {
                    if (world.getBiome(blockPos3).isIn(BiomeTags.IS_NETHER)) {
                        StriderEntity striderEntity = EntityType.STRIDER.spawn(world, null, null, null, blockPos3, SpawnReason.EVENT,
                                false, false);
                        WanderingTraderEntity wanderingTraderEntity = EntityType.WANDERING_TRADER.spawn(world, null,
                                null,
                                null, blockPos3, SpawnReason.EVENT, false, false);
                        if (wanderingTraderEntity != null) {
                            if (striderEntity != null) {
                                striderEntity.saddle(null);
                                wanderingTraderEntity.startRiding(striderEntity, true);
                            }
                            properties.setWanderingTraderId(wanderingTraderEntity.getUuid());
                            wanderingTraderEntity.setDespawnDelay(48000);
                            wanderingTraderEntity.setWanderTarget(blockPos2);
                            wanderingTraderEntity.setPositionTarget(blockPos2, 16);
                            cir.setReturnValue(true);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private BlockPos getNearbySpawnPos(ServerWorld world, BlockPos pos) {
        BlockPos blockPos = null;

        for (int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(48 * 2) - 48;
            int k = pos.getZ() + this.random.nextInt(48 * 2) - 48;
            int l = world.getTopY(Type.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            BlockPos blockPos3 = new BlockPos(j, l-1, k);
            BlockState blockState = world.getBlockState(blockPos3);
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
