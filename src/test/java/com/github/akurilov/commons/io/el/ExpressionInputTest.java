package com.github.akurilov.commons.io.el;

import static java.lang.System.currentTimeMillis;

import com.github.akurilov.commons.math.MathUtil;
import de.odysseus.el.tree.TreeBuilderException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExpressionInputTest {

	@Test
	public void userFunctionInExpression()
	throws Exception {
		final var in = ExpressionInput.builder()
			.expression("${time:millisSinceEpoch()}")
			.function("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
			.<ExpressionInput<Long>>build();
		assertTrue(in instanceof SynchronousExpressionInput);
		final var t0 = currentTimeMillis();
		final var t1 = in.get();
		final var t2 = currentTimeMillis();
		assertTrue(t0 <= t1);
		assertTrue(t1 <= t2);
	}

	@Test
	public void externalExpressionEvaluation()
	throws Exception {
		final var in = ExpressionInput.builder()
			.expression("#{time:millisSinceEpoch()}")
			.function("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
			.<ExpressionInput<Long>>build();
		assertFalse(in instanceof SynchronousExpressionInput);
		assertNull(in.get());
		in.call();
		final var t0 = currentTimeMillis();
		final var t1 = in.get();
		final var t2 = currentTimeMillis();
		assertTrue(t0 <= t1);
		assertTrue(t1 <= t2);
	}

	@Test
	public void incrementLastWithInitialValue() {
		final var in = ExpressionInput.builder()
			.expression("${this.last() + 1}%{-1}")
			.<ExpressionInput<Long>>build();
		assertEquals(0, in.get().intValue());
		assertEquals(1, in.get().intValue());
		assertEquals(2, in.get().intValue());
	}

	@Test
	public void incrementLastNoInitialValue() {
		final var in = ExpressionInput.builder()
			.expression("${this.last() + 1}")
			.<ExpressionInput<Long>>build();
		assertEquals(1, in.get().intValue());
		assertEquals(2, in.get().intValue());
		assertEquals(3, in.get().intValue());
	}

	@Test
	public void selfReference() {
		final var in = ExpressionInput.builder()
			.expression("${this}")
			.<ExpressionInput<ExpressionInput>>build();
		final var x = in.get();
		assertEquals(in, x);
		final var y = x.get();
		assertEquals(x, y);
	}

	@Test
	public void selfReferenceAsInitialValueWithIncrement() {
		final var in = ExpressionInput.builder()
			.expression("%{this.last() + 1}")
			.<ExpressionInput<Long>>build();
		assertEquals(1, in.get().intValue());
		assertEquals(1, in.get().intValue());
	}

	@Test
	public void selfReferenceAsInitialValue() {
		final var in = ExpressionInput.builder()
			.expression("%{this.last()}")
			.build();
		assertNull(null, in.get());
	}

	@Test
	public void initOnlyExpressionYieldsConstantValue()
	throws Exception {
		final var t0 = System.currentTimeMillis();
		TimeUnit.MILLISECONDS.sleep(100);
		final var in = ExpressionInput.builder()
			.expression("%{time:millisSinceEpoch()}")
			.function("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
			.<ExpressionInput<Long>>build();
		TimeUnit.MILLISECONDS.sleep(100);
		final var t2 = System.currentTimeMillis();
		final var t1 = in.get();
		assertTrue(t0 < t1);
		assertTrue(t1 < t2);
		TimeUnit.MILLISECONDS.sleep(100);
		final var t3 = in.get();
		assertEquals(t1, t3);
	}

	@Test
	public void emptyExpressionCausesExceptionOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression("")
				.<ExpressionInput<String>>build();
			fail();
		} catch(final IllegalArgumentException e) {
			assertEquals(
				"The expression \"\" doesn't match the pattern: ([$#]\\{[^}]+})?(%\\{[^}]+})?",
				e.getMessage()
			);
		}
	}

	@Test
	public void emptyExpressionInsideBracketsCausesExceptionOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression("${}")
				.<ExpressionInput<String>>build();
			fail();
		} catch(final IllegalArgumentException e) {
			assertEquals(
				"The expression \"${}\" doesn't match the pattern: ([$#]\\{[^}]+})?(%\\{[^}]+})?",
				e.getMessage()
			);
		}
	}

	@Test
	public void expressionToYieldEmptyString() {
		final var in = ExpressionInput.builder()
			.expression("${\"\"}")
			.<ExpressionInput<String>>build();
		assertEquals("", in.get());
	}

	@Test
	public void nullExpressionCausesNpeOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression(null)
				.<ExpressionInput<String>>build();
			fail();
		} catch(final Throwable thrown) {
			assertTrue(thrown instanceof NullPointerException);
		}
	}

	@Test
	public void nestedExpressionCausesExceptionOnBuild()
	throws Exception {
		try {
			final var in = ExpressionInput.builder()
				.expression("${math:xorShift(${time:millisSinceEpoch()})")
				.function("math", "xorShift", MathUtil.class.getMethod("xorShift", long.class))
				.function("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
				.<ExpressionInput<Long>>build();
			fail();
		} catch(final Throwable thrown) {
			assertTrue(thrown instanceof TreeBuilderException);
		}
	}

	@Test
	public void invalidExpressionWithPrefixCausesExceptionOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression("foo${\"bar\"}")
				.<ExpressionInput<String>>build();
			fail();
		} catch(final IllegalArgumentException e) {
			assertEquals(
				"The expression \"foo${\"bar\"}\" doesn't match the pattern: ([$#]\\{[^}]+})?(%\\{[^}]+})?",
				e.getMessage()
			);
		}
	}

	@Test
	public void invalidExpression$CausesExceptionOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression("$")
				.<ExpressionInput<String>>build();
			fail();
		} catch(final IllegalArgumentException e) {
			assertEquals(
				"The expression \"$\" doesn't match the pattern: ([$#]\\{[^}]+})?(%\\{[^}]+})?",
				e.getMessage()
			);
		}
	}

	@Test
	public void invalidExpression42CausesExceptionOnBuild() {
		try {
			final var in = ExpressionInput.builder()
				.expression("42")
				.<ExpressionInput<String>>build();
			fail();
		} catch(final IllegalArgumentException e) {
			assertEquals(
				"The expression \"42\" doesn't match the pattern: ([$#]\\{[^}]+})?(%\\{[^}]+})?",
				e.getMessage()
			);
		}
	}
}
