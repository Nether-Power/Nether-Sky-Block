package dev.dubhe.skyland.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(context.getSide());
        BlockState blockState = world.getBlockState(blockPos);
        boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
        if (bl && useOnGround(context.getStack(), world, blockPos2, blockState, context.getSide())) {
            if (!world.isClient) {
                world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos2, 0);
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }

    private static boolean useOnGround(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, @Nullable Direction facing) {
        BlockState blockState2 = world.getBlockState(blockPos);
        if (facing == Direction.DOWN || !blockState2.getFluidState().isStill()) {
            return false;
        }
        if (facing == Direction.UP){
            if (blockState.getBlock() == Blocks.BUBBLE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.BUBBLE_CORAL.getDefaultState());
            } else if (blockState.getBlock() == Blocks.BRAIN_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.BRAIN_CORAL.getDefaultState());
            } else if (blockState.getBlock() == Blocks.FIRE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.FIRE_CORAL.getDefaultState());
            } else if (blockState.getBlock() == Blocks.HORN_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.HORN_CORAL.getDefaultState());
            } else if (blockState.getBlock() == Blocks.TUBE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.TUBE_CORAL.getDefaultState());
            }
        } else {
            if (blockState.getBlock() == Blocks.BUBBLE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.BUBBLE_CORAL_WALL_FAN.getDefaultState().with(CoralWallFanBlock.FACING, facing));
            } else if (blockState.getBlock() == Blocks.BRAIN_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.BRAIN_CORAL_WALL_FAN.getDefaultState().with(CoralWallFanBlock.FACING, facing));
            } else if (blockState.getBlock() == Blocks.FIRE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.FIRE_CORAL_WALL_FAN.getDefaultState().with(CoralWallFanBlock.FACING, facing));
            } else if (blockState.getBlock() == Blocks.HORN_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.HORN_CORAL_WALL_FAN.getDefaultState().with(CoralWallFanBlock.FACING, facing));
            } else if (blockState.getBlock() == Blocks.TUBE_CORAL_BLOCK){
                world.setBlockState(blockPos, Blocks.TUBE_CORAL_WALL_FAN.getDefaultState().with(CoralWallFanBlock.FACING, facing));
            }
        }
        stack.decrement(1);
        return true;
    }
}
