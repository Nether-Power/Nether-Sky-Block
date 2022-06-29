package dev.dubhe.skyland;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.WorldPreset;

public class SkyLandMod implements ModInitializer {

    public static final String MOD_ID = "skyland";
    public static final Identifier ID = new Identifier(SkyLandMod.MOD_ID, "skyland");

    public static final RegistryKey<WorldPreset> SKYLAND = RegistryKey.of(Registry.WORLD_PRESET_KEY, ID);

    @Override
    public void onInitialize() {

        SkyLandGamerules.registry();

        SkyLandTrades.mergeWanderingTraderOffers(SkyLandTrades.getSkyLandWanderingTraderOffers());
        SkyLandTrades.removeFarmerTrades();

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (killedEntity instanceof EnderDragonEntity enderDragonEntity) {
                ItemEntity itemEntity = EntityType.ITEM.create(world);
                assert itemEntity != null;

                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putInt("Damage", 426);

                ItemStack itemStack = new ItemStack(Items.ELYTRA, 1);
                itemStack.setNbt(nbtCompound);
                itemEntity.setStack(itemStack);
                Vec3d pos = enderDragonEntity.getPos();
                itemEntity.setPosition(pos);
                world.spawnEntityAndPassengers(itemEntity);
            }
        });

    }
}
