package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {

    @Final
    @Shadow
    private ServerWorld world;

    @Shadow
    private BlockPos exitPortalLocation;

    @Shadow
    private boolean previouslyKilled;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setPortal(ServerWorld world, long gatewaysSeed, NbtCompound nbt, CallbackInfo ci) {
        if (this.world.getChunkManager().getChunkGenerator() instanceof SkyLandChunkGenerator && this.exitPortalLocation == null)
            this.exitPortalLocation = new BlockPos(0, 60, 0);
    }

    @Inject(method = "dragonKilled", at = @At(value = "INVOKE_ASSIGN", target="Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateNewEndGateway()V"))
    private void setBlock(EnderDragonEntity dragon, CallbackInfo ci){
        ShulkerEntity shulker = EntityType.SHULKER.create(world);
        BlockPos pos = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.ORIGIN);
        if (shulker != null&&this.previouslyKilled) {
            shulker.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
            shulker.refreshPositionAndAngles(pos, 0.0F, 0.0F);
            world.spawnEntityAndPassengers(shulker);
        }
    }
}
