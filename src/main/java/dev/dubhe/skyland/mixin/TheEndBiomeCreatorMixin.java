package dev.dubhe.skyland.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings.Builder;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.TheEndBiomeCreator;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheEndBiomeCreator.class)
public class TheEndBiomeCreatorMixin {

    @Inject(method = "createEndBiome", at = @At("HEAD"), cancellable = true)
    private static void createEndBiome(Builder builder, CallbackInfoReturnable<Biome> cir) {
        SpawnSettings.Builder builder2 = new SpawnSettings.Builder();
        builder2.spawn(
                SpawnGroup.WATER_CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.SQUID, 2, 1, 4)
        );
        DefaultBiomeFeatures.addEndMobs(builder2);
        Biome biome = new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE)
                .temperature(0.5f)
                .downfall(0.5f)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(4159204)
                                .waterFogColor(329011)
                                .fogColor(0xA080A0)
                                .skyColor(0)
                                .moodSound(BiomeMoodSound.CAVE)
                                .build()
                )
                .spawnSettings(builder2.build())
                .generationSettings(builder.build()).build();
        cir.setReturnValue(biome);
    }
}
