package dev.dubhe.skyland.mixin;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.data.server.BiomeTagProvider;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeTagProvider.class)
public abstract class BiomeTagProviderMixin extends AbstractTagProvider<Biome> {

    private BiomeTagProviderMixin(DataGenerator root, Registry<Biome> registry) {
        super(root, registry);
    }

    @Inject(method = "configure",
            at = @At(
                    value = "HEAD"
            )
    )
    private void spawnOutpost(CallbackInfo ci) {
        this.getOrCreateTagBuilder(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE)
                .add(BiomeKeys.NETHER_WASTES)
                .add(BiomeKeys.BASALT_DELTAS)
                .add(BiomeKeys.WARPED_FOREST)
                .add(BiomeKeys.CRIMSON_FOREST)
                .add(BiomeKeys.SOUL_SAND_VALLEY);
    }
}
