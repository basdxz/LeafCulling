package com.github.basdxz.leafculling.asm.impl;

import com.github.basdxz.leafculling.asm.SmartASMPlugin;
import lombok.*;

import static com.github.basdxz.leafculling.Tags.*;
import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.*;

@MCVersion(MINECRAFT_VERSION)
@Name(ASM_PLUGIN_NAME)
@SortingIndex(1000)
@TransformerExclusions(ASM_PACKAGE)
public final class LeafCullingPlugin implements SmartASMPlugin {
    @Getter
    public final String[] ASMTransformerClass = new String[]{LeafCullingTransformer.class.getName()};
}
