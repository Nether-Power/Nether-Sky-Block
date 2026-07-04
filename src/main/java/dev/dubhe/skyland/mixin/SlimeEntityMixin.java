package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeEntityMixin {
    @Inject(method = "checkSlimeSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void canSpawnInBasaltDeltas(
        EntityType<Slime> type,
        LevelAccessor level,
        EntitySpawnReason spawnReason,
        BlockPos pos,
        RandomSource random,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValue()) return;
        if (level.getDifficulty() != Difficulty.PEACEFUL) {
            if (level.getBiome(pos).is(Biomes.BASALT_DELTAS)) {
                cir.setReturnValue(true);
            }
        }
    }
}
