package dev.dubhe.skyland;

import dev.dubhe.skyland.generator.SkyLandGeneratorType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SkyLandClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SkyLandGeneratorType.register();
    }
}
