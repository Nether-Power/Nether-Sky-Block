package dev.dubhe.skyland;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.gamerules.GameRules;

public class SkyLandMod implements ModInitializer {
    public static final String MOD_ID = "skyland";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(SkyLandMod.MOD_ID, "skyland");

    @Override
    public void onInitialize() {
        SkyLandGamerules.register();
        registerBiomeModifications();

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((level, _, killedEntity, _) -> {
            if (level.getGameRules().get(SkyLandGamerules.KILL_DRAGON_DROP_ELYTRA)
                && killedEntity instanceof EnderDragon enderDragon) {
                ItemEntity itemEntity = EntityType.ITEM.create(level, EntitySpawnReason.COMMAND);
                assert itemEntity != null;
                ItemStack itemStack = new ItemStack(Items.ELYTRA, 1);
                itemStack.set(DataComponents.DAMAGE, 1);
                itemEntity.setItem(itemStack);
                itemEntity.setPos(enderDragon.position());
                level.addFreshEntityWithPassengers(itemEntity);
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ServerLevel level = newPlayer.level();
            if (level.getGameRules().get(SkyLandGamerules.MEMORY_FOOD_LEVEL)) {
                int oldFoodLevel = oldPlayer.getFoodData().getFoodLevel();
                int newFoodLevel = Math.max(oldFoodLevel, level.getGameRules().get(SkyLandGamerules.RESPAWN_MIN_FOOD_LEVEL));
                float oldSaturationLevel = oldPlayer.getFoodData().getSaturationLevel();
                newPlayer.getFoodData().setFoodLevel(newFoodLevel);
                newPlayer.getFoodData().setSaturation(oldSaturationLevel);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> dispatcher.register(
            Commands.literal("skyland").then(Commands.literal("gamerule")
                .then(Commands.argument("boolean", BoolArgumentType.bool()).executes(context -> {
                    CommandSourceStack source = context.getSource();
                    boolean bool = BoolArgumentType.getBool(context, "boolean");
                    MinecraftServer server = source.getServer();
                    server.getGameRules().set(SkyLandGamerules.WATER_CAULDRON, bool, server);
                    server.getGameRules().set(SkyLandGamerules.ICE_GOLEM, bool, server);
                    server.getGameRules().set(SkyLandGamerules.NETHER_TRADER, bool, server);
                    server.getGameRules().set(SkyLandGamerules.ANVIL_HANDLE, bool, server);
                    server.getGameRules().set(SkyLandGamerules.NETHER_PATROL, bool, server);
                    server.getGameRules().set(SkyLandGamerules.VILLAGER_REINFORCEMENTS, bool, server);
                    server.getGameRules().set(SkyLandGamerules.KILL_DRAGON_SPAWN_SHULKER, bool, server);
                    server.getGameRules().set(SkyLandGamerules.KILL_DRAGON_DROP_ELYTRA, bool, server);
                    server.getGameRules().set(GameRules.SPAWN_PHANTOMS, !bool, server);
                    source.sendSuccess(
                        () -> Component.translatable("skyland.command.gamerule_set_succeed", String.valueOf(bool)),
                        true
                    );
                    return 1;
                })))
        ));
    }

    private void registerBiomeModifications() {
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.WOLF, 20, 4, 4
        );
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.COW, 20, 2, 4
        );
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.SHEEP, 20, 2, 4
        );
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.LLAMA, 20, 4, 6
        );
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.HORSE, 20, 2, 6
        );
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
            MobCategory.CREATURE, EntityType.DONKEY, 20, 1, 2
        );

        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY),
            MobCategory.MONSTER, EntityType.WITCH, 1, 1, 1
        );

        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.BASALT_DELTAS),
            MobCategory.MONSTER, EntityType.SLIME, 10, 2, 5
        );

        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.THE_END),
            MobCategory.WATER_CREATURE, EntityType.SQUID, 2, 1, 4
        );
    }

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(SkyLandMod.MOD_ID, path);
    }
}
