package dev.dubhe.skyland;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.TradeOffers.SellItemFactory;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyLandTrades {

    public static void mergeWanderingTraderOffers(Int2ObjectMap<Factory[]> custom) {
        TradeOffers.Factory[] customTier1 = custom.get(1);
        TradeOffers.Factory[] customTier2 = custom.get(2);

        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            if (customTier1 != null) {
                factories.addAll(List.of(customTier1));
            }
        });

        TradeOfferHelper.registerWanderingTraderOffers(2, factories -> {
            if (customTier2 != null) {
                factories.addAll(List.of(customTier2));
            }
        });

    }

    private static TradeOffers.Factory sell(Item item, int price, int maxUses) {
        return new TradeOffers.SellItemFactory(item, price, 1, maxUses, 1);
    }

    public static Int2ObjectMap<TradeOffers.Factory[]> getSkyLandWanderingTraderOffers() {
        return new Int2ObjectOpenHashMap<>(ImmutableMap.of(2, new TradeOffers.Factory[]{
                sell(Items.NETHER_WART, 32, 4),
                sell(Items.BAMBOO, 16, 8),
                sell(Items.SEA_PICKLE, 16, 8),
                sell(Items.COCOA_BEANS, 16, 8),
                sell(Items.SUNFLOWER, 8, 8),
                sell(Items.LILAC, 8, 8),
                sell(Items.ROSE_BUSH, 8, 8),
                sell(Items.PEONY, 8, 8),
                sell(Items.TURTLE_EGG, 16, 4),
                sell(Items.TUBE_CORAL_FAN, 64, 4),
                sell(Items.BRAIN_CORAL_FAN, 64, 4),
                sell(Items.BUBBLE_CORAL_FAN, 64, 4),
                sell(Items.FIRE_CORAL_FAN, 64, 4),
                sell(Items.HORN_CORAL_FAN, 64, 4)
        }));
    }

    public static void removeFarmerTrades() {

        System.out.println("Start remove farmer trades");
        Factory[] farm_trades = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.FARMER).get(2);
        Factory[] new_farm_trades = new Factory[2];

        int counter = 0;
        for (Factory factory : farm_trades) {
            if (factory instanceof SellItemFactory sellItemFactory) {
                if (sellItemFactory.sell.getItem() != Items.PUMPKIN_PIE) {
                    new_farm_trades[counter] = factory;
                    counter += 1;
                }
            } else {
                new_farm_trades[counter] = factory;
                counter += 1;
            }
        }
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.FARMER).put(2, new_farm_trades);
    }
}
