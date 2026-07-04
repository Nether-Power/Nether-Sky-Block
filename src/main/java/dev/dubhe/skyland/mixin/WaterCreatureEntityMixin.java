package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterAnimal.class)
public class WaterCreatureEntityMixin {
    @Inject(method = "checkSurfaceWaterAnimalSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void canSpawnOnCoral(
        EntityType<? extends WaterAnimal> type,
        LevelAccessor level,
        EntitySpawnReason reason,
        BlockPos pos,
        RandomSource random,
        CallbackInfoReturnable<Boolean> cir
    ) {
        boolean inWater = level.getFluidState(pos).is(FluidTags.WATER);
        BlockPos checkPos = pos;
        while (level.getFluidState(checkPos).is(FluidTags.WATER)) {
            checkPos = checkPos.below();
        }
        boolean onCoral = level.getBlockState(checkPos).is(Blocks.BRAIN_CORAL_BLOCK)
                          || level.getBlockState(checkPos).is(Blocks.BUBBLE_CORAL_BLOCK)
                          || level.getBlockState(checkPos).is(Blocks.FIRE_CORAL_BLOCK)
                          || level.getBlockState(checkPos).is(Blocks.HORN_CORAL_BLOCK)
                          || level.getBlockState(checkPos).is(Blocks.TUBE_CORAL_BLOCK);
        cir.setReturnValue(inWater && onCoral);
    }
}
