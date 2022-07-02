package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    
    private static boolean last_is_sneaking = false;
    private static int cool_down = 0;
    private static final int MAX_COOL_DOWN = 40;

    @Shadow @Final public MinecraftServer server;

    @Inject(
            method = "onSpawn",
            at = @At("HEAD")
    )
    private void spawn(CallbackInfo ci){
        File ojng = server.getSavePath(WorldSavePath.ROOT).resolve("skyland.ojng").toFile();
        if (!ojng.isFile()) {
            ((ServerPlayerEntity)(Object)this).giveItemStack(new ItemStack(Items.BONE_MEAL, 32));
            try{
                ojng.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    
    @Inject(
            method = "tick()V",
            at = @At("RETURN")
    )
    private void tick(CallbackInfo ci) {

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld world = player.getWorld();

        if (!world.getGameRules().getBoolean(SkyLandGamerules.COMPOSTER_BONE_MEAL)) {
            return;
        }

        if ((cool_down == 0)) {
            if (player.isSneaking()) {
                if (!last_is_sneaking) {
                    last_is_sneaking = true;


                    BlockPos pos = player.getBlockPos();

                    BlockState state = world.getBlockState(pos);

                    if (state.getBlock() == Blocks.COMPOSTER) {
                        int level = state.get(ComposterBlock.LEVEL);
                        if (level < 7) {
                            int new_level = level + 1;
                            BlockState blockState = state.with(ComposterBlock.LEVEL, new_level);
                            world.setBlockState(pos, blockState);
                            if (player.getRandom().nextFloat() < 0.3) {
                                player.getHungerManager().add(-1, -1);
                            }
                            if (new_level == 7) {
                                world.createAndScheduleBlockTick(pos, blockState.getBlock(), 20);
                            }
                            cool_down = MAX_COOL_DOWN;
                        }
                    }
                }
            } else {
                last_is_sneaking = false;
            }
        }
        if (cool_down > 0) {
            cool_down = cool_down - 1;
        }
    }
}
