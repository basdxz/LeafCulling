package com.github.basdxz.leafculling.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.*;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.always;
import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.require;
import static com.falsepattern.lib.mixin.IMixin.Side.CLIENT;
import static com.github.basdxz.leafculling.mixin.plugin.TargetedMod.*;


@Getter
@RequiredArgsConstructor
public enum Mixin implements IMixin {
    /**
     * Always required Mixins.
     */
    BlockLeavesBaseHideSideAdjacentToEqualMixin(CLIENT, always(), "minecraft.BlockLeavesBaseHideSideAdjacentToEqualMixin"),
    /**
     * Chisel Mixins.
     */
    BlockLeafHideSideAdjacentToEqualMixin(CLIENT, require(CHISEL), "chisel.BlockLeafHideSideAdjacentToEqualMixin"),
    /**
     * Biomes O' Plenty Mixins.
     */
    BlockBOPLeavesHideSideAdjacentToEqualMixin(CLIENT, require(BIOMESOPLENTY), "bop.BlockBOPLeavesHideSideAdjacentToEqualMixin"),
    /**
     * Forestry Mixins.
     */
    BlockForestryLeavesHideSideAdjacentToEqualMixin(CLIENT, require(FORESTRY), "forestry.BlockForestryLeavesHideSideAdjacentToEqualMixin"),
    ;

    private final Side side;
    private final Predicate<List<ITargetedMod>> filter;
    private final String mixin;
}
