package dev.dubhe.skyland.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.concurrent.CompletableFuture;

public class SkyLandVillagerTradeTagProvider extends FabricTagsProvider<VillagerTrade> {
    private static final TagKey<VillagerTrade> WANDERING_TRADER_UNCOMMON = TagKey.create(
        Registries.VILLAGER_TRADE,
        Identifier.withDefaultNamespace("wandering_trader/uncommon")
    );
    private static final TagKey<VillagerTrade> FARMER_LEVEL_2 = TagKey.create(
        Registries.VILLAGER_TRADE,
        Identifier.withDefaultNamespace("farmer/level_2")
    );

    public SkyLandVillagerTradeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.VILLAGER_TRADE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(WANDERING_TRADER_UNCOMMON).addOptional(SkyLandVillagerTrades.NETHER_WART)
            .addOptional(SkyLandVillagerTrades.BAMBOO)
            .addOptional(SkyLandVillagerTrades.SEA_PICKLE)
            .addOptional(SkyLandVillagerTrades.COCOA_BEANS)
            .addOptional(SkyLandVillagerTrades.SUNFLOWER)
            .addOptional(SkyLandVillagerTrades.LILAC)
            .addOptional(SkyLandVillagerTrades.ROSE_BUSH)
            .addOptional(SkyLandVillagerTrades.PEONY)
            .addOptional(SkyLandVillagerTrades.TURTLE_EGG);

        builder(FARMER_LEVEL_2).addOptional(ResourceKey.create(
            Registries.VILLAGER_TRADE,
            Identifier.withDefaultNamespace("farmer/2/pumpkin_emerald")
        )).addOptional(ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.withDefaultNamespace("farmer/2/emerald_apple")));
    }
}
