package dev.dubhe.skyland.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @Shadow @Final
    protected FlowableFluid fluid;
    @Shadow @Final
    public static ImmutableList<Direction> FLOW_DIRECTIONS;

    @Inject(
            method = "receiveNeighborFluids",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void cauldronObsidian(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir){
        for (Direction direction : FLOW_DIRECTIONS) {
            BlockPos blockPos = pos.offset(direction.getOpposite());
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.WATER_CAULDRON)&&((LeveledCauldronBlock)blockState.getBlock()).isFull(blockState)) {
                if (world.getFluidState(pos).isStill()){
                    world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                    world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
                }
            }
        }
    }
}
