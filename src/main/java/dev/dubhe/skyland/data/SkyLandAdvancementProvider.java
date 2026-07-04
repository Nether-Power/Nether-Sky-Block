package dev.dubhe.skyland.data;

import dev.dubhe.skyland.SkyLandMod;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.FilledBucketTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SkyLandAdvancementProvider extends FabricAdvancementProvider {
    private static final String LANG = "advancement." + SkyLandMod.MOD_ID + ".";
    private static final Identifier BACKGROUND = Identifier.withDefaultNamespace("block/crimson_planks");

    public SkyLandAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<EntityType<?>> entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        AdvancementHolder root = simple(
            "root",
            Items.CRIMSON_FUNGUS,
            AdvancementType.TASK,
            false,
            false,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{})
        ).save(consumer, id("root"));

        AdvancementHolder wood = child(
            "wood",
            Items.CRIMSON_STEM,
            AdvancementType.TASK,
            root,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_STEM)
        ).save(consumer, id("wood"));

        AdvancementHolder composter = child(
            "composter",
            Items.COMPOSTER,
            AdvancementType.TASK,
            wood,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.COMPOSTER)
        ).save(consumer, id("composter"));

        AdvancementHolder killZombifiedPiglin = child(
            "kill_zombified_piglin",
            Items.DIAMOND_SWORD,
            AdvancementType.TASK,
            wood,
            "0",
            KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes, EntityType.ZOMBIFIED_PIGLIN))
        ).save(consumer, id("kill_zombified_piglin"));

        AdvancementHolder killWrong = child(
            "kill_wrong",
            Items.WOODEN_SWORD,
            AdvancementType.TASK,
            killZombifiedPiglin,
            "0",
            KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes, EntityType.ZOMBIE_VILLAGER))
        ).save(consumer, id("kill_wrong"));

        AdvancementHolder goldIngot = child(
            "gold_ingot",
            Items.GOLD_INGOT,
            AdvancementType.TASK,
            killZombifiedPiglin,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT)
        ).save(consumer, id("gold_ingot"));

        AdvancementHolder blastFurnace = child(
            "blast_furnace",
            Items.BLAST_FURNACE,
            AdvancementType.TASK,
            goldIngot,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAST_FURNACE)
        ).save(consumer, id("blast_furnace"));

        AdvancementHolder netherrack = child(
            "netherrack",
            Items.NETHERRACK,
            AdvancementType.TASK,
            blastFurnace,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERRACK)
        ).save(consumer, id("netherrack"));

        AdvancementHolder weakness = child(
            "weakness",
            Items.SPLASH_POTION,
            AdvancementType.TASK,
            composter,
            "0",
            EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.WEAKNESS))
        ).save(consumer, id("weakness"));

        AdvancementHolder saveVillager = child(
            "save_villager",
            Items.GOLDEN_APPLE,
            AdvancementType.GOAL,
            weakness,
            "0",
            CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager()
        ).save(consumer, id("save_villager"));

        AdvancementHolder villageHero = Advancement.Builder.advancement()
            .parent(saveVillager)
            .display(Items.EMERALD, title("village_hero"), desc("village_hero"), BACKGROUND, AdvancementType.CHALLENGE, true, true, false)
            .addCriterion(
                "0",
                EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.BAD_OMEN))
            )
            .addCriterion(
                "1",
                EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.HERO_OF_THE_VILLAGE))
            )
            .save(consumer, id("village_hero"));

        AdvancementHolder breedVillagers = child(
            "breed_villagers",
            Items.BREAD,
            AdvancementType.TASK,
            villageHero,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.BREAD)
        ).save(consumer, id("breed_villagers"));

        AdvancementHolder lavaBucket = child(
            "lava_bucket",
            Items.LAVA_BUCKET,
            AdvancementType.GOAL,
            villageHero,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.LAVA_BUCKET)
        ).save(consumer, id("lava_bucket"));

        AdvancementHolder betterWood = Advancement.Builder.advancement()
            .parent(lavaBucket)
            .display(Items.OAK_WOOD, title("better_wood"), desc("better_wood"), BACKGROUND, AdvancementType.TASK, true, true, false)
            .addCriterion("0", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SPRUCE_SAPLING))
            .addCriterion("1", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ACACIA_SAPLING))
            .addCriterion("2", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BIRCH_SAPLING))
            .addCriterion("3", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DARK_OAK_SAPLING))
            .addCriterion("4", InventoryChangeTrigger.TriggerInstance.hasItems(Items.JUNGLE_SAPLING))
            .addCriterion("5", InventoryChangeTrigger.TriggerInstance.hasItems(Items.OAK_SAPLING))
            .addCriterion("6", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MANGROVE_PROPAGULE))
            .requirements(AdvancementRequirements.Strategy.OR)
            .save(consumer, id("better_wood"));

        AdvancementHolder ancientDebris = child(
            "ancient_debris",
            Items.ANCIENT_DEBRIS,
            AdvancementType.CHALLENGE,
            villageHero,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.ANCIENT_DEBRIS)
        ).save(consumer, id("ancient_debris"));

        AdvancementHolder bedrockLayer = child(
            "bedrock_layer",
            Items.BEDROCK,
            AdvancementType.GOAL,
            root,
            "0",
            PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setY(MinMaxBounds.Doubles.atMost(1)))
        ).save(consumer, id("bedrock_layer"));

        AdvancementHolder slime = child(
            "slime",
            Items.SLIME_BALL,
            AdvancementType.TASK,
            bedrockLayer,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.SLIME_BALL)
        ).save(consumer, id("slime"));

        AdvancementHolder theEnd = child(
            "the_end",
            Items.END_PORTAL_FRAME,
            AdvancementType.TASK,
            root,
            "0",
            ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)
        ).save(consumer, id("the_end"));

        AdvancementHolder water = child(
            "water",
            Items.WATER_BUCKET,
            AdvancementType.GOAL,
            theEnd,
            "0",
            FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(items, Items.WATER_BUCKET))
        ).save(consumer, id("water"));

        AdvancementHolder ice = child(
            "ice",
            Items.ICE,
            AdvancementType.TASK,
            water,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.ICE)
        ).save(
            consumer,
            id("ice")
        );

        AdvancementHolder coralFan = Advancement.Builder.advancement()
            .parent(lavaBucket)
            .display(Items.FIRE_CORAL_FAN, title("coral_fan"), desc("coral_fan"), BACKGROUND, AdvancementType.GOAL, true, true, false)
            .addCriterion("0", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRAIN_CORAL_BLOCK))
            .addCriterion("1", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TUBE_CORAL_BLOCK))
            .addCriterion("2", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BUBBLE_CORAL_BLOCK))
            .addCriterion("3", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FIRE_CORAL_BLOCK))
            .addCriterion("4", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HORN_CORAL_BLOCK))
            .requirements(AdvancementRequirements.Strategy.OR)
            .save(consumer, id("coral_fan"));

        AdvancementHolder elytra = child(
            "elytra",
            Items.ELYTRA,
            AdvancementType.CHALLENGE,
            theEnd,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.ELYTRA)
        ).save(consumer, id("elytra"));

        AdvancementHolder shulkerBox = child(
            "shulker_box",
            Items.SHULKER_SHELL,
            AdvancementType.CHALLENGE,
            elytra,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.SHULKER_SHELL)
        ).save(consumer, id("shulker_box"));

        AdvancementHolder bulkLava = child(
            "bulk_lava",
            Items.POINTED_DRIPSTONE,
            AdvancementType.GOAL,
            ice,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.POINTED_DRIPSTONE)
        ).save(consumer, id("bulk_lava"));

        AdvancementHolder respawn = child(
            "respawn",
            Items.RESPAWN_ANCHOR,
            AdvancementType.GOAL,
            killZombifiedPiglin,
            "0",
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.RESPAWN_ANCHOR)
        ).save(consumer, id("respawn"));
    }

    private static Component title(String name) {
        return Component.translatable(LANG + name + ".title");
    }

    private static Component desc(String name) {
        return Component.translatable(LANG + name + ".desc");
    }

    private static String id(String name) {
        return SkyLandMod.MOD_ID + ":" + name;
    }

    private static Advancement.Builder simple(
        String name,
        ItemLike icon,
        AdvancementType type,
        boolean toast,
        boolean announce,
        String criterionName,
        Criterion<?> criterion
    ) {
        return Advancement.Builder.advancement()
            .display(icon, title(name), desc(name), BACKGROUND, type, toast, announce, false)
            .addCriterion(criterionName, criterion);
    }

    private static Advancement.Builder child(
        String name,
        ItemLike icon,
        AdvancementType type,
        AdvancementHolder parent,
        String criterionName,
        Criterion<?> criterion
    ) {
        return Advancement.Builder.advancement()
            .parent(parent)
            .display(icon, title(name), desc(name), BACKGROUND, type, true, true, false)
            .addCriterion(criterionName, criterion);
    }
}
