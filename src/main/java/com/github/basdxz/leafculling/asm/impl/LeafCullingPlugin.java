package com.github.basdxz.leafculling.asm.impl;

import com.falsepattern.lib.mapping.MappingManager;
import com.github.basdxz.leafculling.asm.SmartASMPlugin;
import lombok.*;

import static com.github.basdxz.leafculling.Tags.*;
import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.*;

@MCVersion(MINECRAFT_VERSION)
@Name(ASM_PLUGIN_NAME)
@SortingIndex(1000)
@TransformerExclusions(ASM_PACKAGE)
public final class LeafCullingPlugin implements SmartASMPlugin {
    static {
        try {
            MappingManager.classForName(null, null, null);
        } catch (Exception ignored) {
        }
    }

    @Getter
    public final String[] ASMTransformerClass = new String[]{LeafCullingTransformer.class.getName()};
}
