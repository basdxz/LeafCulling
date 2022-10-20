package com.github.basdxz.leafculling.mixin.mixins.client.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import thaumcraft.common.blocks.BlockMagicalLeaves;

import static com.github.basdxz.leafculling.LeafCulling.handleHidingSidesAdjacentEqualLeaves;

@Mixin(BlockMagicalLeaves.class)
public abstract class BlockMagicalLeavesLeavesHideSideAdjacentToEqualMixin extends Block implements IShearable {
    protected BlockMagicalLeavesLeavesHideSideAdjacentToEqualMixin(Material material) {
        super(material);
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
