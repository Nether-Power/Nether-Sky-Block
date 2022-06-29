package dev.dubhe.skyland;

import net.minecraft.block.Block;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;

import java.util.Objects;

public class SkyLandStructures {
    public record SpawnPlatform(BlockPos worldSpawn) {
        public void generate(ServerWorldAccess world, Random random) {
            StructureTemplate structure = Objects.requireNonNull(world.getServer()).getStructureTemplateManager().getTemplate(new Identifier("skyland", "spawn_platform")).orElseThrow();
            BlockPos structureOrigin = worldSpawn.subtract(new BlockPos(0, 1, 0));
            structure.place(world, structureOrigin, worldSpawn, new StructurePlacementData(), random, Block.NOTIFY_LISTENERS);
        }
    }
}
