package dev.dubhe.skyland.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffers.SellItemFactory.class)
public interface SellItemFactoryAccessor {

    @Accessor
    ItemStack getSell();
}
