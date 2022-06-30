package dev.dubhe.skyland.mixin;

import net.minecraft.structure.PillagerOutpostGenerator;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.gen.structure.Structures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Structures.class)
public class StructuresMixin {

    @Shadow
    private static Structure.Config createConfig(TagKey<Biome> biomeTag, StructureTerrainAdaptation terrainAdaptation) {
        return null;
    }

    @Inject(method = "register(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/gen/structure/Structure;)Lnet/minecraft/util/registry/RegistryEntry;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void injected(RegistryKey<Structure> key, Structure structure,
            CallbackInfoReturnable<RegistryEntry<Structure>> cir) {
        if (StructureKeys.PILLAGER_OUTPOST == key) {
            cir.setReturnValue(
                    BuiltinRegistries.add(
                            BuiltinRegistries.STRUCTURE, StructureKeys.PILLAGER_OUTPOST,
                            new JigsawStructure(createConfig(
                                    BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE,
                                    StructureTerrainAdaptation.NONE),
                                    PillagerOutpostGenerator.STRUCTURE_POOLS,
                                    7,
                                    ConstantHeightProvider.create(YOffset.fixed(0)),
                                    true
                            )
                    )
            );
        }
    }
}
