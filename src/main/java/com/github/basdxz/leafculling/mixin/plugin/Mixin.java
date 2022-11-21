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
     * Vanilla
     */
    BlockLeavesBaseHideSideAdjacentToEqualMixin(CLIENT, always().negate(), "minecraft.BlockLeavesBaseHideSideAdjacentToEqualMixin"),
    /**
     * Chisel
     */
    BlockLeafHideSideAdjacentToEqualMixin(CLIENT, require(CHISEL).negate(), "chisel.BlockLeafHideSideAdjacentToEqualMixin"),
    /**
     * Biomes O' Plenty
     */
    BlockBOPLeavesHideSideAdjacentToEqualMixin(CLIENT, require(BIOMES_O_PLENTY).negate(), "biomesoplenty.BlockBOPLeavesHideSideAdjacentToEqualMixin"),
    /**
     * Forestry
     */
    BlockForestryLeavesHideSideAdjacentToEqualMixin(CLIENT, require(FORESTRY).negate(), "forestry.BlockForestryLeavesHideSideAdjacentToEqualMixin"),
    /**
     * Minefactory Reloaded
     */
    BlockRubberLeavesHideSideAdjacentToEqualMixin(CLIENT, require(MINEFACTORY_RELOADED).negate(), "minefactoryreloaded.BlockRubberLeavesHideSideAdjacentToEqualMixin"),
    /**
     * The Aether
     */
    BlockAetherLeavesHideSideAdjacentToEqualMixin(CLIENT, require(AETHER).negate(), "aether.BlockAetherLeavesHideSideAdjacentToEqualMixin"),
    /**
     * Thaumcraft
     */
    BlockMagicalLeavesLeavesHideSideAdjacentToEqualMixin(CLIENT, require(THAUMCRAFT).negate(), "thaumcraft.BlockMagicalLeavesLeavesHideSideAdjacentToEqualMixin"),
    /**
     * Kore Sample
     */
    LeavesBlockHideSideAdjacentToEqualMixin(CLIENT, require(KORE_SAMPLE).negate(), "koresample.LeavesBlockHideSideAdjacentToEqualMixin"),
    ;

    private final Side side;
    private final Predicate<List<ITargetedMod>> filter;
    private final String mixin;
}
