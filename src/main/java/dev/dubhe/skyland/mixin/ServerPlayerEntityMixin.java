package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.ComposterCoolDown;
import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Unique
    private boolean skyland$checkedFirstSpawn = false;

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel level = player.level();

        if (!skyland$checkedFirstSpawn) {
            skyland$checkedFirstSpawn = true;
            File ojng = server.getWorldPath(LevelResource.ROOT).resolve("skyland.ojng").toFile();
            if (!ojng.isFile()) {
                player.getInventory().add(new ItemStack(Items.BONE_MEAL, 32));
                try {
                    ojng.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!level.getGameRules().get(SkyLandGamerules.COMPOSTER_BONE_MEAL)) {
            return;
        }

        if (ComposterCoolDown.cool_down == 0) {
            if (player.isShiftKeyDown()) {
                if (!ComposterCoolDown.last_is_sneaking) {
                    ComposterCoolDown.last_is_sneaking = true;
                    BlockPos pos = player.blockPosition();
                    BlockState state = level.getBlockState(pos);
                    if (state.is(Blocks.COMPOSTER)) {
                        int lvl = state.getValue(ComposterBlock.LEVEL);
                        if (lvl < 7) {
                            int newLevel = lvl + 1;
                            BlockState newState = state.setValue(ComposterBlock.LEVEL, newLevel);
                            level.setBlockAndUpdate(pos, newState);
                            if (player.getRandom().nextFloat() < 0.3f) {
                                player.getFoodData().setFoodLevel(
                                    player.getFoodData().getFoodLevel() - 1);
                            }
                            if (newLevel == 7) {
                                level.scheduleTick(pos, newState.getBlock(), 20);
                            }
                            ComposterCoolDown.cool_down = ComposterCoolDown.MAX_COOL_DOWN;
                        }
                    }
                }
            } else {
                ComposterCoolDown.last_is_sneaking = false;
            }
        }
        if (ComposterCoolDown.cool_down > 0) {
            ComposterCoolDown.cool_down--;
        }
    }
}
