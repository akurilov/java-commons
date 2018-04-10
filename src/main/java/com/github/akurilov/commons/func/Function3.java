package com.github.akurilov.commons.func;

import java.util.function.Function;

@FunctionalInterface
public interface Function3<A, B, C, Z> {

	Z apply(final A a, final B b, final C c);

	static <A, B, C, Z> Function<C, Z> partial12(
		final Function3<A, B, C, Z> f, final A a, final B b
	) {
		return (c) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function<B, Z> partial13(
		final Function3<A, B, C, Z> f, final A a, final C c
	) {
		return (b) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function<A, Z> partial23(
		final Function3<A, B, C, Z> f, final B b, final C c
	) {
		return (a) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function2<B, C, Z> partial1(final Function3<A, B, C, Z> f, final A a) {
		return (b, c) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function2<A, C, Z> partial2(final Function3<A, B, C, Z> f, final B b) {
		return (a, c) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function2<A, B, Z> partial3(final Function3<A, B, C, Z> f, final C c) {
		return (a, b) -> f.apply(a, b, c);
	}
}
