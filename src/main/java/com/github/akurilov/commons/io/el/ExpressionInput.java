package com.github.akurilov.commons.io.el;

import com.github.akurilov.commons.io.Input;

import de.odysseus.el.ExpressionFactoryImpl;

import javax.el.ExpressionFactory;
import java.lang.reflect.Method;

public interface ExpressionInput<T>
extends Input<T> {

	ExpressionFactory FACTORY = new ExpressionFactoryImpl();

	String expr();

	Class<T> type();

	default void reset() {
	}

	interface Builder<T> {

		Builder<T> expr(final String expr);

		Builder<T> type(final Class<T> type);

		Builder<T> func(final String prefix, final String name, final Method method);

		Builder<T> value(final String name, final Object value, final Class<?> type);

		Builder<T> sync(final boolean syncFlag);

		ExpressionInput<T> build();
	}

	static <T> Builder<T> builder() {
		return new ExpressionInputBuilder<>();
	}
}
