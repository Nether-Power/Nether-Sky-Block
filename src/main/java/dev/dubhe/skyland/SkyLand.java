package dev.dubhe.skyland;

import net.fabricmc.api.ModInitializer;
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

    @Override
    public void onInitialize() {
    }
}
