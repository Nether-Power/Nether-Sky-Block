package dev.dubhe.skyland.data;

import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

public class SkyLandVillagerTrades {
    public static final ResourceKey<VillagerTrade> NETHER_WART = key("wandering_trader/nether_wart");
    public static final ResourceKey<VillagerTrade> BAMBOO = key("wandering_trader/bamboo");
    public static final ResourceKey<VillagerTrade> SEA_PICKLE = key("wandering_trader/sea_pickle");
    public static final ResourceKey<VillagerTrade> COCOA_BEANS = key("wandering_trader/cocoa_beans");
    public static final ResourceKey<VillagerTrade> SUNFLOWER = key("wandering_trader/sunflower");
    public static final ResourceKey<VillagerTrade> LILAC = key("wandering_trader/lilac");
    public static final ResourceKey<VillagerTrade> ROSE_BUSH = key("wandering_trader/rose_bush");
    public static final ResourceKey<VillagerTrade> PEONY = key("wandering_trader/peony");
    public static final ResourceKey<VillagerTrade> TURTLE_EGG = key("wandering_trader/turtle_egg");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, NETHER_WART, Items.NETHER_WART, 32, 4);
        register(context, BAMBOO, Items.BAMBOO, 16, 8);
        register(context, SEA_PICKLE, Items.SEA_PICKLE, 16, 8);
        register(context, COCOA_BEANS, Items.COCOA_BEANS, 16, 8);
        register(context, SUNFLOWER, Items.SUNFLOWER, 8, 8);
        register(context, LILAC, Items.LILAC, 8, 8);
        register(context, ROSE_BUSH, Items.ROSE_BUSH, 8, 8);
        register(context, PEONY, Items.PEONY, 8, 8);
        register(context, TURTLE_EGG, Items.TURTLE_EGG, 16, 4);
    }

    private static void register(
        BootstrapContext<VillagerTrade> context,
        ResourceKey<VillagerTrade> key,
        net.minecraft.world.level.ItemLike result,
        int price,
        int maxUses
    ) {
        context.register(
            key,
            new VillagerTrade(
                new TradeCost(Items.EMERALD, price),
                new ItemStackTemplate(result.asItem()),
                maxUses,
                1,
                0.05F,
                Optional.empty(),
                List.of()
            )
        );
    }

    private static ResourceKey<VillagerTrade> key(String path) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, SkyLandMod.of(path));
    }
}
