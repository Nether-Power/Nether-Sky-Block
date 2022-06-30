package dev.dubhe.skyland;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
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

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("skyland")
                        .then(CommandManager.literal("gamerule")
                                .then(CommandManager.argument("boolean", BoolArgumentType.bool())
                                        .executes(context -> {
                                            ServerCommandSource serverCommandSource = context.getSource();
                                            boolean bool = BoolArgumentType.getBool(context, "boolean");
                                            if (bool) {
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.WATER_CAULDRON)
                                                        .set(true, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.ICE_GOLEM)
                                                        .set(true, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.NETHER_TRADER)
                                                        .set(true, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.DOUBLE_CORAL_FANS)
                                                        .set(true, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.NETHER_PATROL)
                                                        .set(true, serverCommandSource.getServer());
                                                serverCommandSource.sendFeedback(
                                                        Text.translatable("skyland.command.gamerule_set_succeed",
                                                                "true"), true);
                                            } else {
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.WATER_CAULDRON)
                                                        .set(false, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.ICE_GOLEM)
                                                        .set(false, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.NETHER_TRADER)
                                                        .set(false, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.DOUBLE_CORAL_FANS)
                                                        .set(false, serverCommandSource.getServer());
                                                serverCommandSource.getServer().getGameRules()
                                                        .get(SkyLandGamerules.NETHER_PATROL)
                                                        .set(false, serverCommandSource.getServer());
                                                serverCommandSource.sendFeedback(
                                                        Text.translatable("skyland.command.gamerule_set_succeed",
                                                                "false"), true);
                                            }
                                            return 1;
                                        }))
                        )));

    }
}
