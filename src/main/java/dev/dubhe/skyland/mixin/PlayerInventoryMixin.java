package dev.dubhe.skyland.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Inventory.class)
public class PlayerInventoryMixin {
    @Redirect(
        method = "dropAll",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;")
    )
    private net.minecraft.world.entity.item.ItemEntity dropWithoutScatter(
        net.minecraft.world.entity.player.Player player, ItemStack stack, boolean throwRandomly, boolean retainOwnership
    ) {
        return player.drop(stack, false, false);
    }
}
