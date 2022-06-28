package dev.dubhe.skyland;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;

public class SkyLandGamerules {

    public static void registry() {

    }

    public static final Key<BooleanRule> LC = GameRuleRegistry.register("qnmdLC", Category.MOBS, BooleanRule.create(false));
    public static final Key<BooleanRule> CHIEFTAIN = GameRuleRegistry.register("chieftainMode", Category.MISC, BooleanRule.create(false));


}
