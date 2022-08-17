package dev.dubhe.skyland.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterCreatureEntity.class)
public class WaterCreatureEntityMixin {

    @Inject(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At("RETURN"), cancellable = true)
    private static void canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason,
            BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir){
        boolean bl = world.getFluidState(pos).isIn(FluidTags.WATER);
        while (world.getFluidState(pos).isIn(FluidTags.WATER)){
            pos = pos.down();
        }
        boolean bl2 = world.getBlockState(pos).isOf(Blocks.BRAIN_CORAL_BLOCK) ||
                world.getBlockState(pos).isOf(Blocks.BUBBLE_CORAL_BLOCK) ||
                world.getBlockState(pos).isOf(Blocks.FIRE_CORAL_BLOCK) ||
                world.getBlockState(pos).isOf(Blocks.BUBBLE_CORAL_BLOCK) ||
                world.getBlockState(pos).isOf(Blocks.TUBE_CORAL_BLOCK);
        cir.setReturnValue(bl && bl2);
    }
}
