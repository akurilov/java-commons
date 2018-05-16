package com.github.akurilov.commons.reflection;

import java.lang.reflect.Field;

public interface TypeUtil {

	/**
	 * Compare the types, allows to compare primitive types with their wrapper classes, etc
	 */
	static boolean typeEquals(final Class cls1, final Class cls2) {

		if(cls1.equals(cls2)) {
			return true;
		} else {

			Object primitiveType1 = null;
			try {
				primitiveType1 = cls1.getField("TYPE").get(cls1);
			} catch(final NoSuchFieldException | IllegalAccessException ignored) {
			}

			Object primitiveType2 = null;
			try {
				primitiveType2 = cls2.getField("TYPE").get(cls2);
			} catch(final NoSuchFieldException | IllegalAccessException ignored) {
			}

			if(primitiveType1 == null) {
				if(primitiveType2 == null) {
					return false;
				} else {
					return cls1.equals(primitiveType2);
				}
			} else if(primitiveType2 == null) {
				return cls2.equals(primitiveType1);
			} else {
				return primitiveType1.equals(primitiveType2);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "UnnecessaryUnboxing" })
	static <T> T typeConvert(final Object val, final Class<T> cls)
	throws ClassCastException {
		if(val instanceof Byte) {
			if(cls.equals(short.class)) {
				return (T) (Object) ((Byte) val).shortValue();
			}
			if(cls.equals(int.class)) {
				return (T) (Object) ((Byte) val).intValue();
			}
			if(cls.equals(long.class)) {
				return (T) (Object) ((Byte) val).longValue();
			}
			if(cls.equals(float.class)) {
				return (T) (Object) ((Byte) val).floatValue();
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Byte) val).doubleValue();
			}
		}
		if(val instanceof Short) {
			if(cls.equals(int.class)) {
				return (T) (Object) ((Short) val).intValue();
			}
			if(cls.equals(long.class)) {
				return (T) (Object) ((Short) val).longValue();
			}
			if(cls.equals(float.class)) {
				return (T) (Object) ((Short) val).floatValue();
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Short) val).doubleValue();
			}
		}
		if(val instanceof Integer) {
			if(cls.equals(long.class)) {
				return (T) (Object) ((Integer) val).longValue();
			}
			if(cls.equals(float.class)) {
				return (T) (Object) ((Integer) val).floatValue();
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Integer) val).doubleValue();
			}
		}
		if(val instanceof Long) {
			if(cls.equals(float.class)) {
				return (T) (Object) ((Long) val).floatValue();
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Long) val).doubleValue();
			}
		}
		if(val instanceof Float) {
			if(cls.equals(double.class)) {
				return (T) (Object) ((Float) val).doubleValue();
			}
		}
		return (T) val;
	}
}
