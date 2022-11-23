package com.github.basdxz.leafculling.asm.impl;

import com.falsepattern.lib.asm.IClassNodeTransformer;
import com.falsepattern.lib.asm.SmartTransformer;
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.github.basdxz.leafculling.Tags.ASM_NAME;
import static com.github.basdxz.leafculling.asm.impl.LeafCullingInjector.goofyLeaf;
import static java.util.Collections.singletonList;

@Getter
@Accessors(fluent = true)
public final class LeafCullingTransformer implements SmartTransformer {
    public static final Logger LOG = LogManager.getLogger(ASM_NAME);
    public static final List<IClassNodeTransformer> TRANSFORMERS = singletonList(goofyLeaf());

    private final Logger logger = LOG;
    private final List<IClassNodeTransformer> transformers = TRANSFORMERS;
}
