package com.github.basdxz.leafculling.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.IMixinPlugin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.*;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.*;

import static com.github.basdxz.leafculling.Tags.MODNAME;

/**
 * The {@link IMixinConfigPlugin} implementation, delegating most of the logic to FalsePatternLib.
 */
public final class MixinPlugin implements IMixinPlugin {
    /**
     * Logger provided by FalsePatternLib.
     */
    @Getter
    private final Logger logger = IMixinPlugin.createLogger(MODNAME);

    /**
     * Invoked by FalsePattern Lib to decide what targeted mods to check for.
     *
     * @return targeted mods
     */
    @Override
    public ITargetedMod[] getTargetedModEnumValues() {
        return TargetedMod.values();
    }

    /**
     * Invoked by FalsePattern Lib to decide what mixins to load.
     *
     * @return mixins to load
     */
    @Override
    public IMixin[] getMixinEnumValues() {
        return Mixin.values();
    }
}
