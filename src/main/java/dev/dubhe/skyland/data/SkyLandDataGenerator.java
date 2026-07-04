package dev.dubhe.skyland.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class SkyLandDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(SkyLandAdvancementProvider::new);
        pack.addProvider(SkyLandRecipeProvider::new);
        pack.addProvider(SkyLandLootTableProvider::new);
        pack.addProvider(SkyLandVillagerTradeTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.VILLAGER_TRADE, SkyLandVillagerTrades::bootstrap);
    }
}
