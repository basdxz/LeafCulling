package com.github.basdxz.leafculling.mixin.mixins.client.chisel;

import com.github.basdxz.leafculling.LeafCulling;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.*;
import team.chisel.block.BlockCarvable;
import team.chisel.block.BlockLeaf;

@Mixin(BlockLeaf.class)
public abstract class BlockLeafHideSideAdjacentToEqualMixin extends BlockCarvable {
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess,
                                        int otherXPos,
                                        int otherYPos,
                                        int otherZPos,
                                        int side) {
        return !LeafCulling.isBlockAtSideSameLeaf(blockAccess, otherXPos, otherYPos, otherZPos, side);
    }
}
