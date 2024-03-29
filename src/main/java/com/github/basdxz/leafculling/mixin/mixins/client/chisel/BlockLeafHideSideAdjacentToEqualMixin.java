package com.github.basdxz.leafculling.mixin.mixins.client.chisel;

import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.*;
import team.chisel.block.BlockCarvable;
import team.chisel.block.BlockLeaf;

import static com.github.basdxz.leafculling.LeafCulling.isBlockAtSideSameLeaf;

@Mixin(BlockLeaf.class)
public abstract class BlockLeafHideSideAdjacentToEqualMixin extends BlockCarvable {
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess,
                                        int otherXPos,
                                        int otherYPos,
                                        int otherZPos,
                                        int side) {
        return !isBlockAtSideSameLeaf(blockAccess, otherXPos, otherYPos, otherZPos, side);
    }
}
