package com.github.basdxz.leafculling;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Optional;

import static com.github.basdxz.leafculling.Tags.*;
import static net.minecraftforge.common.util.ForgeDirection.getOrientation;

@Mod(modid = MOD_ID,
     version = VERSION,
     name = MOD_NAME,
     acceptedMinecraftVersions = MINECRAFT_VERSION,
     dependencies = DEPENDENCIES)
public final class LeafCulling {
    // Masks off the bits used for tracking leaf decay
    public static final int LEAF_DECAY_METADATA_MASK = 0xC;

    private static final int LEAF_DEPTH = 2;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        ModCompat.init();
    }

    public static boolean isBlockAtSideSameLeaf(IBlockAccess blockAccess,
                                                int otherXPos,
                                                int otherYPos,
                                                int otherZPos,
                                                int directionID) {
        val direction = getOrientation(directionID);
        Block currentBlock = getBlock(blockAccess, otherXPos, otherYPos, otherZPos, direction, -1);
        int currentBlockMeta = getBlockMetadata(blockAccess, otherXPos, otherYPos, otherZPos, direction, -1);
        currentBlockMeta = applyLeafDecayMask(currentBlock, currentBlockMeta);
        Block lastBlock;
        int lastBlockMeta;
        for (var i = 0; i < LEAF_DEPTH; i++) {
            lastBlock = currentBlock;
            lastBlockMeta = currentBlockMeta;
            currentBlock = getBlock(blockAccess, otherXPos, otherYPos, otherZPos, direction, i);
            currentBlockMeta = getBlockMetadata(blockAccess, otherXPos, otherYPos, otherZPos, direction, i);
            currentBlockMeta = applyLeafDecayMask(currentBlock, currentBlockMeta);
            if (currentBlock != lastBlock || currentBlockMeta != lastBlockMeta)
                return false;

            Optional<Boolean> opt = Optional.ofNullable(false);
        }
        return true;
    }

    private static Block getBlock(IBlockAccess blockAccess,
                                  int posX,
                                  int posY,
                                  int posZ,
                                  ForgeDirection direction,
                                  int distance) {
        return blockAccess.getBlock(posX + (direction.offsetX * distance),
                                    posY + (direction.offsetY * distance),
                                    posZ + (direction.offsetZ * distance));
    }

    private static int getBlockMetadata(IBlockAccess blockAccess,
                                        int posX,
                                        int posY,
                                        int posZ,
                                        ForgeDirection direction,
                                        int distance) {
        return blockAccess.getBlockMetadata(posX + (direction.offsetX * distance),
                                            posY + (direction.offsetY * distance),
                                            posZ + (direction.offsetZ * distance));

    }

    private static int applyLeafDecayMask(Block block, int blockMeta) {
        if (block instanceof BlockLeavesBase)
            blockMeta &= ~LEAF_DECAY_METADATA_MASK;
        return blockMeta;
    }

    private static boolean areBlocksEqual(Block currentBlock,
                                          int currentBlockMeta,
                                          Block lastBlock,
                                          int lastBlockMeta) {
        return ModCompat.areBlocksEqual(currentBlock, currentBlockMeta, lastBlock, lastBlockMeta)
                        .orElse(currentBlock == lastBlock && currentBlockMeta == lastBlockMeta);
    }
}