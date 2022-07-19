package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
    @Shadow
    public abstract BlockState getBlockState();

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FallingBlock;canFallThrough(Lnet/minecraft/block/BlockState;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(CallbackInfo ci) {
        if (world.getGameRules().getBoolean(SkyLandGamerules.ANVIL_HANDLE) && getBlockState().isIn(
                BlockTags.ANVIL)) {
            BlockState downBlockState = this.world.getBlockState(
                    new BlockPos(this.getX(), this.getY() - 0.06, this.getZ()));
            BlockState downDownBlockState = this.world.getBlockState(
                    new BlockPos(this.getX(), this.getY() - 1.12, this.getZ()));
            // 压合
            if (downBlockState.getBlock() == Blocks.MOSS_BLOCK && downDownBlockState.getBlock() == Blocks.DIRT) {
                world.setBlockState(new BlockPos(this.getX(), this.getY() - 1.06, this.getZ()), Blocks.GRASS_BLOCK.getDefaultState());
                world.setBlockState(new BlockPos(this.getX(), this.getY() - 0.06, this.getZ()), Blocks.AIR.getDefaultState());
            }
        }
    }
}
