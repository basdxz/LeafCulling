package com.github.basdxz.leafculling.mixin.mixins.client.biomesoplenty;

import biomesoplenty.common.blocks.BlockBOPLeaves;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.github.basdxz.leafculling.LeafCulling.handleHidingSidesAdjacentEqualLeaves;

@Mixin(BlockBOPLeaves.class)
public abstract class BlockBOPLeavesHideSideAdjacentToEqualMixin extends BlockLeavesBase implements IShearable {
    protected BlockBOPLeavesHideSideAdjacentToEqualMixin(Material material, boolean flag) {
        super(material, flag);
    }

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
