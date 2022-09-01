package com.github.basdxz.leafculling.mixin.mixins.client.minecraft;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

//@Pseudo
//@Mixin(targets = {"net.minecraft.block.BlockLeavesBase",
//                  "biomesoplenty.common.blocks.BlockBOPAppleLeaves",
//                  "biomesoplenty.common.blocks.BlockBOPColorizedLeaves",
//                  "biomesoplenty.common.blocks.BlockBOPLeaves",
//                  "biomesoplenty.common.blocks.BlockBOPPersimmonLeaves"})
@Mixin(BlockLeavesBase.class)
public abstract class BlockLeavesHideLeafAdjacentSideMixin {
    @Inject(method = "shouldSideBeRendered(Lnet/minecraft/world/IBlockAccess;IIII)Z",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    private void hideSidesAdjacentToOtherLeaves(IBlockAccess blockAccess,
                                                int xPos,
                                                int yPos,
                                                int zPos,
                                                int side,
                                                CallbackInfoReturnable<Boolean> cir) {
        if (blockAccess.getBlock(xPos, yPos, zPos) instanceof BlockLeavesBase) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
