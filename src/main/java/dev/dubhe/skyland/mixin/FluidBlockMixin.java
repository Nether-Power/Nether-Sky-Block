package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class FluidBlockMixin {
    @Shadow
    @Final
    protected FlowingFluid fluid;

    @Inject(method = "shouldSpreadLiquid", at = @At("HEAD"))
    private void cauldronObsidian(
        Level level, BlockPos pos, BlockState state,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!this.fluid.is(FluidTags.LAVA)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.getGameRules().get(SkyLandGamerules.WATER_CAULDRON)) return;

        for (Direction direction : LiquidBlock.POSSIBLE_FLOW_DIRECTIONS) {
            BlockPos blockPos = pos.relative(direction.getOpposite());
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(Blocks.WATER_CAULDRON)
                && ((LayeredCauldronBlock) blockState.getBlock()).isFull(blockState)) {
                if (level.getFluidState(pos).isSource()) {
                    level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                    level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                    level.levelEvent(1501, pos, 0);
                }
            }
        }
    }
}
