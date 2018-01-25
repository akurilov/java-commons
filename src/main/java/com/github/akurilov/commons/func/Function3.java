package com.github.akurilov.commons.func;

import java.util.function.Function;

@FunctionalInterface
public interface Function3<A, B, C, Z> {

	Z apply(final A a, final B b, final C c);

	static <A, B, C, Z> Function<C, Z> partial(
		final Function3<A, B, C, Z> f, final A a, final B b
	) {
		return (c) -> f.apply(a, b, c);
	}

	static <A, B, C, Z> Function2<B, C, Z> partial(final Function3<A, B, C, Z> f, final A a) {
		return (b, c) -> f.apply(a, b, c);
	}
}
