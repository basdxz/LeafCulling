package com.github.basdxz.leafculling.asm.impl;

import com.falsepattern.lib.asm.IClassNodeTransformer;
import com.falsepattern.lib.internal.asm.CoreLoadingPlugin;
import com.falsepattern.lib.mapping.MappingManager;
import lombok.*;
import org.objectweb.asm.tree.*;

import java.util.Optional;

import static com.falsepattern.lib.mapping.types.MappingType.MCP;
import static com.falsepattern.lib.mapping.types.NameType.Internal;
import static com.github.basdxz.leafculling.asm.ASMUtil.isClassCastableTo;
import static org.objectweb.asm.Opcodes.*;

public final class GoofyLeaf implements IClassNodeTransformer {
    private static final IClassNodeTransformer INSTANCE = new GoofyLeaf();
    private static final String BLOCK_LEAVES_BASE_NAME = "net/minecraft/block/BlockLeavesBase";
    private static final String SHOULD_BE_RENDERED_METHOD_NAME;
    private static final String SHOULD_BE_RENDERED_METHOD_DESC;

    static {
        try {
            val isObfuscated = CoreLoadingPlugin.isObfuscated();
            val universalMethod = MappingManager
                    .classForName(Internal, MCP, BLOCK_LEAVES_BASE_NAME)
                    .getMethod(MCP,
                               "shouldSideBeRendered",
                               "(Lnet/minecraft/world/IBlockAccess;IIII)Z");
            val name = universalMethod.name();
            val desc = universalMethod.descriptor();
            SHOULD_BE_RENDERED_METHOD_NAME = isObfuscated ? name.srg() : name.mcp();
            SHOULD_BE_RENDERED_METHOD_DESC = isObfuscated ? desc.srg() : desc.mcp();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    public final String name = "GoofyLeaf";

    @Override
    public boolean shouldTransform(ClassNode classNode, String transformedName, boolean obfuscated) {
        return isClassCastableToBlockLeaves(classNode);
    }

    private boolean isClassCastableToBlockLeaves(ClassNode classNode) {
        val name = classNode.name;
        return isClassCastableTo(name, BLOCK_LEAVES_BASE_NAME) ||
               isClassCastableTo(name, "thaumcraft/common/blocks/BlockMagicalLeaves") ||
               isClassCastableTo(name, "team/chisel/block/BlockLeaf");
    }

    @Override
    public void transform(ClassNode classNode, String transformedName, boolean obfuscated) {
        shouldSideBeRenderedMethod(classNode).ifPresent(methodNode -> {
            System.out.println("GOOFY_LEAF Applying to: " + classNode.name);
            insertHideSideAdjacentToEqual(methodNode);
        });
    }

    private Optional<MethodNode> shouldSideBeRenderedMethod(ClassNode classNode) {
        return classNode.methods.stream()//TODO: cleanup
                                .filter(this::isMethodShouldSideBeRendered)
                                .findAny();
    }

    private boolean isMethodShouldSideBeRendered(MethodNode methodNode) {
        return methodNode.name.equals(SHOULD_BE_RENDERED_METHOD_NAME) &&
               methodNode.desc.equals(SHOULD_BE_RENDERED_METHOD_DESC);
    }

    private void insertHideSideAdjacentToEqual(@NonNull MethodNode methodNode) {
        val newInstructions = new InsnList();
        val label = new LabelNode();
        /*
            Loads the arguments:
            (IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
            Which can be interpreted as:
            (IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side)
            Onto the stack
        */
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new VarInsnNode(ILOAD, 2));
        newInstructions.add(new VarInsnNode(ILOAD, 3));
        newInstructions.add(new VarInsnNode(ILOAD, 4));
        newInstructions.add(new VarInsnNode(ILOAD, 5));
        /*
            Invokes the method:
            com.github.basdxz.leafculling.LeafCulling(@NonNull IBlockAccess blockAccess, int otherXPos, int otherYPos, int otherZPos, int side)
            Which will consume the arguments and leave a boolean on the stack
         */
        newInstructions.add(new MethodInsnNode(INVOKESTATIC,
                                               "com/github/basdxz/leafculling/LeafCulling",
                                               "isBlockAtSideSameLeaf",
                                               "(Lnet/minecraft/world/IBlockAccess;IIII)Z",
                                               false));
        // Checks if the boolean is true, if so it jumps to the label
        newInstructions.add(new JumpInsnNode(IFEQ, label));
        // Loads false onto the stack
        newInstructions.add(new InsnNode(ICONST_0));
        // Returns the current boolean on the stack
        newInstructions.add(new InsnNode(IRETURN));
        // Label to jump to if the comparison earlier was false
        newInstructions.add(label);
        // Inserts the new instructions at the beginning of the method
        methodNode.instructions.insert(newInstructions);
    }

    public static IClassNodeTransformer goofyLeaf() {
        return INSTANCE;
    }
}
