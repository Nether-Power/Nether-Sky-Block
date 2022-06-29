package dev.dubhe.skyland.mixin;

import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.TheNetherBiomeCreator;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.NetherPlacedFeatures;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheNetherBiomeCreator.class)
public class TheNetherBiomeCreatorMixin {
    @Inject(method = "createSoulSandValley", at = @At("HEAD"), cancellable = true)
    private static void createSoulSandValley(CallbackInfoReturnable<Biome> cir) {
        SpawnSettings spawnSettings = new SpawnSettings.Builder()
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 20, 5, 5))
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GHAST, 50, 4, 4))
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2))
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 1, 1, 1))
                .spawnCost(EntityType.SKELETON, 0.7, 0.15)
                .spawnCost(EntityType.GHAST, 0.7, 0.15)
                .spawnCost(EntityType.ENDERMAN, 0.7, 0.15)
                .spawnCost(EntityType.STRIDER, 0.7, 0.15)
                .spawnCost(EntityType.WITCH, 0.7, 0.15)
                .build();
        GenerationSettings.Builder builder = new GenerationSettings.Builder()
                .carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_LAVA)
                .feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, NetherPlacedFeatures.BASALT_PILLAR)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_OPEN)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_FIRE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_SOUL_FIRE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE_EXTRA)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_CRIMSON_ROOTS)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_MAGMA)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_CLOSED)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_SOUL_SAND);
        DefaultBiomeFeatures.addNetherMineables(builder);
        cir.setReturnValue(
            new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE)
                .temperature(2.0f).downfall(0.0f)
                .effects(new BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1787717)
                    .skyColor(getSkyColor(2.0f))
                    .particleConfig(new BiomeParticleConfig(ParticleTypes.ASH, 0.00625f))
                    .loopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                    .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0))
                    .additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY))
                    .build())
                .spawnSettings(spawnSettings)
                .generationSettings(builder.build())
                .build()
        );

    }

    @Inject(method = "createBasaltDeltas", at = @At("HEAD"), cancellable = true)
    private static void createBasaltDeltas(CallbackInfoReturnable<Biome> cir) {
        SpawnSettings spawnSettings = new SpawnSettings.Builder()
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GHAST, 40, 1, 1))
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 100, 2, 5))
                .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2))
                .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 100, 2, 5))
                .build();
        GenerationSettings.Builder builder = new GenerationSettings.Builder()
                .carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
                .feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.DELTA)
                .feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.SMALL_BASALT_COLUMNS)
                .feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.LARGE_BASALT_COLUMNS)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.BASALT_BLOBS)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.BLACKSTONE_BLOBS)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_DELTA)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_FIRE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_SOUL_FIRE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE_EXTRA)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, VegetationPlacedFeatures.BROWN_MUSHROOM_NETHER)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, VegetationPlacedFeatures.RED_MUSHROOM_NETHER)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_MAGMA)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_CLOSED_DOUBLE).
                feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_GOLD_DELTAS)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_QUARTZ_DELTAS);
        DefaultBiomeFeatures.addAncientDebris(builder);
        cir.setReturnValue(
            new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE)
                .temperature(2.0f)
                .downfall(0.0f)
                .effects(new BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(6840176)
                    .skyColor(getSkyColor(2.0f))
                    .particleConfig(new BiomeParticleConfig(ParticleTypes.WHITE_ASH, 0.118093334f))
                    .loopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                    .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0))
                    .additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_BASALT_DELTAS)).build())
                .spawnSettings(spawnSettings).generationSettings(builder.build())
                .build()
        );
    }

    private static int getSkyColor(float temperature) {
        temperature = MathHelper.clamp(temperature / 3.0f, -1.0f, 1.0f);
        return MathHelper.hsvToRgb(0.62222224f - temperature * 0.05f, 0.5f + temperature * 0.1f, 1.0f);
    }

}
