package dev.dubhe.skyland.mixin;

import static net.minecraft.block.LeveledCauldronBlock.decrementFluidLevel;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo ci){
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        World world = itemEntity.getWorld();
        if(world.getGameRules().getBoolean(SkyLandGamerules.ANVIL_HANDLE)){
            BlockPos blockPos = itemEntity.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            // 洗涤
            if (blockState.getBlock() == Blocks.WATER_CAULDRON) {
                ItemStack itemStack = itemEntity.getStack();
                Item item = itemStack.getItem();
                if (item == Items.BASALT){
                    world.spawnEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(Items.DRIPSTONE_BLOCK, itemStack.getCount())));
                    itemEntity.kill();
                    decrementFluidLevel(blockState, world, blockPos);
                }
            }
        }
    }
}
