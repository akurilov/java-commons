package com.github.akurilov.commons.io.el;

import java.util.function.IntFunction;
import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionInputTest {

	@Test
	public void testMillisSinceEpoch()
	throws Exception {
		final var in = ExpressionInput.<Long>builder()
			.expr("${time:millisSinceEpoch()}")
			.type(long.class)
			.func("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
			.build();
		assertTrue(in instanceof SynchronousExpressionInput);
		final var t0 = currentTimeMillis();
		final var t1 = Long.parseLong(in.get().toString());
		final var t2 = currentTimeMillis();
		assertTrue(t0 <= t1);
		assertTrue(t1 <= t2);
	}

	@Test
	public void testMillisSinceEpochDeferred()
	throws Exception {
		final var in = ExpressionInput.<Long>builder()
			.expr("#{time:millisSinceEpoch()}")
			.type(long.class)
			.func("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
			.build();
		assertFalse(in instanceof SynchronousExpressionInput);
		assertNull(in.get());
		in.call();
		final var t0 = currentTimeMillis();
		final var t1 = Long.parseLong(in.get().toString());
		final var t2 = currentTimeMillis();
		assertTrue(t0 <= t1);
		assertTrue(t1 <= t2);
	}

	@Test
	public void testRandomIdWithPrefix()
	throws Exception {
		final IntFunction<String> idSupplier = (radix) -> Long.toString(
			abs(Long.reverse(currentTimeMillis()) ^ Long.reverseBytes(nanoTime())),
			radix
		);
		final var in = ExpressionInput.<String>builder()
			.value("idSupplier", idSupplier, IntFunction.class)
			.value("radix", 36, int.class)
			.expr("prefix_${idSupplier.apply(radix)}")
			.type(String.class)
			.build();
		System.out.println(in.get());
	}

	@Test
	public void testIota()
	throws Exception {
		final var in = ExpressionInput.<Integer>builder()
			.expr("${this.last() + 1}")
			.initial(-1)
			.type(int.class)
			.build();
		assertEquals(0, (int) in.get());
		assertEquals(1, (int) in.get());
		assertEquals(2, (int) in.get());
	}
}
