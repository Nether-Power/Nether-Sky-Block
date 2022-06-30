package dev.dubhe.skyland;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;

public class SkyLandGamerules {
    public static void registry() {

    }
    public static final Key<BooleanRule> LC = GameRuleRegistry.register("qnmdLC", Category.MOBS,
            GameRuleFactory.createBooleanRule(false));
    public static final Key<BooleanRule> CHIEFTAIN = GameRuleRegistry.register("chieftainMode", Category.MISC,
            GameRuleFactory.createBooleanRule(false));
    public static final Key<BooleanRule> WATER_CAULDRON = GameRuleRegistry.register("waterCauldron", Category.MISC,
            GameRuleFactory.createBooleanRule(false));
    public static final Key<BooleanRule> NETHER_TRADER = GameRuleRegistry.register("netherTrader", Category.SPAWNING,
            GameRuleFactory.createBooleanRule(false));
    public static final Key<BooleanRule> ICE_GOLEM = GameRuleRegistry.register("iceGolem", Category.MISC,
            GameRuleFactory.createBooleanRule(false));
    public static final Key<BooleanRule> DOUBLE_CORAL_FANS = GameRuleRegistry.register("doubleCoralFans", Category.MISC,
            GameRuleFactory.createBooleanRule(false));
}
