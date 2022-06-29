package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @Inject(
            method = "receiveNeighborFluids",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void cauldronObsidian(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (world.getGameRules().getBoolean(SkyLandGamerules.WATER_CAULDRON)) {
            for (Direction direction : FluidBlock.FLOW_DIRECTIONS) {
                BlockPos blockPos = pos.offset(direction.getOpposite());
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(Blocks.WATER_CAULDRON) && ((LeveledCauldronBlock) blockState.getBlock()).isFull(
                        blockState)) {
                    if (world.getFluidState(pos).isStill()) {
                        world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                        world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                        world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
                    }
                }
            }
        }
    }
}
