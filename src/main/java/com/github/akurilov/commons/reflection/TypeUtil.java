package com.github.akurilov.commons.reflection;

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
		if(val == null && cls.isPrimitive()) {
			throw new ClassCastException("Couldn't cast null value to a primitive type");
		}
		if(val instanceof Byte) {
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast byte value to boolean");
			}
			if(cls.equals(char.class)) {
				return (T) (Object) (char) (((Byte) val).byteValue() & 0xFF);
			}
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
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast short value to boolean");
			}
			if(cls.equals(byte.class)) {
				throw new ClassCastException("Couldn't cast short value to byte");
			}
			if(cls.equals(char.class)) {
				return (T) (Object) (char) (((Short) val).shortValue() & 0xFFFF);
			}
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
		if(val instanceof Character) {
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast char value to boolean");
			}
			if(cls.equals(byte.class)) {
				throw new ClassCastException("Couldn't cast char value to byte");
			}
			if(cls.equals(short.class)) {
				throw new ClassCastException("Couldn't cast char value to short");
			}
			if(cls.equals(int.class)) {
				return (T) (Object) (int) ((char) val);
			}
			if(cls.equals(long.class)) {
				return (T) (Object) (long) ((char) val);
			}
			if(cls.equals(float.class)) {
				return (T) (Object) (float) ((char) val);
			}
			if(cls.equals(double.class)) {
				return (T) (Object) (double) ((char) val);
			}
		}
		if(val instanceof Integer) {
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast int value to boolean");
			}
			if(cls.equals(byte.class)) {
				throw new ClassCastException("Couldn't cast int value to byte");
			}
			if(cls.equals(short.class)) {
				throw new ClassCastException("Couldn't cast int value to short");
			}
			if(cls.equals(char.class)) {
				throw new ClassCastException("Couldn't cast int value to char");
			}
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
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast long value to boolean");
			}
			if(cls.equals(byte.class)) {
				throw new ClassCastException("Couldn't cast long value to byte");
			}
			if(cls.equals(short.class)) {
				throw new ClassCastException("Couldn't cast long value to short");
			}
			if(cls.equals(char.class)) {
				throw new ClassCastException("Couldn't cast long value to char");
			}
			if(cls.equals(int.class)) {
				throw new ClassCastException("Couldn't cast long value to int");
			}
			if(cls.equals(float.class)) {
				return (T) (Object) ((Long) val).floatValue();
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Long) val).doubleValue();
			}
		}
		if(val instanceof Float) {
			if(cls.equals(boolean.class)) {
				throw new ClassCastException("Couldn't cast float value to boolean");
			}
			if(cls.equals(byte.class)) {
				throw new ClassCastException("Couldn't cast float value to byte");
			}
			if(cls.equals(short.class)) {
				throw new ClassCastException("Couldn't cast float value to short");
			}
			if(cls.equals(char.class)) {
				throw new ClassCastException("Couldn't cast float value to char");
			}
			if(cls.equals(int.class)) {
				throw new ClassCastException("Couldn't cast float value to int");
			}
			if(cls.equals(long.class)) {
				throw new ClassCastException("Couldn't cast float value to long");
			}
			if(cls.equals(double.class)) {
				return (T) (Object) ((Float) val).doubleValue();
			}
		}
		return (T) val;
	}
}
