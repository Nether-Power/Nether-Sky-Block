package dev.dubhe.skyland.mixin;

import dev.dubhe.skyland.SkyLandGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatrolSpawner.class)
public abstract class PatrolSpawnerMixin {
    @Shadow
    protected abstract boolean spawnPatrolMember(ServerLevel level, BlockPos pos, RandomSource random, boolean isLeader);

    @Inject(method = "tick", at = @At("TAIL"))
    private void netherPatrol(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        if (!spawnEnemies) return;
        if (!level.getGameRules().get(SkyLandGamerules.NETHER_PATROL)) return;

        RandomSource random = level.getRandom();
        if (level.getPlayers(_ -> true).isEmpty()) return;
        Player player = level.getPlayers(_ -> true).get(random.nextInt(level.getPlayers(_ -> true).size()));

        int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos.MutableBlockPos mutable = player.blockPosition().mutable().move(j, 0, k);

        if (level.getBiome(mutable).is(BiomeTags.IS_NETHER)) {
            int groupSize = (int) Math.ceil(level.getCurrentDifficultyAt(mutable).getEffectiveDifficulty()) + 1;
            for (int p = 0; p < groupSize; ++p) {
                mutable.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                if (p == 0) {
                    if (!this.spawnPatrolMember(level, mutable, random, true)) break;
                } else {
                    this.spawnPatrolMember(level, mutable, random, false);
                }
                mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
            }
        }
    }
}
