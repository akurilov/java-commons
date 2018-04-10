package com.github.akurilov.commons.func;

import java.util.function.Function;

@FunctionalInterface
public interface Function4<A, B, C, D, Z> {

	Z apply(final A a, final B b, final C c, final D d);

	static <A, B, C, D, Z> Function<D, Z> partial123(
		final Function4<A, B, C, D, Z> f, final A a, final B b, final C c
	) {
		return (d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function<C, Z> partial124(
		final Function4<A, B, C, D, Z> f, final A a, final B b, final D d
	) {
		return (c) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function<B, Z> partial134(
		final Function4<A, B, C, D, Z> f, final A a, final C c, final D d
	) {
		return (b) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function<A, Z> partial234(
		final Function4<A, B, C, D, Z> f, final B b, final C c, final D d
	) {
		return (a) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<C, D, Z> partial12(
		final Function4<A, B, C, D, Z> f, final A a, final B b
	) {
		return (c, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<B, D, Z> partial13(
		final Function4<A, B, C, D, Z> f, final A a, final C c
	) {
		return (b, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<B, C, Z> partial14(
		final Function4<A, B, C, D, Z> f, final A a, final D d
	) {
		return (b, c) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<A, D, Z> partial23(
		final Function4<A, B, C, D, Z> f, final B b, final C c
	) {
		return (a, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<A, C, Z> partial24(
		final Function4<A, B, C, D, Z> f, final B b, final D d
	) {
		return (a, c) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function2<A, B, Z> partial34(
		final Function4<A, B, C, D, Z> f, final C c, final D d
	) {
		return (a, b) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function3<B, C, D, Z> partial1(
		final Function4<A, B, C, D, Z> f, final A a
	) {
		return (b, c, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function3<A, C, D, Z> partial2(
		final Function4<A, B, C, D, Z> f, final B b
	) {
		return (a, c, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function3<A, B, D, Z> partial3(
		final Function4<A, B, C, D, Z> f, final C c
	) {
		return (a, b, d) -> f.apply(a, b, c, d);
	}

	static <A, B, C, D, Z> Function3<A, B, C, Z> partial4(
		final Function4<A, B, C, D, Z> f, final D d
	) {
		return (a, b, c) -> f.apply(a, b, c, d);
	}
}
