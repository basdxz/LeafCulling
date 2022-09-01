package com.github.basdxz.leafculling.mixin.plugin;

import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.*;

import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.startsWith;

/**
 * List of targeted mods used for mixing loading logic.
 */
@Getter
@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {
    OPTIFINE("OptiFine", false, startsWith("optifine"));

    private final String modName;
    private final boolean loadInDevelopment;
    private final Predicate<String> condition;
}
