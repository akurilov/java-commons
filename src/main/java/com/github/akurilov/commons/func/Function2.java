package com.github.akurilov.commons.func;

import java.util.function.Function;

@FunctionalInterface
public interface Function2<A, B, Z> {

	Z apply(final A a, final B b);

	static <A, B, Z> Function<B, Z> partial(final Function2<A, B, Z> f2, final A a) {
		return (b) -> f2.apply(a, b);
	}
}
