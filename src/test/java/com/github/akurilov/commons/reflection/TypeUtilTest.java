package com.github.akurilov.commons.reflection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.akurilov.commons.reflection.TypeUtil.typeConvert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static com.github.akurilov.commons.reflection.TypeUtil.typeEquals;
import static org.junit.Assert.fail;

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
		System.out.println(typeConvert(42, Double.class));
		System.out.println(typeConvert(42L, double.class));
		System.out.println(typeConvert((float) 42.0, float.class));
		System.out.println(typeConvert(Arrays.asList(1, 2, 3), List.class));
		System.out.println(typeConvert("12,34,abc,", List.class));
		System.out.println(Arrays.toString(typeConvert("12,34,abc,", String[].class)));
		System.out.println(typeConvert(null, Integer.class));
	}

	@Test
	public void typeConvertFailTest()
	throws Exception {
		try {
			System.out.println(typeConvert((byte) 0x10, boolean.class));
			fail();
		} catch(final ClassCastException ok) {
		}
		try {
			System.out.println(typeConvert(Long.MAX_VALUE, int.class));
			fail();
		} catch(final ClassCastException ok) {
		}
		try {
			System.out.println(typeConvert(42.0, float.class));
			fail();
		} catch(final ClassCastException ok) {
		}
		try {
			System.out.println(typeConvert(null, int.class));
			fail();
		} catch(final ClassCastException ok) {
		}
		try {
			typeConvert(Arrays.asList(1, 2, 3), String.class);
			fail();
		} catch(final ClassCastException ok) {
		}
	}
}
