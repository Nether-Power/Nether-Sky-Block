package dev.dubhe.skyland.plugin;

import dev.dubhe.skyland.ComposterCoolDown;
import dev.dubhe.skyland.SkyLandMod;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ComposterCoolDownProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlock() == Blocks.COMPOSTER) {
            if (ComposterCoolDown.cool_down > 0) {
                iTooltip.add(Text.translatable("skyland.tooltip.cool_down", ComposterCoolDown.cool_down));
            } else {
                iTooltip.add(Text.translatable("skyland.tooltip.ready"));
            }
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(SkyLandMod.MOD_ID, "composter_cool_down");
    }
}
