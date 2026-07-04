package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
    @Shadow
    public abstract BlockState getBlockState();

    public FallingBlockEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/FallingBlock;isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            shift = At.Shift.AFTER
        )
    )
    private void onTick(CallbackInfo ci) {
        Level level = this.level();
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
        if (serverLevel.getGameRules().get(SkyLandGamerules.ANVIL_HANDLE)
            && getBlockState().is(BlockTags.ANVIL)) {
            BlockPos downPos = BlockPos.containing(this.getX(), this.getY() - 0.06, this.getZ());
            BlockPos downDownPos = BlockPos.containing(this.getX(), this.getY() - 1.12, this.getZ());
            BlockState downState = level.getBlockState(downPos);
            BlockState downDownState = level.getBlockState(downDownPos);
            if (downState.is(Blocks.MOSS_BLOCK) && downDownState.is(Blocks.DIRT)) {
                level.setBlockAndUpdate(
                    BlockPos.containing(this.getX(), this.getY() - 1.06, this.getZ()),
                    Blocks.GRASS_BLOCK.defaultBlockState()
                );
                level.setBlockAndUpdate(downPos, Blocks.AIR.defaultBlockState());
            }
        }
    }
}
