package com.github.basdxz.leafculling.asm;

import lombok.*;
import lombok.var;
import lombok.experimental.*;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.github.basdxz.leafculling.asm.impl.LeafCullingTransformer.LOG;
import static java.text.MessageFormat.format;

@UtilityClass
public final class ASMUtil {
    private static final ClassLoader CLASS_LOADER = ASMUtil.class.getClassLoader();
    private static final String OBJECT_CLASS_NAME = "java/lang/Object";
    private static final String CLASS_EXTENSION = ".class";

    public static boolean isClassCastableTo(@NonNull String sourceClassName, @NonNull final String targetClassName) {
        var className = sourceClassName;
        try {
            while (!className.equals(OBJECT_CLASS_NAME)) {
                if (className.equals(targetClassName)) {
                    System.out.println("CAST_CHECK PASS: " + sourceClassName);
                    return true;
                } else {
                    className = newClassReader(className).getSuperName();
                }
            }
        } catch (IOException e) {
            LOG.warn(format("Failed to check if class: {0} can be cast into {1}",
                            className,
                            targetClassName),
                     e);
        }
        return false;
    }

    public static ClassReader newClassReader(@NonNull String className) throws IOException {
        val classInputStream = getResourceBytes(className + CLASS_EXTENSION);
        if (!classInputStream.isPresent())
            throw new IOException("Failed to get bytes for:" + "");
        return new ClassReader(classInputStream.get());
    }

    public static Optional<byte[]> getResourceBytes(@NonNull String resourceLocation) throws IOException {
        val resourceInputStream = getResourceInputStream(resourceLocation);
        if (!resourceInputStream.isPresent())
            return Optional.empty();
        return Optional.of(IOUtils.toByteArray(resourceInputStream.get()));
    }

    public static Optional<InputStream> getResourceInputStream(@NonNull String resourceLocation) {
        return Optional.ofNullable(CLASS_LOADER.getResourceAsStream(resourceLocation));
    }
}
