package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTraderManager.class)
//TODO 使下界群系可以生成骑着赤足兽的游商（玩家40格半径最高点为熔岩时，每20min有2.5%概率生成，当第一次生成失败时后续生成概率提高至7.5%）
//TODO 游商额外出售物品
public class WanderingTraderManagerMixin {
    @Redirect(method = "trySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int spawnX10(Random random, int value, ServerWorld world) {
        return world.getGameRules().getBoolean(SkyLandGamerules.CHIEFTAIN) ? 0 : random.nextInt(value);
    }
}
