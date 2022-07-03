package dev.dubhe.skyland.data;

import dev.dubhe.skyland.SkyLandMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.CuredZombieVillagerCriterion;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.advancement.criterion.FilledBucketCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange.FloatRange;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {

    public static String LANG = "advancement." + SkyLandMod.MOD_ID + ".";
    public static final Identifier BACK_GROUND = new Identifier("textures/block/crimson_planks.png");

    protected AdvancementProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement ROOT = newAdvancement("root", Items.CRIMSON_FUNGUS, TaskType.ROOT,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(new ItemPredicate[]{})), consumer);
        Advancement WOOD = newAdvancement("wood", Items.CRIMSON_STEM, TaskType.MILESTONE, ROOT,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.CRIMSON_STEM)), consumer);
        Advancement COMPOSTER = newAdvancement("composter", Items.COMPOSTER, TaskType.NORMAL, WOOD,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.COMPOSTER)), consumer);
        Advancement KILL_PIGLIN = newAdvancement("piglin", Items.DIAMOND_SWORD, TaskType.NORMAL, WOOD,
                new Criterion("0", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.Builder.create().type(EntityType.PIGLIN))), consumer);
        Advancement KILL_WRONG = newAdvancement("kill_wrong", Items.WOODEN_SWORD, TaskType.NORMAL, KILL_PIGLIN,
                new Criterion("0", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.Builder.create().type(EntityType.ZOMBIE_VILLAGER))), consumer);
        Advancement GOLD_INGOT = newAdvancement("gold_ingot", Items.GOLD_INGOT, TaskType.NORMAL, KILL_PIGLIN,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.GOLD_INGOT)), consumer);
        Advancement BLAST_FURNACE = newAdvancement("blast_furnace", Items.BLAST_FURNACE, TaskType.NORMAL, GOLD_INGOT,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.BLAST_FURNACE)), consumer);
        Advancement NETHERRACK = newAdvancement("netherrack", Items.NETHERRACK, TaskType.NORMAL, BLAST_FURNACE,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.NETHERRACK)), consumer);
        Advancement WEAKNESS = newAdvancement("weakness", Items.SPLASH_POTION, TaskType.NORMAL, COMPOSTER,
                new Criterion("0", EffectsChangedCriterion.Conditions.create(EntityEffectPredicate.create().withEffect(
                        StatusEffects.WEAKNESS))), consumer);
        Advancement SAVE_VILLAGER = newAdvancement("save_villager", Items.GOLDEN_APPLE, TaskType.GOAL, WEAKNESS,
                new Criterion("0", CuredZombieVillagerCriterion.Conditions.any()), consumer);
        Advancement VILLAGE_HERO = newAdvancement("village_hero", Items.EMERALD, TaskType.CHALLENGE, SAVE_VILLAGER,
                new Criterion("0", EffectsChangedCriterion.Conditions.create(EntityEffectPredicate.create()
                        .withEffect(StatusEffects.BAD_OMEN).withEffect(StatusEffects.HERO_OF_THE_VILLAGE))), consumer);
        Advancement BREED_VILLAGERS = newAdvancement("breed_villagers", Items.BREAD, TaskType.NORMAL, VILLAGE_HERO,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.BREAD)), consumer);
        Advancement LAVA_BUCKET = newAdvancement("lava_bucket", Items.LAVA_BUCKET, TaskType.GOAL, VILLAGE_HERO,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.LAVA_BUCKET)), consumer);
        Advancement BETTER_WOOD = Advancement.Builder.create().parent(LAVA_BUCKET)
                .display(Items.OAK_WOOD, getTranslatableTitle("better_wood"), getTranslatableDesc("better_wood"), BACK_GROUND,
                        AdvancementFrame.TASK, true, true, false)
                .criteriaMerger(CriterionMerger.OR)
                .criterion("0", InventoryChangedCriterion.Conditions.items(Items.SPRUCE_SAPLING))
                .criterion("1", InventoryChangedCriterion.Conditions.items(Items.ACACIA_SAPLING))
                .criterion("2", InventoryChangedCriterion.Conditions.items(Items.BIRCH_SAPLING))
                .criterion("3", InventoryChangedCriterion.Conditions.items(Items.DARK_OAK_SAPLING))
                .criterion("4", InventoryChangedCriterion.Conditions.items(Items.JUNGLE_SAPLING))
                .criterion("5", InventoryChangedCriterion.Conditions.items(Items.OAK_SAPLING))
                .criterion("6", InventoryChangedCriterion.Conditions.items(Items.MANGROVE_PROPAGULE))
                .build(consumer, SkyLandMod.MOD_ID + ":better_wood");
        Advancement ANCIENT_DEBRIS = newAdvancement("ancient_debris", Items.ANCIENT_DEBRIS, TaskType.CHALLENGE, VILLAGE_HERO,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.ANCIENT_DEBRIS)), consumer);
        Advancement BEDROCK_LAYER = newAdvancement("bedrock_layer", Items.BEDROCK, TaskType.GOAL, ROOT,
                new Criterion("0", TickCriterion.Conditions.createLocation(LocationPredicate.y(FloatRange.atMost(0)))), consumer);
        Advancement SLIME = newAdvancement("slime", Items.SLIME_BALL, TaskType.NORMAL, BEDROCK_LAYER,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.SLIME_BALL)), consumer);
        Advancement THE_END = newAdvancement("the_end", Items.END_PORTAL_FRAME, TaskType.NORMAL, ROOT,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.ENDER_EYE)), consumer);
        Advancement WATER = newAdvancement("water", Items.WATER_BUCKET, TaskType.GOAL, THE_END,
                new Criterion("0", FilledBucketCriterion.Conditions.create(ItemPredicate.Builder
                        .create().items(Items.WATER_BUCKET).build())), consumer);
        Advancement ICE = newAdvancement("ice", Items.ICE, TaskType.NORMAL, WATER,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.ICE)), consumer);
        Advancement CORAL_FAN = Advancement.Builder.create().parent(LAVA_BUCKET)
                .display(Items.FIRE_CORAL_FAN, getTranslatableTitle("coral_fan"), getTranslatableDesc("coral_fan"),
                        BACK_GROUND, AdvancementFrame.GOAL, true, true, false)
                .criteriaMerger(CriterionMerger.OR)
                .criterion("0", InventoryChangedCriterion.Conditions.items(Items.BRAIN_CORAL_FAN))
                .criterion("1", InventoryChangedCriterion.Conditions.items(Items.TUBE_CORAL_FAN))
                .criterion("2", InventoryChangedCriterion.Conditions.items(Items.BUBBLE_CORAL_FAN))
                .criterion("3", InventoryChangedCriterion.Conditions.items(Items.FIRE_CORAL_FAN))
                .criterion("4", InventoryChangedCriterion.Conditions.items(Items.HORN_CORAL_FAN))
                .build(consumer, SkyLandMod.MOD_ID + ":coral_fan");
        Advancement ELYTRA = newAdvancement("elytra", Items.ELYTRA, TaskType.CHALLENGE, THE_END,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.ELYTRA)), consumer);
        Advancement SHULKER_BOX = newAdvancement("shulker_box", Items.SHULKER_BOX, TaskType.CHALLENGE, ELYTRA,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.ELYTRA)), consumer);
        Advancement BULK_LAVA = newAdvancement("bulk_lava", Items.POINTED_DRIPSTONE, TaskType.GOAL, ICE,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.POINTED_DRIPSTONE)), consumer);
        Advancement RESPAWN = newAdvancement("respawn", Items.RESPAWN_ANCHOR, TaskType.GOAL, KILL_PIGLIN,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(Items.RESPAWN_ANCHOR)), consumer);
    }

    public static Text getTranslatableTitle(String title) {
        return Text.translatable(LANG + title + ".title");
    }

    public static Text getTranslatableDesc(String desc) {
        return Text.translatable(LANG + desc + ".desc");
    }

    public static Advancement newAdvancement(String id, Item icon, TaskType type, Advancement parent,
            Criterion criterion, Consumer<Advancement> consumer) {
        return Advancement.Builder.create().parent(parent)
                .display(icon, getTranslatableTitle(id), getTranslatableDesc(id),
                        BACK_GROUND, type.frame, type.toast, type.announce, type.hide)
                .criterion(criterion.name, criterion.conditions)
                .build(consumer, SkyLandMod.MOD_ID + ":" + id);
    }

    public static Advancement newAdvancement(String id, Item icon, TaskType type, Criterion criterion,
            Consumer<Advancement> consumer) {
        return Advancement.Builder.create()
                .display(icon, getTranslatableTitle(id), getTranslatableDesc(id),
                        BACK_GROUND, type.frame, type.toast, type.announce, type.hide)
                .criterion(criterion.name, criterion.conditions)
                .build(consumer, SkyLandMod.MOD_ID + ":" + id);
    }

    enum TaskType {
        ROOT(AdvancementFrame.TASK, false, false, false),
        NORMAL(AdvancementFrame.TASK, true, false, false),
        MILESTONE(AdvancementFrame.TASK, true, true, false),
        GOAL(AdvancementFrame.GOAL, true, true, false),
        SECRET(AdvancementFrame.GOAL, true, true, true),
        SILENT_GATE(AdvancementFrame.CHALLENGE, false, false, false),
        CHALLENGE(AdvancementFrame.CHALLENGE, true, true, false),

        ;

        private final AdvancementFrame frame;
        private final boolean toast;
        private final boolean announce;
        private final boolean hide;

        TaskType(AdvancementFrame frame, boolean toast, boolean announce, boolean hide) {
            this.frame = frame;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }

    public record Criterion(String name, CriterionConditions conditions) {

    }

}
