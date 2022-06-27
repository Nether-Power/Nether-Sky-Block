package dev.dubhe.skyland;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.WorldPreset;

public class SkyLand implements ModInitializer {
    public static final Identifier ID = new Identifier("skyland", "skyland");

    public static final GameRules.Key<GameRules.BooleanRule> LC = GameRules.register("qnmdLC", GameRules.Category.MOBS, GameRules.BooleanRule.create(false));
    public static final GameRules.Key<GameRules.BooleanRule> CHIEFTAIN = GameRules.register("chieftainMode", GameRules.Category.MISC, GameRules.BooleanRule.create(false));

    public static final RegistryKey<WorldPreset> SKYLAND = RegistryKey.of(Registry.WORLD_PRESET_KEY, ID);

    private static final Identifier SOUL_SAND_VALLEY = new Identifier("soul_sand_valley");

    @Override
    public void onInitialize() {

        BiomeModifications.addSpawn(context -> SOUL_SAND_VALLEY.equals(context.getBiomeKey().getValue()), SpawnGroup.MONSTER,
                EntityType.WITCH, 1, 1, 1);

    }
}
