package dev.dubhe.skyland.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class SkyLandRecipeProvider extends FabricRecipeProvider {
    public SkyLandRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new SkyLandRecipes(registries, output);
    }

    @Override
    public String getName() {
        return "SkyLand Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }

    private static class SkyLandRecipes extends RecipeProvider {

        protected SkyLandRecipes(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
        }

        @Override
        public void buildRecipes() {
            ResourceKey<Recipe<?>> blastFurnaceKey = ResourceKey.create(
                Registries.RECIPE,
                Identifier.withDefaultNamespace("blast_furnace")
            );
            ResourceKey<Recipe<?>> pointedDripstoneKey = ResourceKey.create(
                Registries.RECIPE,
                Identifier.withDefaultNamespace("pointed_dripstone")
            );

            // Blast furnace: obsidian instead of smooth stone
            shaped(RecipeCategory.DECORATIONS, Items.BLAST_FURNACE).define('I', Items.IRON_INGOT)
                .define('X', Items.FURNACE)
                .define('#', Items.OBSIDIAN)
                .pattern("III")
                .pattern("IXI")
                .pattern("###")
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .save(output, blastFurnaceKey);

            // Pointed dripstone from stonecutting
            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(Items.DRIPSTONE_BLOCK),
                    RecipeCategory.DECORATIONS,
                    Items.POINTED_DRIPSTONE,
                    1
                )
                .unlockedBy("has_dripstone_block", has(Items.DRIPSTONE_BLOCK))
                .save(output, pointedDripstoneKey);

            // Glow ink sac from ink sac + glowstone dust
            shapeless(RecipeCategory.MISC, Items.GLOW_INK_SAC).requires(Items.INK_SAC)
                .requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_ink_sac", has(Items.INK_SAC))
                .save(output, "skyland:glow_ink_sac_4_ink_sac");

            // Netherrack from blasting nether wart block
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(Items.NETHER_WART_BLOCK),
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                Items.NETHERRACK,
                0.1F,
                100,
                BlastingRecipe::new
            ).unlockedBy("has_nether_wart_block", has(Items.NETHER_WART_BLOCK)).save(output, "skyland:netherrack_from_blasting");
        }
    }
}
