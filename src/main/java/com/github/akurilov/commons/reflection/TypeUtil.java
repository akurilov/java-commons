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

		if(val == null) {
			if(cls.isPrimitive()) {
				throw new ClassCastException("Cannot cast null to a primitive type");
			} else {
				return (T) val;
			}
		}

		if(val instanceof Byte) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast byte to boolean");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				return (T) (Object) (char) (((Byte) val).byteValue() & 0xFF);
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				return (T) (Object) ((Byte) val).shortValue();
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) ((Byte) val).intValue();
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) ((Byte) val).longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) ((Byte) val).floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) ((Byte) val).doubleValue();
			}
		}
		if(val instanceof Short) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast short to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast short to byte");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				return (T) (Object) (char) (((Short) val).shortValue() & 0xFFFF);
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) ((Short) val).intValue();
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) ((Short) val).longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) ((Short) val).floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) ((Short) val).doubleValue();
			}
		}
		if(val instanceof Character) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast char to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast char to byte");
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				throw new ClassCastException("Cannot cast char to short");
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) (int) ((char) val);
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) (long) ((char) val);
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) (float) ((char) val);
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) (double) ((char) val);
			}
		}
		if(val instanceof Integer) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast int to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast int to byte");
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				throw new ClassCastException("Cannot cast int to short");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				throw new ClassCastException("Cannot cast int to char");
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) ((Integer) val).longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) ((Integer) val).floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) ((Integer) val).doubleValue();
			}
		}
		if(val instanceof Long) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast long to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast long to byte");
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				throw new ClassCastException("Cannot cast long to short");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				throw new ClassCastException("Cannot cast long to char");
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				throw new ClassCastException("Cannot cast long to int");
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) ((Long) val).floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) ((Long) val).doubleValue();
			}
		}
		if(val instanceof Float) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast float to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast float to byte");
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				throw new ClassCastException("Cannot cast float to short");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				throw new ClassCastException("Cannot cast float to char");
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				throw new ClassCastException("Cannot cast float to int");
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				throw new ClassCastException("Cannot cast float to long");
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) ((Float) val).doubleValue();
			}
		}
		if(val instanceof Double) {
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast double to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast double to byte");
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				throw new ClassCastException("Cannot cast double to short");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				throw new ClassCastException("Cannot cast double to char");
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				throw new ClassCastException("Cannot cast double to int");
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				throw new ClassCastException("Cannot cast double to long");
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				throw new ClassCastException("Cannot cast double to float");
			}
		}

		final Class valCls = val.getClass();
		if(cls.isAssignableFrom(valCls) || typeEquals(cls, valCls)) {
			return (T) val;
		} else {
			throw new ClassCastException("Cannot cast " + valCls + " to " + cls);
		}
	}
}
