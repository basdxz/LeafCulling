package com.github.basdxz.leafculling.asm.impl;

import com.falsepattern.lib.asm.IClassNodeTransformer;
import lombok.*;
import org.objectweb.asm.tree.ClassNode;


// Check if running in obf
// Find if class can be cast into BlockLeavesBase
// Check if has shouldSideBeRendered(Lnet/minecraft/world/IBlockAccess;IIII)Z
// Insert the early return with 'handleHidingSidesAdjacentEqualLeaves(blockAccess, otherXPos, otherYPos, otherZPos, side);'
public final class GoofyLeaf implements IClassNodeTransformer {
    private static final IClassNodeTransformer INSTANCE = new GoofyLeaf();

    @Getter
    public final String name = "GoofyLeaf";

    @Override
    public boolean shouldTransform(ClassNode cn, String transformedName, boolean obfuscated) {
        System.out.println(cn.superName);
        return false;
    }

    @Override
    public void transform(ClassNode cn, String transformedName, boolean obfuscated) {
    }

    public static IClassNodeTransformer goofyLeaf() {
        return INSTANCE;
    }
}
