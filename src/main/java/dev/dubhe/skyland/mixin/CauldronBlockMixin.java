package dev.dubhe.skyland.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {

    @Inject(method = "precipitationTick", at = @At("HEAD"))
    private void precipitationTick(BlockState state, World world, BlockPos pos, Precipitation precipitation,
            CallbackInfo ci){
        if (world.getBiome(pos).matchesKey(BiomeKeys.BASALT_DELTAS)){
            world.setBlockState(pos, Blocks.POWDER_SNOW_CAULDRON.getDefaultState());
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }
}
