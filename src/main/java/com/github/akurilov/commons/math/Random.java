package com.github.akurilov.commons.math;

/**
 * Faster random implementation which uses the XorShift algorithm
 */
public final class Random {
	
	private static final double DOUBLE_UNIT = 0x1.0p-53;

	private long seed;

	public Random() {
		reset();
	}

	public void reset() {
		seed = System.nanoTime() ^ System.currentTimeMillis();
	}

	public Random(final long seed) {
		this.seed = seed;
	}

	public final long nextLong() {
		return seed = MathUtil.xorShift(seed);
	}

	public final long nextLong(final long range) {
		return Math.abs(nextLong() % range);
	}

	public final int nextInt() {
		return (int) nextLong();
	}

	public final int nextInt(final int range) {
		return (int) nextLong(range);
	}

	public final double nextDouble() {
		seed = MathUtil.xorShift(seed);
		return (((seed >>> 22) << 27) + (seed >>> 21)) * DOUBLE_UNIT;
	}
}
