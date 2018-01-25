package com.github.akurilov.commons.func;

import java.util.function.Function;

@FunctionalInterface
public interface Function4<A, B, C, D, Z> {

	Z apply(final A a, final B b, final C c, final D d);

	static <A, B, C, D, Z> Function<D, Z> partial(
		final Function4<A, B, C, D, Z> f, final A a, final B b, final C c
	) {
		return (d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<C, D, Z> partial(
		final Function4<A, B, C, D, Z> f, final A a, final B b
	) {
		return (c, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function3<B, C, D, Z> partial(
		final Function4<A, B, C, D, Z> f, final A a
	) {
		return (b, c, d) -> f.apply(a, b, c, d);
	}
}
