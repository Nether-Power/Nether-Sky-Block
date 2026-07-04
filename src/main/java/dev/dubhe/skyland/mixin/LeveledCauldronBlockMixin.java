package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public class LeveledCauldronBlockMixin {
    @Inject(method = "handlePrecipitation", at = @At("HEAD"))
    private void handleBasaltDeltasPrecipitation(
        BlockState state, Level level, BlockPos pos,
        Biome.Precipitation precipitation, CallbackInfo ci
    ) {
        if (level.getBiome(pos).is(Biomes.BASALT_DELTAS)
            && state.is(Blocks.POWDER_SNOW_CAULDRON)) {
            BlockState newState = state.cycle(LayeredCauldronBlock.LEVEL);
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }
    }
}
