package com.github.basdxz.leafculling;

import cpw.mods.fml.common.Mod;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.github.basdxz.leafculling.Tags.*;
import static net.minecraftforge.common.util.ForgeDirection.getOrientation;

@Mod(modid = MODID, version = VERSION, name = MODNAME, acceptedMinecraftVersions = MINECRAFT_VERSION,
     dependencies = DEPENDENCIES)
public class LeafCulling {
    public static final int LEAF_DECAY_METADATA_MASK = 0xc;

    public static void handleHidingSidesAdjacentEqualLeaves(IBlockAccess blockAccess,
                                                            int otherXPos,
                                                            int otherYPos,
                                                            int otherZPos,
                                                            int side,
                                                            CallbackInfoReturnable<Boolean> cir) {
        if (isBlockAtSideSameLeaf(blockAccess, otherXPos, otherYPos, otherZPos, side))
            returnFalse(cir);
    }

    public static boolean isBlockAtSideSameLeaf(@NonNull IBlockAccess blockAccess,
                                                int otherXPos,
                                                int otherYPos,
                                                int otherZPos,
                                                int side) {
        return isBlockAtSideSameLeaf(blockAccess, otherXPos, otherYPos, otherZPos, getOrientation(side));
    }

    private static boolean isBlockAtSideSameLeaf(@NonNull IBlockAccess blockAccess,
                                                 int otherXPos,
                                                 int otherYPos,
                                                 int otherZPos,
                                                 @NonNull ForgeDirection direction) {
        val thisXPos = otherXPos - direction.offsetX;
        val thisYPos = otherYPos - direction.offsetY;
        val thisZPos = otherZPos - direction.offsetZ;
        val thisBlock = blockAccess.getBlock(thisXPos, thisYPos, thisZPos);
        if (!isBlockSame(blockAccess, thisBlock, otherXPos, otherYPos, otherZPos))
            return false;
        var thisBlockMetadata = blockAccess.getBlockMetadata(thisXPos, thisYPos, thisZPos);
        var otherBlockMetadata = blockAccess.getBlockMetadata(otherXPos, otherYPos, otherZPos);
        if (thisBlock instanceof BlockLeavesBase) {
            thisBlockMetadata &= ~LEAF_DECAY_METADATA_MASK;
            otherBlockMetadata &= ~LEAF_DECAY_METADATA_MASK;
        }
        return thisBlockMetadata == otherBlockMetadata;
    }

    private static boolean isBlockSame(@NonNull IBlockAccess blockAccess,
                                       @NonNull Block thisBlock,
                                       int otherXPos,
                                       int otherYPos,
                                       int otherZPos) {
        return thisBlock == blockAccess.getBlock(otherXPos, otherYPos, otherZPos);
    }

    public static void returnFalse(@NonNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}