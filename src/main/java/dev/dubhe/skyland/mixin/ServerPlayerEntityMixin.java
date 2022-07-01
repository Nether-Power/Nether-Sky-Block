package dev.dubhe.skyland.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
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
}
