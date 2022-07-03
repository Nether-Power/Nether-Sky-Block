package dev.dubhe.skyland;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.WorldPreset;

public class SkyLandMod implements ModInitializer {

    public static final String MOD_ID = "skyland";
    public static final Identifier ID = new Identifier(SkyLandMod.MOD_ID, "skyland");

    public static final RegistryKey<WorldPreset> SKYLAND = RegistryKey.of(Registry.WORLD_PRESET_KEY, ID);

    @Override
    public void onInitialize() {

        SkyLandGamerules.registry();

        SkyLandTrades.mergeWanderingTraderOffers(SkyLandTrades.getSkyLandWanderingTraderOffersTier1());
        SkyLandTrades.mergeWanderingTraderOffers(SkyLandTrades.getSkyLandWanderingTraderOffersTier2());
        SkyLandTrades.removeFarmerTrades();

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (world.getGameRules().getBoolean(SkyLandGamerules.KILL_DRAGON_DROP_ELYTRA)
                    && killedEntity instanceof EnderDragonEntity enderDragonEntity) {
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

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ServerWorld world = newPlayer.getWorld();
            if (world.getGameRules().getBoolean(SkyLandGamerules.MEMORY_FOOD_LEVEL)) {
                int old_food_level = oldPlayer.getHungerManager().getFoodLevel();
                int new_food_level = Math.max(old_food_level, world.getGameRules().getInt(SkyLandGamerules.RESPAWN_MIN_FOOD_LEVEL));
                float old_saturation_level = oldPlayer.getHungerManager().getSaturationLevel();
                newPlayer.getHungerManager().setFoodLevel(new_food_level);
                newPlayer.getHungerManager().setSaturationLevel(old_saturation_level);
            }
        });

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("skyland")
                        .then(CommandManager.literal("gamerule")
                                .then(CommandManager.argument("boolean", BoolArgumentType.bool())
                                        .executes(context -> {
                                            ServerCommandSource serverCommandSource = context.getSource();
                                            boolean bool = BoolArgumentType.getBool(context, "boolean");
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.WATER_CAULDRON)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.ICE_GOLEM)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.NETHER_TRADER)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.ANVIL_HANDLE)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.NETHER_PATROL)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.VILLAGER_REINFORCEMENTS)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.KILL_DRAGON_SPAWN_SHULKER)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(SkyLandGamerules.KILL_DRAGON_DROP_ELYTRA)
                                                    .set(bool, serverCommandSource.getServer());
                                            serverCommandSource.getServer().getGameRules()
                                                    .get(GameRules.DO_INSOMNIA)
                                                    .set(!bool, serverCommandSource.getServer());
                                            serverCommandSource.sendFeedback(
                                                    Text.translatable("skyland.command.gamerule_set_succeed",
                                                            String.valueOf(bool)), true);
                                            return 1;
                                        }))
                        )));

    }
}
