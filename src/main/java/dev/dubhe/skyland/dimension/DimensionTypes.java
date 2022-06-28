package dev.dubhe.skyland.dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class DimensionTypes {
    public static final RegistryKey<DimensionType> OVER_NETHER = of("over_nether");

    public DimensionTypes() {
    }

    private static RegistryKey<DimensionType> of(String id) {
        return RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier(id));
    }
}
