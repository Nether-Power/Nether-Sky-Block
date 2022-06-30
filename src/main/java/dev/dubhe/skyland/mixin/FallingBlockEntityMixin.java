package dev.dubhe.skyland.mixin;

import static net.minecraft.block.LeveledCauldronBlock.decrementFluidLevel;

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
        if (getBlockState().isIn(BlockTags.ANVIL)) {
            BlockPos blockPos = this.getBlockPos();
            BlockState blockState1 = this.world.getBlockState(
                    new BlockPos(this.getX(), this.getY() - 0.06, this.getZ()));
            if (blockState1.getBlock() == Blocks.WATER_CAULDRON) {
                ServerWorld world1 = (ServerWorld) world;
                List<Entity> entityList = world1.getOtherEntities(this, new Box(blockPos.down()));
                for (Entity i : entityList) {
                    if (i instanceof ItemEntity itemEntity) {
                        ItemStack itemStack = itemEntity.getStack();
                        List<Item> itemList = new ArrayList<>(
                                List.of(new Item[]{Items.FIRE_CORAL_FAN, Items.BUBBLE_CORAL_FAN, Items.BRAIN_CORAL_FAN,
                                        Items.HORN_CORAL_FAN, Items.TUBE_CORAL_FAN}));
                        Item item = itemStack.getItem();
                        if (itemList.contains(item)) {
                            itemStack.setCount(Math.min(itemStack.getCount() * 2, 64));
                            decrementFluidLevel(blockState1, this.world, blockPos.down());
                        }
                    }
                }
            }
        }
    }
}
