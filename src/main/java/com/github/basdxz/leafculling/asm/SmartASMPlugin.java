package com.github.basdxz.leafculling.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public interface SmartASMPlugin extends IFMLLoadingPlugin {
    @Override
    default String getModContainerClass() {
        return null;
    }

    @Override
    default String getSetupClass() {
        return null;
    }

    @Override
    default void injectData(Map<String, Object> data) {
    }

    @Override
    default String getAccessTransformerClass() {
        return null;
    }
}
