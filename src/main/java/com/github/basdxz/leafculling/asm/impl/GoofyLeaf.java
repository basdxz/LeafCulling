package com.github.basdxz.leafculling.asm.impl;

import com.falsepattern.lib.asm.IClassNodeTransformer;
import com.falsepattern.lib.internal.asm.CoreLoadingPlugin;
import com.falsepattern.lib.mapping.MappingManager;
import com.falsepattern.lib.mapping.types.MappingType;
import com.falsepattern.lib.mapping.types.NameType;
import com.falsepattern.lib.mapping.types.UniversalMethod;
import lombok.*;
import org.objectweb.asm.tree.*;

import java.util.Optional;

import static com.github.basdxz.leafculling.asm.ASMUtil.isClassCastableTo;
import static org.objectweb.asm.Opcodes.*;

public final class GoofyLeaf implements IClassNodeTransformer {
    private static final IClassNodeTransformer INSTANCE = new GoofyLeaf();
    private static final String BLOCK_LEAVES_BASE_NAME = "net/minecraft/block/BlockLeavesBase";
    private static final UniversalMethod UNIVERSAL_METHOD;

    static {
        try {
            UNIVERSAL_METHOD = MappingManager
                    .classForName(NameType.Internal, MappingType.MCP, BLOCK_LEAVES_BASE_NAME)
                    .getMethod(CoreLoadingPlugin.isObfuscated() ? MappingType.SRG : MappingType.MCP,
                               "shouldSideBeRendered",
                               "(Lnet/minecraft/world/IBlockAccess;IIII)Z");
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
        return isClassCastableTo(classNode.name, BLOCK_LEAVES_BASE_NAME) ||
               isClassCastableTo(classNode.name, "thaumcraft/common/blocks/BlockMagicalLeaves") ||
               isClassCastableTo(classNode.name, "team/chisel/block/BlockLeaf");
    }

    @Override
    public void transform(ClassNode classNode, String transformedName, boolean obfuscated) {
        shouldSideBeRenderedMethod(classNode).ifPresent(methodNode -> {
            System.out.println("GOOFY_LEAF Applying to: " + classNode.name);
            insertHideSideAdjacentToEqual(methodNode);
        });
    }

    private Optional<MethodNode> shouldSideBeRenderedMethod(ClassNode classNode) {
        return classNode.methods.stream()
                                .filter(this::isMethodShouldSideBeRendered)
                                .findAny();
    }

    private boolean isMethodShouldSideBeRendered(MethodNode methodNode) {
        return methodNode.name.equals(UNIVERSAL_METHOD.name().mcp()) &&
               methodNode.desc.equals(UNIVERSAL_METHOD.descriptor().mcp());
    }

    private boolean isMethodShouldSideBeRendered(ClassNode classNode, MethodNode methodNode) {
        UniversalMethod universalMethod = null;
        try {
            universalMethod = MappingManager.getMethod(classNode.name, methodNode);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
        val methodName = universalMethod.name().mcp();
        val methodDesc = universalMethod.descriptor().mcp();
        return methodName.equals("shouldSideBeRendered") && methodDesc.equals("(Lnet/minecraft/world/IBlockAccess;IIII)Z");
    }

    private void insertHideSideAdjacentToEqual(@NonNull MethodNode methodNode) {
        val newInstructions = new InsnList();
        val label = new LabelNode();
        newInstructions.add(new VarInsnNode(ALOAD, 1));
        newInstructions.add(new VarInsnNode(ILOAD, 2));
        newInstructions.add(new VarInsnNode(ILOAD, 3));
        newInstructions.add(new VarInsnNode(ILOAD, 4));
        newInstructions.add(new VarInsnNode(ILOAD, 5));
        newInstructions.add(new MethodInsnNode(INVOKESTATIC,
                                               "com/github/basdxz/leafculling/LeafCulling",
                                               "isBlockAtSideSameLeaf",
                                               "(Lnet/minecraft/world/IBlockAccess;IIII)Z",
                                               false));
        newInstructions.add(new JumpInsnNode(IFEQ, label));
        newInstructions.add(new InsnNode(ICONST_0));
        newInstructions.add(new InsnNode(IRETURN));
        newInstructions.add(label);
        methodNode.instructions.insert(newInstructions);
    }

    public static IClassNodeTransformer goofyLeaf() {
        return INSTANCE;
    }
}
