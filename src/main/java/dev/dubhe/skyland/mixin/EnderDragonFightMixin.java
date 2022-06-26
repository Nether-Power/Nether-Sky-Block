package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandChunkGenerator;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setPortal(ServerWorld world, long gatewaysSeed, NbtCompound nbt, CallbackInfo ci) {
        if (this.world.getChunkManager().getChunkGenerator() instanceof SkyLandChunkGenerator && this.exitPortalLocation == null)
            this.exitPortalLocation = new BlockPos(0, 60, 0);
    }

}
