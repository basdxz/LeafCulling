# Technical Details

A breakdown of the inner workings of Leaf Culling as of 2023-04-18.

## Dependencies

-   [FalsePatternLib](https://github.com/FalsePattern/FalsePatternLib): Provides mixin plugin extension logic.
-   [SpongeMixins](https://github.com/SpongePowered/Mixin): Mixin bootstrap mod.

## Entry Points

-   [Forge Mod](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/LeafCulling.java): Initializes compatibility classes at load time.
-   [Mixin Plugin](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/mixin/plugin/MixinPlugin.java): Applies mixin patches.

## Vanilla Behavior

### Leaf Class Hierarchy

![UML diagram displaying an inheritance hierarchy. BlockOldLeaf and BlockNewLeaf extend BlockLeaves. BlockLeaves extends BlockLeavesBase. BlockLeavesBase extends Block.](leaf_class_hierarchy.png)

In vanilla Minecraft, all leaf blocks extend `BlockLeaves`. Interestingly, `BlockLeaves` is the only class that extends `BlockLeavesBase`. `BlockOldLeaf` is utilized for Oak, Spruce, Birch, and Jungle leaves, while `BlockNewLeaf` is used for Acacia and Big Oak leaves.

### Leaf Metadata

![Graphic illustrating the order of the bits for leaf metadata, from MSB to LSB. Decay: 3, 2. Variation: 1, 0.](leaf_metadata.svg)

Minecraft has 4 bits for its metadata, allowing for 16 possible states for each block. However, each leaf block type can only represent 4 distinct variations. This is because half of the four bits are used to represent the leaf decay progress, leaving the remaining two bits for variations.

### Should Side Be Rendered Signature

Minecraft, like many other voxel games, employs an optimization technique to hide the internal faces of blocks. When a block is added to the chunk mesh, the following method from the `Block` class is called:

```Java
// As found when using MCP mappings
public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_);

// Cleaned up variable names, used in the following examples.
public boolean shouldSideBeRendered(IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side);
```

The method arguments in the provided Forge documentation can be misleading, so they have been corrected for clarity:

-   `IBlockAccess blockAccess`: Generally a `ChunkCache` object, which sources its data from `WorldClient`.
-   `int otherXPos, int otherYPos, int otherZPos`: MCP documentation refers to these as `x y z`, but they are intended to represent the position of the other block being compared to this one.
-   `int side`: This represents the side of this block being checked; it is also the direction of the block provided in the position.
-   `boolean return`: Returns true if the side should render, and false otherwise.

### Should Side Be Rendered in Leaves

The `BlockLeavesBase` implementation renders the obscured sides only when fancy graphics are enabled.

![Screenshot of vanilla Minecraft leaves with fancy graphics turned on. With a red overlay outline over the geometry of the leaf blocks. Demonstrating that the internal sides have geometry.](leaf_vanilla_fancy.png)

![Screenshot of vanilla Minecraft leaves with fast graphics turned on. With a red overlay outline over the geometry of the leaf blocks. Demonstrating that the internal sides don't have geometry.](leaf_vanilla_fast.png)

In these two examples, the first image shows fancy graphics, and the second one displays fast graphics. The red outline highlights the geometry of the blocks.

The following code determines whether to render or skip these sides:

```Java
// As found when using MCP mappings
public boolean shouldSideBeRendered(IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side) {
    Block block = blockAccess.getBlock(otherXPos, otherYPos, otherZPos);
    return !this.field_150121_P && block == this ? false : super.shouldSideBeRendered(blockAccess, otherXPos, otherYPos, otherZPos, side);
}

// Cleaned up for clarity.
public boolean shouldSideBeRendered(IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side) {
    // Delegates to the super method, returning false if it does.  
    if (!super.shouldSideBeRendered(blockAccess, otherXPos, otherYPos, otherZPos, side))
        return false;

    // Show the side if fancy graphics are enabled.  
    if (fancyGraphicsEnabled)
        return true;

    // Checking the other block; if it's not the same as this block, show the side.  
    Block otherBlock = blockAccess.getBlock(otherXPos, otherYPos, otherZPos);
    if (otherBlock != this)
        return true;

    // Returning false if the above check fails.  
    return false;
}
```

### Stained Glass Should Side be Rendered

![UML diagram showing an inheritance hierarchy. BlockStainedGlass extends BlockBreakable. BlockBreakable extends Block.](stained_glass_class_hierarchy.png)

In contrast to the desired behavior of making leaves act more like stained glass, the latter inherits its method from extending `BlockBreakable`.

![Screenshot of vanilla Minecraft showing three layers of stained glass blocks. Lime, red, and blue. Demonstrating how only the internal sides between the different layers are visible.](stained_glass.png)

Stained glass has a special case included in the `BlockBreakable` class, which checks if the nearby block is another stained glass with the same metadata. If this is the case, the side will not be rendered.

```Java
// As found when using MCP mappings.
public boolean shouldSideBeRendered(IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side) {
    Block block = blockAccess.getBlock(otherXPos, otherYPos, otherZPos);

    if (this == Blocks.glass || this == Blocks.stained_glass) {
        if (blockAccess.getBlockMetadata(otherXPos, otherYPos, otherZPos) != blockAccess.getBlockMetadata(otherXPos - Facing.offsetsXForSide[side], otherYPos - Facing.offsetsYForSide[side], otherZPos - Facing.offsetsZForSide[side])) {
            return true;
        }

        if (block == this) {
            return false;
        }
    }

    return !this.field_149996_a && block == this ? false : super.shouldSideBeRendered(blockAccess, otherXPos, otherYPos, otherZPos, side);
}

// Cleaned up for clarity.
public boolean shouldSideBeRendered(IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side) {
    // Gets the other block.
    Block otherBlock = blockAccess.getBlock(otherXPos, otherYPos, otherZPos);

    // The special case for glass blocks.
    if (this == Blocks.glass || this == Blocks.stained_glass) {
        // Fetches the metadata of this and the other block.
        int thisBlockMetadata = blockAccess.getBlockMetadata(otherXPos - Facing.offsetsXForSide[side],
                                                             otherYPos - Facing.offsetsYForSide[side],
                                                             otherZPos - Facing.offsetsZForSide[side]);
        int otherBlockMetadata = blockAccess.getBlockMetadata(otherXPos, otherYPos, otherZPos);

        // If the metadata doesn't match, render the side.
        if (thisBlockMetadata != otherBlockMetadata)
            return true;

        // If the block is equal to this block (in this case, both meta and object) render it.
        if (otherBlock == this)
            return false;

        // If these blocks are the exact same and hide internal sides is false, skip rendering this block.
        // As a note, the `showInternalSides` variable is always false in Vanilla.
        if (otherBlock == this && !showInternalSides)
            return false;
    }

    // Finally, fallback on the super method.
    return super.shouldSideBeRendered(blockAccess, otherXPos, otherYPos, otherZPos, side);
}

```

## Implemented Solution

### Resulting Visuals

![Screenshot of Minecraft with the Leaf Culling mod installed. Showcasing various trees side-by-side.](final_product.png)

[![YouTube video demonstrating the decay of leaves. When the leaves decay, their decaying state is not factored into the metadata comparison.](http://img.youtube.com/vi/AWxp2vOyoHY/0.jpg)](http://www.youtube.com/watch?v=AWxp2vOyoHY)

The implemented solution produces the desired visual effect, as shown in the images and video above, while properly handling decaying leaves.

### Mixin Injection

The first step of this implementation involves injecting code into the [BlockLeavesBase.class](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/mixin/mixins/client/minecraft/BlockLeavesBaseHideSideAdjacentToEqualMixin.java). We insert our own check at the start of the `shouldSideBeRendered` method.

This injection in the `BlockLeavesBase` class alters the behavior of the `shouldSideBeRendered` method, allowing the mod to achieve the desired effect of hiding internal faces of leaf blocks.

### New Should Side be Rendered Check

The injection delegates to a method in the [LeafCulling.class](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/LeafCulling.java#L28), which performs checks similar to those for stained glass. It compares the current block with the adjacent block, including their metadata, but with the significant difference that the two most significant bits of the leaves, which represent decay, are masked off.

## Mod Compatibility

### Explicitly Patched Mods

A list of patches for supported mods can be found in the [Mixin.class](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/mixin/plugin/Mixin.java).

For convenience, the supported mods are:

-   Chisel
-   Biomes O' Plenty
-   Forestry
-   Minefactory Reloaded
-   Aether (1 and 2)
-   Tinkers Construct
-   Thaumcraft
-   Ancient Trees

Additional mod support will be added upon request.

### Why are Patches Needed?

While many mods work out of the box, some require explicit patches for various reasons:

1. Replacing the `shouldSideBeRendered` method to always return `true`, such as in Biomes 'O Plenty's `BlockBOPLeaves` class:
```Java
public boolean shouldSideBeRendered(IBlockAccess world,int x,int y,int z,int side){
    return true;
}
```
2. Copying the method from `shouldSideBeRendered` unchanged, such as in Thaumcraft's `BlockMagicalLeaves` class:
```Java
public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    Block var6 = par1IBlockAccess.getBlock(par2, par3, par4);
    return Blocks.leaves.isOpaqueCube() && var6 == this ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
}
```
3. Not extending the `BlockLeaves` or `BlockLeavesBase` class, such as in Chisel's `BlockLeaf` class:

![UML diagram showing an inheritance hierarchy. BlockLeaf extends BlockCarvable. BlockCarvable extends Block and implements ICarvable. ICarvable extends ICTMBlock.](chisel_leaves_class_hierarchy.png)  
UML diagram showing an inheritance hierarchy. BlockLeaf extends BlockCarvable. BlockCarvable extends Block and implements ICarvable. ICarvable extends ICTMBlock.

In these and a few other scenarios, custom compatibility patches are needed.

## Tinkers Construct Ore Berries Patch

![Screenshot of Minecraft with the Leaf Culling mod installed. Showcasing how only the insides of a fully grown ore berry bush have their geometry hidden.](ore_berries.png)

The Tinkers Construct ore berry bush is an interesting edge case. Firstly, it is unusual for it to extend `BlockLeavesBase` given its significant differences from a standard leaf block. Secondly, the bush's size changes as it grows, requiring additional logic for proper functioning.

The implemented solution can be found in the [ModCompat.class](https://github.com/basdxz/LeafCulling/blob/95feaed779b8da52d29b0513e164f164a862d35c/src/main/java/com/github/basdxz/leafculling/ModCompat.java). If Tinkers Construct is present, an extra check can be performed. The implementation is somewhat naive but works well in this context.

Side culling is restricted by two criteria: the bushes must be of the same ore bush variation, and both must be fully grown.

## Future Improvements

In the next iteration of Leaf Culling, the goal is to move away from the individual Mixin-based approach to a more general global-ASM based approach, adding extra logic only for edge cases such as berry bushes or other unique scenarios that may arise.

Another improvement would be to include an in-game method for adding additional leaves and storing them in a configuration file. This could also include shipping the mod with a solid default configuration file for any outliers.

The [Cull Leaves](https://www.curseforge.com/minecraft/mc-mods/cull-leaves) mod, alongside the [Better Leaves](https://www.curseforge.com/minecraft/texture-packs/motschens-better-leaves) resource pack, both created by [Motschen](https://legacy.curseforge.com/members/Motschen/projects), provide an enhanced visual experience in Minecraft versions 1.13 and up. This is partly due to the fact that they only hide the backside of the leaves if they are deep enough within the tree. Including equivalent functionality in Leaf Culling is also planned for future updates.
