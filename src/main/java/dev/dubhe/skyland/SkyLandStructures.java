package dev.dubhe.skyland;

import net.minecraft.block.Block;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

import java.util.Objects;
import java.util.Random;

public class SkyLandStructures {
    public record SpawnPlatform(BlockPos worldSpawn) {
        public void generate(ServerWorldAccess world, Random random) {
            Structure structure = Objects.requireNonNull(world.getServer()).getStructureManager()
                    .getStructure(new Identifier(SkyLandMod.MOD_ID, "spawn_platform")).orElseThrow();
            BlockPos structureOrigin = worldSpawn.subtract(new BlockPos(0, 1, 0));
            structure.place(world, structureOrigin, worldSpawn, new StructurePlacementData(), random, Block.NOTIFY_LISTENERS);
        }
    }

    public record TheEndPortal(BlockPos worldSpawn) {
        public void generate(ServerWorldAccess world, Random random, BlockPos fixPos) {
            Structure structure = Objects.requireNonNull(world.getServer()).getStructureManager()
                    .getStructure(new Identifier(SkyLandMod.MOD_ID, "the_end_portal")).orElseThrow();
            BlockPos structureOrigin = worldSpawn.subtract(fixPos);
            structure.place(world, structureOrigin, worldSpawn, new StructurePlacementData(), random, Block.NOTIFY_LISTENERS);
        }
    }
}
