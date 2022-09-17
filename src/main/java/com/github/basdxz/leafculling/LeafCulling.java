package com.github.basdxz.leafculling;

import cpw.mods.fml.common.Mod;
import lombok.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.github.basdxz.leafculling.Tags.*;
import static net.minecraftforge.common.util.ForgeDirection.getOrientation;

//,
//     dependencies = DEPENDENCIES
@Mod(modid = MODID, version = VERSION, name = MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class LeafCulling {
    public static void handleHidingSidesAdjacentToEqualBlock(IBlockAccess blockAccess,
                                                             int otherXPos,
                                                             int otherYPos,
                                                             int otherZPos,
                                                             int side,
                                                             CallbackInfoReturnable<Boolean> cir) {
        if (isBlockAtSideEqual(blockAccess, otherXPos, otherYPos, otherZPos, side))
            returnFalse(cir);
    }

    public static boolean isBlockAtSideEqual(@NonNull IBlockAccess blockAccess,
                                             int otherXPos,
                                             int otherYPos,
                                             int otherZPos,
                                             int side) {
        return isBlockAtSideEqual(blockAccess, otherXPos, otherYPos, otherZPos, getOrientation(side));
    }

    public static boolean isBlockAtSideEqual(@NonNull IBlockAccess blockAccess,
                                             int otherXPos,
                                             int otherYPos,
                                             int otherZPos,
                                             @NonNull ForgeDirection direction) {
        val thisXPos = otherXPos - direction.offsetX;
        val thisYPos = otherYPos - direction.offsetY;
        val thisZPos = otherZPos - direction.offsetZ;
        val thisBlock = blockAccess.getBlock(thisXPos, thisYPos, thisZPos);
        val otherBlock = blockAccess.getBlock(otherXPos, otherYPos, otherZPos);
        if (thisBlock != otherBlock)
            return false;
        val thisBlockMetadata = blockAccess.getBlockMetadata(thisXPos, thisYPos, thisZPos);
        val otherBlockMetadata = blockAccess.getBlockMetadata(otherXPos, otherYPos, otherZPos);
        return thisBlockMetadata == otherBlockMetadata;
    }

    public static void returnFalse(@NonNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}