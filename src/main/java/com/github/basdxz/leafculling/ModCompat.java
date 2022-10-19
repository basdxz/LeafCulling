package com.github.basdxz.leafculling;

import cpw.mods.fml.common.Loader;
import lombok.*;
import lombok.experimental.*;
import net.minecraft.block.Block;
import tconstruct.TConstruct;
import tconstruct.world.blocks.OreberryBush;

@UtilityClass
public final class ModCompat {
    private static boolean IS_TCONSTRUCT_PRESENT;

    public static void init() {
        IS_TCONSTRUCT_PRESENT = Loader.isModLoaded(TConstruct.modID);
    }

    public static boolean isBlockTConstructOreberryBush(@NonNull Block block) {
        return IS_TCONSTRUCT_PRESENT && block instanceof OreberryBush;
    }

    public static boolean isBlockTConstructOreberryBushMeta(int thisBlockMetadata,
                                                            int otherBlockMetadata) {
        return otherBlockMetadata >= 8;
    }
}
