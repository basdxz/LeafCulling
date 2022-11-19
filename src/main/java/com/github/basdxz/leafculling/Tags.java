package com.github.basdxz.leafculling;

import lombok.experimental.*;

@UtilityClass
public final class Tags {
    // GRADLETOKEN_* will be replaced by your configuration values at build time
    public static final String MOD_ID = "GRADLETOKEN_MODID";
    public static final String MOD_NAME = "GRADLETOKEN_MODNAME";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final String MINECRAFT_VERSION = "1.7.10";
    public static final String ASM_PACKAGE = "com.github.basdxz.leafculling.asm";
    public static final String ASM_NAME = MOD_NAME + " ASM";
    public static final String ASM_PLUGIN_NAME = ASM_NAME + " Plugin";
    public static final String DEPENDENCIES = "" +
//                                              "required-after:spongemixins@[1.1.0,);" +
                                              "required-after:falsepatternlib@[0.10.13,);";
}