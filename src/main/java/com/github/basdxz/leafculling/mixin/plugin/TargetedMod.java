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
    CHISEL("Chisel", true, startsWith("chisel")),
    BIOMES_O_PLENTY("Biomes O' Plenty", true, startsWith("biomesoplenty")),
    FORESTRY("Forestry", true, startsWith("forestry")),
    MINEFACTORY_RELOADED("Minefactory Reloaded", true, startsWith("minefactory-reloaded")),
    AETHER("The Aether", true, startsWith("aether-"));

    private final String modName;
    private final boolean loadInDevelopment;
    private final Predicate<String> condition;
}
