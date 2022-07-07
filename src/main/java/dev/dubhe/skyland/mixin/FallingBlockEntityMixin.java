package dev.dubhe.skyland.mixin;

import static net.minecraft.block.LeveledCauldronBlock.decrementFluidLevel;

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

import java.util.ArrayList;
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
            BlockPos blockPos = this.getBlockPos();
            BlockState downBlockState = this.world.getBlockState(
                    new BlockPos(this.getX(), this.getY() - 0.06, this.getZ()));
            BlockState downDownBlockState = this.world.getBlockState(
                    new BlockPos(this.getX(), this.getY() - 1.12, this.getZ()));
            ServerWorld serverWorld = (ServerWorld) world;
            List<Entity> downBlockEntityList = serverWorld.getOtherEntities(this, new Box(blockPos.down()));
            List<Entity> thisBlockEntityList = serverWorld.getOtherEntities(this, new Box(blockPos));
            for (Entity entity : downBlockEntityList) {
                if (entity instanceof ItemEntity itemEntity) {
                    ItemStack itemStack = itemEntity.getStack();
                    Item item = itemStack.getItem();
                    // 膨发
                    if (downBlockState.getBlock() == Blocks.WATER_CAULDRON) {
                        List<Item> coralFan = new ArrayList<>(
                                List.of(new Item[]{Items.FIRE_CORAL_FAN, Items.BUBBLE_CORAL_FAN, Items.BRAIN_CORAL_FAN,
                                        Items.HORN_CORAL_FAN, Items.TUBE_CORAL_FAN}));
                        if (coralFan.contains(item)) {
                            itemStack.setCount(Math.min(itemStack.getCount() * 2, 64));
                            decrementFluidLevel(downBlockState, this.world, blockPos.down());
                        }
                    }
                }
            }
            for (Entity entity : thisBlockEntityList) {
                if (entity instanceof ItemEntity itemEntity) {
                    ItemStack itemStack = itemEntity.getStack();
                    Item item = itemStack.getItem();
                    // 砸碎
                    if (item == Items.DRIPSTONE_BLOCK) {
                        serverWorld.spawnEntityAndPassengers(
                                new ItemEntity(serverWorld, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                                        new ItemStack(Items.POINTED_DRIPSTONE, itemStack.getCount())));
                        itemEntity.kill();
                    }
                }
            }
            // 压合
            if (downBlockState.getBlock() == Blocks.MOSS_BLOCK && downDownBlockState.getBlock() == Blocks.DIRT) {
                world.setBlockState(new BlockPos(this.getX(), this.getY() - 1.06, this.getZ()), Blocks.GRASS_BLOCK.getDefaultState());
                world.setBlockState(new BlockPos(this.getX(), this.getY() - 0.06, this.getZ()), Blocks.AIR.getDefaultState());
            }
        }
    }
}
