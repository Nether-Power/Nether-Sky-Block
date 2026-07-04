package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderManagerMixin {
    @Shadow
    @Final
    private RandomSource random;

    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int chieftainMode(RandomSource random, int bound, ServerLevel level) {
        return level.getGameRules().get(SkyLandGamerules.CHIEFTAIN) ? 0 : random.nextInt(bound);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void spawnNetherTrader(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        if (!level.getGameRules().get(SkyLandGamerules.NETHER_TRADER)) return;
        if (!level.getGameRules().get(SkyLandGamerules.CHIEFTAIN) && random.nextFloat() >= 0.1f) return;

        ServerPlayer player = level.getRandomPlayer();
        if (player == null) return;

        BlockPos blockPos = player.blockPosition();
        BlockPos spawnPos = getNearbyLavaSpawnPos(level, blockPos);
        if (spawnPos == null) return;

        if (!level.getBiome(spawnPos).is(BiomeTags.IS_NETHER)) return;

        WanderingTrader trader = EntityType.WANDERING_TRADER.create(level, EntitySpawnReason.EVENT);
        if (trader == null) return;

        trader.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        trader.setDespawnDelay(48000);

        Strider strider = EntityType.STRIDER.create(level, EntitySpawnReason.EVENT);
        if (strider != null) {
            strider.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            strider.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(Items.SADDLE));
            level.addFreshEntityWithPassengers(strider);
            trader.startRiding(strider, true, true);
        }

        level.addFreshEntityWithPassengers(trader);
    }

    @Unique
    @Nullable
    private BlockPos getNearbyLavaSpawnPos(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(96) - 48;
            int k = pos.getZ() + this.random.nextInt(96) - 48;
            int l = level.getHeight(Types.WORLD_SURFACE, j, k);
            BlockPos candidate = new BlockPos(j, l, k);
            BlockPos below = new BlockPos(j, l - 1, k);
            BlockState belowState = level.getBlockState(below);
            if (belowState.getFluidState().is(Fluids.LAVA) && belowState.getFluidState().isSource()) {
                return candidate;
            }
        }
        return null;
    }
}
