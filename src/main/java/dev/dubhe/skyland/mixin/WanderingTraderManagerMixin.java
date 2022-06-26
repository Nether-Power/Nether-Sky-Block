package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    @Redirect(method = "trySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int spawnX10(Random random, int value, ServerWorld world) {
        return world.getGameRules().getBoolean(SkyLand.CHIEFTAIN) ? 0 : random.nextInt(value);
    }
}
