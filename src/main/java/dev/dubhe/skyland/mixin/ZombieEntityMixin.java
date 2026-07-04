package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Zombie.class)
public class ZombieEntityMixin {
    @Inject(method = "hurtServer", at = @At("TAIL"))
    private void zombieVillagerReinforcements(
        ServerLevel level,
        DamageSource source,
        float damage,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Zombie zombie = (Zombie) (Object) this;
        if (!level.getGameRules().get(SkyLandGamerules.VILLAGER_REINFORCEMENTS)) return;

        LivingEntity target = zombie.getTarget();
        if (target == null && source.getEntity() instanceof LivingEntity attacker) {
            target = attacker;
        }
        if (target == null) return;
        if (level.getDifficulty() != Difficulty.HARD) return;

        RandomSource random = zombie.getRandom();
        double reinforcementChance = zombie.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        if (random.nextFloat() >= reinforcementChance * 2.0) return;
        if (!level.getGameRules().get(GameRules.SPAWN_MOBS)) return;

        ZombieVillager zombieVillager = new ZombieVillager(EntityType.ZOMBIE_VILLAGER, level);
        int i = Mth.floor(zombie.getX());
        int j = Mth.floor(zombie.getY());
        int k = Mth.floor(zombie.getZ());

        for (int l = 0; l < 50; ++l) {
            int m = i + Mth.nextInt(random, 7, 40) * Mth.nextInt(random, -1, 1);
            int n = j + Mth.nextInt(random, 7, 40) * Mth.nextInt(random, -1, 1);
            int o = k + Mth.nextInt(random, 7, 40) * Mth.nextInt(random, -1, 1);
            BlockPos blockPos = new BlockPos(m, n, o);
            if (!NaturalSpawner.isValidEmptySpawnBlock(
                level,
                blockPos,
                level.getBlockState(blockPos),
                level.getFluidState(blockPos),
                EntityType.ZOMBIE_VILLAGER
            )) {
                continue;
            }
            zombieVillager.setPos(m, n, o);
            if (level.hasNearbyAlivePlayer(m, n, o, 7.0) || !level.noCollision(zombieVillager) || level.containsAnyLiquid(
                zombieVillager.getBoundingBox())) {
                continue;
            }
            zombieVillager.setTarget(target);
            zombieVillager.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(zombieVillager.blockPosition()),
                EntitySpawnReason.REINFORCEMENT,
                null
            );
            level.addFreshEntityWithPassengers(zombieVillager);
            Objects.requireNonNull(zombie.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE))
                .addPermanentModifier(new AttributeModifier(
                    SkyLandMod.of("zombie_reinforcement_caller_charge"),
                    -0.05,
                    AttributeModifier.Operation.ADD_VALUE
                ));
            Objects.requireNonNull(zombieVillager.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE))
                .addPermanentModifier(new AttributeModifier(
                    SkyLandMod.of("zombie_reinforcement_callee_charge"),
                    -0.05,
                    AttributeModifier.Operation.ADD_VALUE
                ));
            break;
        }
    }
}
