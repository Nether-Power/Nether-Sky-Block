package dev.dubhe.skyland.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    protected final Random random = Random.create();
    @Inject(method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;getZ()D",
                    shift = At.Shift.AFTER
            )
    )
    private void zombieVillager(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        ZombieEntity zombie = (ZombieEntity)(Object)this;
        LivingEntity livingEntity = zombie.getTarget();
        if (livingEntity == null && source.getAttacker() instanceof LivingEntity) {
            livingEntity = (LivingEntity)source.getAttacker();
        }
        World world = zombie.world;
        if (world instanceof ServerWorld serverWorld) {
            int i = MathHelper.floor(zombie.getX());
            int j = MathHelper.floor(zombie.getY());
            int k = MathHelper.floor(zombie.getZ());
            ZombieVillagerEntity zombieVillager = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, serverWorld);
            for (int l = 0; l < 50; ++l) {
                int m = i + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                int n = j + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                int o = k + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                BlockPos blockPos = new BlockPos(m, n, o);
                EntityType<?> entityType = zombieVillager.getType();
                SpawnRestriction.Location location = SpawnRestriction.getLocation(entityType);
                if (!SpawnHelper.canSpawn(location, world, blockPos, entityType) || !SpawnRestriction.canSpawn(entityType, serverWorld, SpawnReason.REINFORCEMENT, blockPos, world.random)) continue;
                zombieVillager.setPosition(m, n, o);
                if (world.isPlayerInRange(m, n, o, 7.0) || !world.doesNotIntersectEntities(zombieVillager) || !world.isSpaceEmpty(zombieVillager) || world.containsFluid(zombieVillager.getBoundingBox())) continue;
                zombieVillager.setTarget(livingEntity);
                zombieVillager.initialize(serverWorld, world.getLocalDifficulty(zombieVillager.getBlockPos()), SpawnReason.REINFORCEMENT, null, null);
                serverWorld.spawnEntityAndPassengers(zombieVillager);
                Objects.requireNonNull(zombie.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)).addPersistentModifier(new EntityAttributeModifier("Zombie reinforcement caller charge", -0.05f, EntityAttributeModifier.Operation.ADDITION));
                Objects.requireNonNull(zombieVillager.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)).addPersistentModifier(new EntityAttributeModifier("Zombie reinforcement callee charge", -0.05f, EntityAttributeModifier.Operation.ADDITION));
                break;
            }
        }
    }
}
