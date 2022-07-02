package dev.dubhe.skyland.data;

import dev.dubhe.skyland.SkyLandMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.FilledBucketCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {

    public static String LANG = "advancement." + SkyLandMod.MOD_ID + ".";
    public static final Identifier BACK_GROUND = new Identifier("textures/block/netherrack.png");

    protected AdvancementProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement ROOT = newAdvancement("root", Items.NETHERRACK, TaskType.NORMAL,
                new Criterion("0", InventoryChangedCriterion.Conditions.items(new ItemPredicate[]{})), consumer);
        Advancement NEXT = newAdvancement("next", Items.ACACIA_DOOR, TaskType.CHALLENGE, ROOT, new Criterion("0",
                FilledBucketCriterion.Conditions.create(
                        ItemPredicate.Builder.create().items(Items.WATER_BUCKET).build())), consumer);
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
