package com.github.basdxz.leafculling;

import cpw.mods.fml.common.Loader;
import lombok.*;
import lombok.experimental.*;
import net.minecraft.block.Block;
import tconstruct.TConstruct;
import tconstruct.world.blocks.OreberryBush;

@UtilityClass
public final class ModCompat {
    private static final int MAX_SIZE_META = 8;
    private static final int MAX_BUSH_TYPE_META = 4;

    private static boolean IS_TCONSTRUCT_PRESENT;

    public static void init() {
        IS_TCONSTRUCT_PRESENT = Loader.isModLoaded(TConstruct.modID);
    }

    public static boolean isBlockTConstructOreberryBush(@NonNull Block block) {
        return IS_TCONSTRUCT_PRESENT && block instanceof OreberryBush;
    }

    public static boolean isBlockTConstructOreberryBushSameMeta(int thisBlockMetadata, int otherBlockMetadata) {
        if (otherBlockMetadata < MAX_SIZE_META || thisBlockMetadata < MAX_SIZE_META)
            return false;
        return thisBlockMetadata % MAX_BUSH_TYPE_META == otherBlockMetadata % MAX_BUSH_TYPE_META;
    }
}
