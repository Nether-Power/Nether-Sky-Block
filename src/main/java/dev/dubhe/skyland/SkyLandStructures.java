package dev.dubhe.skyland;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Objects;

public class SkyLandStructures {

    public record SpawnPlatform(BlockPos worldSpawn) {
        public void generate(ServerLevelAccessor level, RandomSource random) {
            StructureTemplate structure = Objects.requireNonNull(level.getServer()).getStructureManager()
                .get(Identifier.fromNamespaceAndPath(SkyLandMod.MOD_ID, "spawn_platform")).orElseThrow();
            BlockPos origin = worldSpawn.subtract(new BlockPos(0, 1, 0));
            structure.placeInWorld(
                level, origin, worldSpawn, new StructurePlaceSettings(), random,
                Block.UPDATE_CLIENTS
            );
        }
    }

    public record TheEndPortal(BlockPos worldSpawn) {
        public void generate(ServerLevelAccessor level, RandomSource random, BlockPos fixPos) {
            StructureTemplate structure = Objects.requireNonNull(level.getServer()).getStructureManager()
                .get(Identifier.fromNamespaceAndPath(SkyLandMod.MOD_ID, "the_end_portal")).orElseThrow();
            BlockPos origin = worldSpawn.subtract(fixPos);
            structure.placeInWorld(
                level, origin, worldSpawn, new StructurePlaceSettings(), random,
                Block.UPDATE_CLIENTS
            );
        }
    }
}
