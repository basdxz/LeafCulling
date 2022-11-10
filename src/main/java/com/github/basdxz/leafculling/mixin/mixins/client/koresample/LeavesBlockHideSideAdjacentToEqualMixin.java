package com.github.basdxz.leafculling.mixin.mixins.client.koresample;

import com.scottkillen.mod.koresample.tree.block.LeavesBlock;
import net.minecraft.block.BlockLeaves;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.github.basdxz.leafculling.LeafCulling.handleHidingSidesAdjacentEqualLeaves;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockHideSideAdjacentToEqualMixin extends BlockLeaves {
    @Inject(method = "shouldSideBeRendered(Lnet/minecraft/world/IBlockAccess;IIII)Z",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    private void hideSidesAdjacentToEqualBlock(IBlockAccess blockAccess,
                                               int otherXPos,
                                               int otherYPos,
                                               int otherZPos,
                                               int side,
                                               CallbackInfoReturnable<Boolean> cir) {
        handleHidingSidesAdjacentEqualLeaves(blockAccess, otherXPos, otherYPos, otherZPos, side, cir);
    }
}
