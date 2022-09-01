package com.github.basdxz.leafculling.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.*;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.always;
import static com.falsepattern.lib.mixin.IMixin.Side.CLIENT;


@Getter
@RequiredArgsConstructor
public enum Mixin implements IMixin {
    /**
     * Always required Mixins.
     */
    BlockLeavesHideLeafAdjacentSideMixin(CLIENT, always(), "minecraft.BlockLeavesHideLeafAdjacentSideMixin");

    private final Side side;
    private final Predicate<List<ITargetedMod>> filter;
    private final String mixin;
}
