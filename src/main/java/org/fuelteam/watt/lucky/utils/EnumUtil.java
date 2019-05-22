package org.fuelteam.watt.lucky.utils;

import java.util.EnumSet;

import org.apache.commons.lang3.EnumUtils;

public class EnumUtil {

	public static <E extends Enum<E>> long generateBits(final Class<E> enumClass, final Iterable<? extends E> values) {
		return EnumUtils.generateBitVector(enumClass, values);
	}

	@SuppressWarnings("unchecked")
    public static <E extends Enum<E>> long generateBits(final Class<E> enumClass, final E... values) {
		return EnumUtils.generateBitVector(enumClass, values);
	}

	public static <E extends Enum<E>> EnumSet<E> processBits(final Class<E> enumClass, final long value) {
		return EnumUtils.processBitVector(enumClass, value);
	}

	/**
	 * Enum转换为String
	 */
	@SuppressWarnings("rawtypes")
    public static String toString(Enum e) {
		return e.name();
	}

	/**
	 * String转换为Enum
	 */
	public static <T extends Enum<T>> T fromString(Class<T> enumClass, String value) {
		return Enum.valueOf(enumClass, value);
	}
}