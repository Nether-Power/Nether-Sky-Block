package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow
    private ServerLevel level;
    @Shadow
    private BlockPos exitPortalLocation;
    @Shadow
    private boolean hasPreviouslyKilledDragon;

    @Inject(method = "tryRespawn", at = @At("HEAD"))
    private void setPortalIfMissing(CallbackInfo ci) {
        if (
            this.level.getChunkSource().getGenerator() instanceof SkyLandChunkGenerator
            && this.exitPortalLocation == null
        ) {
            this.exitPortalLocation = new BlockPos(0, 60, 0);
        }
    }

    @Inject(method = "setDragonKilled", at = @At("TAIL"))
    private void spawnShulker(EnderDragon dragon, CallbackInfo ci) {
        if (level.getGameRules().get(SkyLandGamerules.KILL_DRAGON_SPAWN_SHULKER)) {
            if (!hasPreviouslyKilledDragon) {
                return;
            }
            Shulker shulker = EntityType.SHULKER.create(level, EntitySpawnReason.COMMAND);
            if (shulker == null) return;
            BlockPos pos = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.ZERO);
            shulker.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            level.addFreshEntityWithPassengers(shulker);
        }
    }
}
