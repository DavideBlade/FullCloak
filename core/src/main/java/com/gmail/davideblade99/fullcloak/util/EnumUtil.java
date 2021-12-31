/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.util;

public final class EnumUtil {

    private EnumUtil() {
        throw new IllegalAccessError();
    }

    public static <E extends Enum<E>> E getEnum(final String value, final Class<E> enumClass, final E defaultEnum) {
        try {
            return Enum.valueOf(enumClass, value);
        } catch (final Exception ignored) {
            return defaultEnum;
        }
    }

    public static <E extends Enum<E>> E getEnumIgnoreCase(final String value, final Class<E> enumClass, final E defaultEnum) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value))
                return e;
        }

        return defaultEnum;
    }

    public static <E extends Enum<E>> boolean isInEnum(final String value, final Class<E> enumClass) {
        return getEnum(value, enumClass, null) != null;
    }

    public static <E extends Enum<E>> boolean isInEnumIgnoreCase(final String value, final Class<E> enumClass) {
        return getEnumIgnoreCase(value, enumClass, null) != null;
    }
}
