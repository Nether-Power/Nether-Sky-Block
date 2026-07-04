package dev.dubhe.skyland.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SkyLandLootTableProvider extends SimpleFabricLootTableSubProvider {
    public SkyLandLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.PIGLIN_BARTER);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        generatePiglinBartering(output);
        generateHeroGifts(output);
    }

    private void generatePiglinBartering(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(
            BuiltInLootTables.PIGLIN_BARTERING,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(5))
                    .add(LootItem.lootTableItem(Items.POTION).setWeight(10))
                    .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(15))
                    .add(LootItem.lootTableItem(Items.STRING).setWeight(20))
                    .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(20))
                    .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40))
                    .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN).setWeight(40))
                    .add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
                    .add(LootItem.lootTableItem(Items.LEATHER).setWeight(40))
                    .add(LootItem.lootTableItem(Items.SOUL_SAND).setWeight(40))
                    .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(40))
                    .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).setWeight(40))
                    .add(LootItem.lootTableItem(Items.GRAVEL).setWeight(30))
                    .add(LootItem.lootTableItem(Items.BLACKSTONE).setWeight(60))
                    .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(40)))
        );
    }

    private void generateHeroGifts(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(
            BuiltInLootTables.FARMER_GIFT,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.BREAD).setWeight(30))
                    .add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(30))
                    .add(LootItem.lootTableItem(Items.COOKIE).setWeight(30))
                    .add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(1)))
        );

        output.accept(
            BuiltInLootTables.LEATHERWORKER_GIFT,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.LEATHER).setWeight(40))
                    .add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(3)))
        );

        output.accept(
            BuiltInLootTables.TOOLSMITH_GIFT,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(40))
                    .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(40))
                    .add(LootItem.lootTableItem(Items.STONE_HOE).setWeight(40))
                    .add(LootItem.lootTableItem(Items.STONE_SHOVEL).setWeight(40))
                    .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(3)))
        );
    }

    @Override
    public String getName() {
        return "SkyLand Loot Tables";
    }
}
