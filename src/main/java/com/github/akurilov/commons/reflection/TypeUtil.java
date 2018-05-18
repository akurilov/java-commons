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
				return null;
			}
		}

		if(val instanceof Byte) {
			final Byte b = (Byte) val;
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast byte to boolean");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				return (T) (Object) (char) (b.byteValue() & 0xFF);
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				return (T) (Object) b.shortValue();
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) b.intValue();
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) b.longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) b.floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) b.doubleValue();
			}
			if(cls.equals(String.class)) {
				return (T) val;
			}
		}
		if(val instanceof Short) {
			final Short v = (Short) val;
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				throw new ClassCastException("Cannot cast short to boolean");
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				throw new ClassCastException("Cannot cast short to byte");
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				return (T) (Object) (char) (v.shortValue() & 0xFFFF);
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) v.intValue();
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) v.longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) v.floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) v.doubleValue();
			}
			if(cls.equals(String.class)) {
				return (T) val;
			}
		}
		if(val instanceof Character) {
			final char c = (char) val;
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
				return (T) (Object) (int) c;
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) (long) c;
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) (float) c;
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) (double) c;
			}
			if(cls.equals(String.class)) {
				return (T) val;
			}
		}
		if(val instanceof Integer) {
			final Integer i = (Integer) val;
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
				return (T) (Object) i.longValue();
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) i.floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) i.doubleValue();
			}
			if(cls.equals(String.class)) {
				return (T) val;
			}
		}
		if(val instanceof Long) {
			final Long v = (Long) val;
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
				return (T) (Object) v.floatValue();
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) v.doubleValue();
			}
			if(cls.equals(String.class)) {
				return (T) val;
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
			if(cls.equals(String.class)) {
				return (T) val;
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
			if(cls.equals(String.class)) {
				return (T) val;
			}
		}
		if(val instanceof String) {
			final String s = (String) val;
			if(cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				return (T) (Object) Boolean.parseBoolean(s);
			}
			if(cls.equals(byte.class) || cls.equals(Byte.class)) {
				return (T) (Object) Byte.parseByte(s);
			}
			if(cls.equals(short.class) || cls.equals(Short.class)) {
				return (T) (Object) Short.parseShort(s);
			}
			if(cls.equals(char.class) || cls.equals(Character.class)) {
				if(s.length() == 1) {
					return (T) (Object) s.charAt(0);
				}
				throw new ClassCastException("Cannot cast string to char");
			}
			if(cls.equals(int.class) || cls.equals(Integer.class)) {
				return (T) (Object) Integer.parseInt(s);
			}
			if(cls.equals(long.class) || cls.equals(Long.class)) {
				return (T) (Object) Long.parseLong(s);
			}
			if(cls.equals(float.class) || cls.equals(Float.class)) {
				return (T) (Object) Float.parseFloat(s);
			}
			if(cls.equals(double.class) || cls.equals(Double.class)) {
				return (T) (Object) Double.parseDouble(s);
			}
		}

		final Class valCls = val.getClass();
		if(cls.isAssignableFrom(valCls) || typeEquals(cls, valCls)) {
			return (T) val;
		}

		throw new ClassCastException("Cannot cast " + valCls + " to " + cls);
	}
}
