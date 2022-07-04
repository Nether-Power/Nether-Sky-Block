package dev.dubhe.skyland.mixin;


import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolemEntity.class)
public class SnowGolemEntityMixin {

    @Inject(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"))
    private void setIce(CallbackInfo ci) {
        World world = ((SnowGolemEntity) (Object) this).world;
        BlockState iceState = Blocks.ICE.getDefaultState();
        if (world.getGameRules().getBoolean(SkyLandGamerules.ICE_GOLEM)) {
            for (int l = 0; l < 4; ++l) {
                int i = MathHelper.floor(
                        ((SnowGolemEntity) (Object) this).getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                int j = MathHelper.floor(((SnowGolemEntity) (Object) this).getY());
                int k = MathHelper.floor(
                        ((SnowGolemEntity) (Object) this).getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos icePos = new BlockPos(i, j - 1, k);
                BlockState state = world.getBlockState(icePos);
                if (state.isOf(Blocks.WATER) && state.getFluidState().isStill() && iceState.canPlaceAt(world, icePos)) {
                    world.setBlockState(icePos, iceState);
                }
            }
        }
    }
}
