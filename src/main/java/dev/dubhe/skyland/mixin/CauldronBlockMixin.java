package dev.dubhe.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {
    @Inject(method = "handlePrecipitation", at = @At("HEAD"))
    private void handleBasaltDeltasPrecipitation(
        BlockState state, Level level, BlockPos pos,
        Biome.Precipitation precipitation, CallbackInfo ci
    ) {
        if (level.getBiome(pos).is(Biomes.BASALT_DELTAS)) {
            level.setBlockAndUpdate(pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }
}
