package com.github.akurilov.commons.reflection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.akurilov.commons.reflection.TypeUtil.typeConvert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static com.github.akurilov.commons.reflection.TypeUtil.typeEquals;

/**
 Created by andrey on 13.11.16.
 */
public class TypeUtilTest {

	@Test
	public void typeEqualsTest()
	throws Exception {

		assertTrue(typeEquals(boolean.class, Boolean.class));
		assertTrue(typeEquals(Boolean.class, boolean.class));
		assertTrue(typeEquals(int.class, Integer.class));
		assertTrue(typeEquals(Integer.class, int.class));
		assertTrue(typeEquals(long.class, Long.class));
		assertTrue(typeEquals(Long.class, long.class));
		assertTrue(typeEquals(double.class, Double.class));
		assertTrue(typeEquals(Double.class, double.class));

		assertFalse(typeEquals(Object.class, String.class));
		assertFalse(typeEquals(String.class, Object.class));
		assertFalse(typeEquals(Integer.class, Long.class));
		assertFalse(typeEquals(long.class, int.class));
	}

	@Test
	public void typeConvertTest()
	throws Exception {
		typeConvert(42, Double.class);
		typeConvert(42L, double.class);
		typeConvert(42.0, float.class);
		typeConvert(Double.valueOf(42.0), float.class);
		typeConvert(Arrays.asList(1, 2, 3), List.class);
	}
}
