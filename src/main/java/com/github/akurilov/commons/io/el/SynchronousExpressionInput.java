package com.github.akurilov.commons.io.el;

import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import java.util.List;

/**
 A marker interface to determine if the given expression input is synchronous.
 @param <T>
 */
public interface SynchronousExpressionInput<T>
extends ExpressionInput<T> {

	/**
	 Get a new result of the synchronous expression evaluation
	 @throws PropertyNotFoundException
	 @throws ELException
	 */
	@Override
	T get()
	throws PropertyNotFoundException, ELException;

	/**
	 Bulk new values get
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
	 * @throws PropertyNotFoundException
	 * @throws ELException
	 */
	@Override
	long skip(final long count)
	throws PropertyNotFoundException, ELException;
}
