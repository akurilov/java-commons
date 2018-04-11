package com.github.akurilov.commons.system;

import java.io.Serializable;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 The class to represent some size data. A size may be formatted into the form like "1.234MB". Also the size may be not
 fixed but describe some size range with the given bias.
 */
public final class SizeInBytes
	implements Serializable {

	private static final String
		FMT_MSG_INVALID_SIZE = "The string \"%s\" doesn't match the pattern: \"%s\"";

	public static final String SIZE_UNITS = "kmgtpe";
	public static final Pattern PATTERN_SIZE = Pattern.compile("([\\d\\.]+)(["+SIZE_UNITS+"]?)b?");

	/**
	 * Parse the fixed size info. Example "16MB" -&gt; 16777216
	 * @return the numeric size
	 * @throws NumberFormatException
	 */
	public static long toFixedSize(final String value)
		throws NumberFormatException {
		final String unit;
		final var matcher = PATTERN_SIZE.matcher(value.toLowerCase());
		double size;
		long degree;
		if(matcher.matches() && matcher.groupCount() > 0 && matcher.groupCount() < 3) {
			size = Double.parseDouble(matcher.group(1));
			unit = matcher.group(2);
			if(unit.length() == 0) {
				degree = 0;
			} else if(unit.length() == 1) {
				degree = SIZE_UNITS.indexOf(matcher.group(2)) + 1;
			} else {
				throw new IllegalArgumentException(
					String.format(FMT_MSG_INVALID_SIZE, value, PATTERN_SIZE)
				);
			}
		} else {
			throw new IllegalArgumentException(
				String.format(FMT_MSG_INVALID_SIZE, value, PATTERN_SIZE)
			);
		}
		size *= 1L << 10 * degree;
		return (long) size;
	}

	/**
	 * Format the size info. Example: 16777216 -&gt; "16MB"
	 * @param v the numeric size
	 * @return the formatted size info
	 */
	public static String formatFixedSize(final long v) {
		if(v < 1024) {
			return v + "B";
		}
		final var z = (63 - Long.numberOfLeadingZeros(v)) / 10;
		final var x = (double) v / (1L << (z * 10));
		if(x % 1 == 0) {
			final var y = (long) x;
			return String.format(
				Locale.ROOT,
				y < 10 ? "%d%sb" : y < 100 ? "%d%sb" : "%d%sb",
				y, z > 0 ? SIZE_UNITS.charAt(z - 1) : ""
			).toUpperCase();
		} else {
			return String.format(
				Locale.ROOT,
				x < 10 ? "%.3f%sb" : x < 100 ? "%.2f%sb" : "%.1f%sb",
				x, z > 0 ? SIZE_UNITS.charAt(z - 1) : ""
			).toUpperCase();
		}
	}

	private static final char SEP1 = '-', SEP2 = ',';

	private long min, range = 0;
	private double bias = 1;

	public SizeInBytes(final String sizeInfo)
		throws NumberFormatException, IllegalArgumentException {
		final int
			sep1pos = sizeInfo.indexOf(SEP1, 0),
			sep2pos = sizeInfo.indexOf(SEP2, 0);
		if(sep1pos < 0) {
			min = toFixedSize(sizeInfo);
		} else {
			min = toFixedSize(sizeInfo.substring(0, sep1pos));
			if(sep2pos < 0) {
				range = toFixedSize(sizeInfo.substring(sep1pos + 1)) - min;
			} else {
				range = toFixedSize(sizeInfo.substring(sep1pos + 1, sep2pos)) - min;
				bias = Double.parseDouble(sizeInfo.substring(sep2pos + 1));
			}
		}
		if(range < 0) {
			throw new IllegalArgumentException("Min value is less than max: \"" + sizeInfo + "\"");
		}
	}

	public SizeInBytes(final long size) {
		this(size, size, 1);
	}

	public SizeInBytes(final long min, final long max, final double bias) {
		if(min < 0) {
			throw new IllegalArgumentException("Min size is less than 0");
		}
		if(min > max) {
			throw new IllegalArgumentException("Min size is more than max");
		}
		this.min = min;
		this.range = max - min;
		this.bias = bias;
	}

	/**
	 * Copy constructor
	 */
	public SizeInBytes(final SizeInBytes other) {
		this.min = other.min;
		this.range = other.range;
		this.bias = other.bias;
	}

	/**
	 * @return the numeric size if fixed, the random size in the specified range if not fixed (the bias also used)
	 */
	public long get() {
		if(range == 0) {
			return min;
		} else if(bias == 1) {
			return min + ThreadLocalRandom.current().nextLong(range + 1);
		} else {
			return min + (long) (Math.pow(ThreadLocalRandom.current().nextDouble(), bias) * range);
		}
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return min + range;
	}

	private static final int APPROXIMATION_COUNT = 100;

	/**
	 * @return the average fixed size value for 1st 100 returned by {@link SizeInBytes#get()} size values
	 */
	public final long getAvg() {
		if(range == 0) {
			return min;
		} else {
			long sum = 0;
			for(var i = 0; i < APPROXIMATION_COUNT; i ++) {
				sum += get();
			}
			return sum / APPROXIMATION_COUNT;
		}
	}

	private static final ThreadLocal<StringBuilder> STRING_BULDER = ThreadLocal.withInitial(
		StringBuilder::new
	);

	@Override
	public final String toString() {
		final var sb = STRING_BULDER.get();
		sb.setLength(0);
		sb.append(formatFixedSize(min));
		if(range > 0) {
			sb.append(SEP1).append(formatFixedSize(min + range));
			if(bias != 1) {
				sb.append(SEP2).append(Double.toString(bias));
			}
		}
		return sb.toString();
	}

	/**
	 * Compare two sizes
	 * @return true if fixed sizes are equal either minimums &amp; ranges &amp; biases are equal, false otherwise
	 */
	@Override
	public final boolean equals(final Object o) {
		if(o instanceof SizeInBytes) {
			final var s = (SizeInBytes) o;
			return min == s.min && range == s.range && bias == s.bias;
		} else {
			return false;
		}
	}
}
