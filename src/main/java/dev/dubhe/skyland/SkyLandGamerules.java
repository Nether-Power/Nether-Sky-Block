package dev.dubhe.skyland;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class SkyLandGamerules {
    public static void register() {
    }

    public static final GameRule<Boolean> LC = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MOBS)
        .buildAndRegister(SkyLandMod.of("qnmd_lc"));
    public static final GameRule<Boolean> CHIEFTAIN = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MISC)
        .buildAndRegister(SkyLandMod.of("chieftain_mode"));
    public static final GameRule<Boolean> WATER_CAULDRON = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MISC)
        .buildAndRegister(SkyLandMod.of("water_cauldron"));
    public static final GameRule<Boolean> NETHER_TRADER = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.SPAWNING)
        .buildAndRegister(SkyLandMod.of("nether_trader"));
    public static final GameRule<Boolean> ICE_GOLEM = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MISC)
        .buildAndRegister(SkyLandMod.of("ice_golem"));
    public static final GameRule<Boolean> ANVIL_HANDLE = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MISC)
        .buildAndRegister(SkyLandMod.of("anvil_handle"));
    public static final GameRule<Boolean> NETHER_PATROL = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.SPAWNING)
        .buildAndRegister(SkyLandMod.of("nether_patrol"));
    public static final GameRule<Boolean> VILLAGER_REINFORCEMENTS = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.SPAWNING)
        .buildAndRegister(SkyLandMod.of("villager_reinforcements"));
    public static final GameRule<Boolean> KILL_DRAGON_SPAWN_SHULKER = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MOBS)
        .buildAndRegister(SkyLandMod.of("kill_dragon_spawn_shulker"));
    public static final GameRule<Boolean> KILL_DRAGON_DROP_ELYTRA = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.DROPS)
        .buildAndRegister(SkyLandMod.of("kill_dragon_drop_elytra"));
    public static final GameRule<Boolean> COMPOSTER_BONE_MEAL = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.MISC)
        .buildAndRegister(SkyLandMod.of("composter_bone_meal"));
    public static final GameRule<Boolean> MEMORY_FOOD_LEVEL = GameRuleBuilder.forBoolean(false)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(SkyLandMod.of("memory_food_level"));
    public static final GameRule<Integer> RESPAWN_MIN_FOOD_LEVEL = GameRuleBuilder.forInteger(4)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(SkyLandMod.of("respawn_min_food_level"));
}
