package dev.dubhe.skyland.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeveledCauldronBlock.class)
public class LeveledCauldronBlockMixin {

    @Inject(method = "precipitationTick", at = @At("HEAD"))
    public void precipitationTick(BlockState state, World world, BlockPos pos, Precipitation precipitation,
            CallbackInfo ci) {
        if (world.getBiome(pos).matchesKey(BiomeKeys.BASALT_DELTAS)
                && state.getBlock() == Blocks.POWDER_SNOW_CAULDRON) {
            BlockState blockState = state.cycle(LeveledCauldronBlock.LEVEL);
            world.setBlockState(pos, blockState);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }
}
