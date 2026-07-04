package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void useOnCoral(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos relativePos = blockPos.relative(context.getClickedFace());
        BlockState blockState = level.getBlockState(blockPos);
        boolean solid = blockState.isFaceSturdy(level, blockPos, context.getClickedFace());
        if (solid && useOnGround(context.getItemInHand(), level, relativePos, blockState, context.getClickedFace())) {
            if (!level.isClientSide()) {
                level.levelEvent(1505, relativePos, 15);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Unique
    private static boolean useOnGround(ItemStack stack, Level level, BlockPos blockPos, BlockState blockState, @Nullable Direction facing) {
        BlockState stateAt = level.getBlockState(blockPos);
        if (facing == null) return false;
        if (facing == Direction.DOWN || !stateAt.getFluidState().isSource()) {
            return false;
        }
        if (facing == Direction.UP) {
            if (blockState.is(Blocks.BUBBLE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(blockPos, Blocks.BUBBLE_CORAL.defaultBlockState());
            } else if (blockState.is(Blocks.BRAIN_CORAL_BLOCK)) {
                level.setBlockAndUpdate(blockPos, Blocks.BRAIN_CORAL.defaultBlockState());
            } else if (blockState.is(Blocks.FIRE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(blockPos, Blocks.FIRE_CORAL.defaultBlockState());
            } else if (blockState.is(Blocks.HORN_CORAL_BLOCK)) {
                level.setBlockAndUpdate(blockPos, Blocks.HORN_CORAL.defaultBlockState());
            } else if (blockState.is(Blocks.TUBE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(blockPos, Blocks.TUBE_CORAL.defaultBlockState());
            }
        } else {
            if (blockState.is(Blocks.BUBBLE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(
                    blockPos,
                    Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, facing)
                );
            } else if (blockState.is(Blocks.BRAIN_CORAL_BLOCK)) {
                level.setBlockAndUpdate(
                    blockPos,
                    Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, facing)
                );
            } else if (blockState.is(Blocks.FIRE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(
                    blockPos,
                    Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, facing)
                );
            } else if (blockState.is(Blocks.HORN_CORAL_BLOCK)) {
                level.setBlockAndUpdate(
                    blockPos,
                    Blocks.HORN_CORAL_WALL_FAN.defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, facing)
                );
            } else if (blockState.is(Blocks.TUBE_CORAL_BLOCK)) {
                level.setBlockAndUpdate(
                    blockPos,
                    Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, facing)
                );
            }
        }
        stack.shrink(1);
        return true;
    }
}
