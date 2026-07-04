package dev.dubhe.skyland.jade;

import dev.dubhe.skyland.ComposterCoolDown;
import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ComposterCoolDownProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() == Blocks.COMPOSTER) {
            if (ComposterCoolDown.cool_down > 0) {
                tooltip.add(Component.translatable("skyland.tooltip.cool_down", ComposterCoolDown.cool_down));
            } else {
                tooltip.add(Component.translatable("skyland.tooltip.ready"));
            }
        }
    }

    @Override
    public Identifier getUid() {
        return SkyLandMod.of("composter_cool_down");
    }
}
