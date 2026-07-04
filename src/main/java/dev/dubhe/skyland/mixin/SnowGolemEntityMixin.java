package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public class SnowGolemEntityMixin {
    @Inject(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;",
            ordinal = 0
        )
    )
    private void setIce(CallbackInfo ci) {
        SnowGolem self = (SnowGolem) (Object) this;
        Level level = self.level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.getGameRules().get(SkyLandGamerules.ICE_GOLEM)) return;

        BlockState iceState = Blocks.ICE.defaultBlockState();
        for (int l = 0; l < 4; ++l) {
            int i = Mth.floor(self.getX() + (l % 2 * 2 - 1) * 0.25F);
            int j = Mth.floor(self.getY());
            int k = Mth.floor(self.getZ() + (l / 2.0 % 2 * 2 - 1) * 0.25F);
            BlockPos icePos = new BlockPos(i, j - 1, k);
            BlockState state = level.getBlockState(icePos);
            if (state.is(Blocks.WATER) && state.getFluidState().isSource()
                && iceState.canSurvive(level, icePos)) {
                level.setBlockAndUpdate(icePos, iceState);
            }
        }
    }
}
