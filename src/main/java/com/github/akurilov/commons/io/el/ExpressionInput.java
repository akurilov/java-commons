package com.github.akurilov.commons.io.el;

import com.github.akurilov.commons.io.Input;

import de.odysseus.el.ExpressionFactoryImpl;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 @param <T>
 */
public interface ExpressionInput<T>
extends Callable<T>, Input<T> {

	String LAST_VALUE_ID = "x";
	ExpressionFactory FACTORY = new ExpressionFactoryImpl();

	/**
	 Perform the expression evaluation
	 @return the expression evaluation result
	 */
	@Override
	T call();

	/**
	 Get last value. A user should invoke call() method by itself to evaluate the expression if the given expression
	 input is not synchronous. The initial value only would be returned otherwise.
	 @throws PropertyNotFoundException
	 @throws ELException
	 */
	@Override
	T get()
	throws PropertyNotFoundException, ELException;

	/**
	 Bulk values get. A user should invoke call() method by itself to evaluate the expression if the given expression
	 input is not synchronous. The initial value only would be returned otherwise.
	 @param buffer buffer for the values
	 @param limit max count of the values to put into the buffer
	 @throws PropertyNotFoundException
	 @throws ELException
	 */
	@Override
	int get(final List<T> buffer, final int limit)
	throws PropertyNotFoundException, ELException;

	/**
	 * Skip some items.
	 * @param count count of items should be skipped from the input stream
	 */
	@Override
	long skip(final long count);

	/**
	 Set the last value to the initial one.
	 */
	@Override
	void reset();

	/**
	 @return the expression string
	 */
	String expr();

	/**
	 @return the result type
	 */
	Class<T> type();

	interface Builder<T> {

		Builder<T> expr(final String expr);

		Builder<T> initial(final T value);

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
